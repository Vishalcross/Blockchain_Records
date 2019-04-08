
import java.util.*;
import java.net.*;
import java.io.*;
import java.math.BigInteger;
class Main{
	public static String name;
	public static boolean readyForTransaction = false;
	// public static boolean welcome = false; 
	static boolean running = true;
	static ArrayList<Object> channel = new ArrayList<>();
	static Transaction tempTransaction = new Transaction(123123,"");
	public static void main(String[] args) {
		try{ 
			InetAddress group = InetAddress.getByName("239.0.0.0"); 
			int port = Integer.parseInt("1234"); 
			// Scanner sc = new Scanner(System.in); 
			// System.out.print("Enter your name: "); 
			// name = sc.nextLine(); 
			//welcome messages for a new user and user creation 
			System.out.print("Welcome to the network, please register yourself\n");
			Scanner scan = new Scanner(System.in);
			System.out.print("Enter your desired username\n");
			String uname = scan.nextLine();
			System.out.print("Are you the first user(yes/no)\n");
			String first = scan.nextLine();
			User currentUser = new User(uname,first.compareTo("yes")==0?true:false);
			System.out.println("Welcome " +uname+", would you like to manually enter the criminal records or have a random insertion\nPress y for manual input and n for random");
			name = uname;
			char choice = (scan.nextLine()).charAt(0);

			if(choice!='y' || choice!='n')
				System.out.println("Should select y or n");

			MulticastSocket socket = new MulticastSocket(port); 
			
			// Since we are deploying 
			socket.setTimeToLive(0); 
			//this on localhost only (For a subnet set it as 1) 
			
			socket.joinGroup(group);
			// ReadThread rt = new ReadThread();
			Thread t = new Thread(new ReadThread(name,socket,group,port)); 
		
			// Spawn a thread for reading messages 
			t.start();
			// sent to the current group 
			Message m = new Message();
			//sending hi
			byte[] intro = m.introduceUser(uname, currentUser.group.generator, currentUser.group.prime, currentUser.publicKey);
			DatagramPacket datagram = new DatagramPacket(intro,intro.length,group,port); 
			socket.send(datagram);
			while(running){ 
				System.out.print("");
				if(channel.size() > 0){
					if(currentUser.firstUser){
						if(readyForTransaction){
							if(choice == 'y'){
								System.out.println("enter the ID number of criminal.");
								int idNo=scan.nextInt();	
								String junk = scan.nextLine();							
								System.out.println("enter the details of the crime");
								String crimeDetails = scan.nextLine();
								Transaction newTransaction = new Transaction(idNo, crimeDetails);
								if((int)channel.get(0) == Message.transactionContainer){
									System.out.println("There is another transaction in the pipeline");
									readyForTransaction = false;
									continue;
								}
								Random newRandomNumber = new Random();
								BigInteger randNo=new BigInteger(""+newRandomNumber.nextInt(100));
								//m x (mod p), m r (mod p), A r (mod p);
								String temp = newTransaction.getHash();
								BigInteger msg = new BigInteger(temp,16);

								BigInteger alpha = msg.modPow(currentUser.privateKey,currentUser.group.prime);
								BigInteger beta = msg.modPow(randNo,currentUser.group.prime);
								BigInteger gamma = currentUser.group.generator.modPow(randNo,currentUser.group.prime);
								BigInteger c = new BigInteger(StringUtil.applySha256(""+alpha.toString()+beta.toString()+gamma.toString()),16);
								c = c.multiply(currentUser.privateKey);
								BigInteger s = c.add(randNo);

								byte[] newByte = m.transactionContainer(currentUser.username,newTransaction,s,alpha,beta,gamma);

								System.out.println("should I send?");
								String somethingRandom = scan.nextLine();

								datagram = new DatagramPacket(newByte,newByte.length,group,port); 
								socket.send(datagram);
								tempTransaction = newTransaction;
								System.out.println("Message sent for verification");
								readyForTransaction = false;

							}

						}

						if(currentUser.usernameToPublicKey.size() == 1){
							System.out.println("I'm lonely");
						}

						if((int)channel.get(0) == Message.intro){
							readyForTransaction = false;
							if(((String)channel.get(1)).compareTo(currentUser.username) != 0){
								System.out.println("welcome "+(String)channel.get(1));
								ArrayList<BigInteger> temp = new ArrayList<>();
								temp.add( (BigInteger)channel.get(2));
								temp.add((BigInteger)channel.get(3));
								temp.add((BigInteger)channel.get(4));
								currentUser.usernameToPublicKey.put((String)channel.get(1),temp);
	
								byte[] welkum = m.welcomeUser((String)channel.get(1), currentUser.usernameToPublicKey, currentUser.blockchain, currentUser.currentBuffer);
								datagram = new DatagramPacket(welkum,welkum.length,group,port); 
								socket.send(datagram);
								// break;
							}
							else{
								System.out.println("LET IT RIP");
							}
						}
						else if((int)channel.get(0) == Message.welcome){
							// break;
							readyForTransaction = true;
							try{
								Thread.sleep(100);
							}
							catch(InterruptedException e){
								e.printStackTrace();
							}
						}
						else if((int)channel.get(0) == Message.transactionContainer){
							boolean isVerified = currentUser.verifyTransaction(channel);
							byte[] reply = m.transactionVerificationReply(currentUser.username, (String)channel.get(1),isVerified);
							datagram = new DatagramPacket(reply,reply.length,group,port); 
							socket.send(datagram);
							System.out.println(isVerified?"Valid":"Not valid");
						}

						else if((int)channel.get(0) == Message.verificationReply){
							System.out.println((boolean)channel.get(3)?"Validated":"Not Validated");
							if((boolean)channel.get(3)){
								byte[] reply = m.storeTransactionNow(currentUser.username, tempTransaction);
								datagram = new DatagramPacket(reply,reply.length,group,port); 
								socket.send(datagram);
							}
							else{
								System.out.println("Verification failed");
								readyForTransaction = true;
							}
						}

						else if((int)channel.get(0) == Message.storeTransaction){
							System.out.println("I am storing this transaction");
							readyForTransaction = true;
							currentUser.currentBuffer.block.add((Transaction)channel.get(2));
							if(currentUser.currentBuffer.block.size() == currentUser.blockchain.limit){
								readyForTransaction = false;
								System.out.println("MINING");
								int nonce = currentUser.mineBlock();
								if((int)channel.get(0) == Message.hashMined){
									System.out.println("Already mined");
									continue;
								}
								else{
									byte[] reply = m.hashMinedInfo(currentUser.username, nonce);
									datagram = new DatagramPacket(reply,reply.length,group,port);
									socket.send(datagram);
								}
							}
						}

						else if((int)channel.get(0) == Message.hashMined){
							if(((String)channel.get(1)).compareTo(currentUser.username) != 0){
								boolean valid = currentUser.verifyMining((int)channel.get(2));
								System.out.println("Hash "+(valid?"valid":"invalid"));
								byte[] reply = m.hashVerified(currentUser.username, (String)channel.get(1), valid,(int)channel.get(2));
								datagram = new DatagramPacket(reply,reply.length,group,port);
								socket.send(datagram);
							}
						}

						else if((int)channel.get(0) == Message.hashVerified){
							if(((String)channel.get(2)).compareTo(currentUser.username) == 0){
								// increment the number of Valid replies
								if((boolean)channel.get(3)){
									String data = "";
									for(int i=0;i<currentUser.currentBuffer.block.size();i++){
										data += currentUser.currentBuffer.block.get(i).getString();
									}
									currentUser.currentBuffer.previousHash = currentUser.blockchain.getLastHash();
									currentUser.currentBuffer.hash = StringUtil.applySha256(currentUser.blockchain.getLastHash() + Integer.toString((int)channel.get(4)) + data);
									currentUser.currentBuffer.nonce = (int)channel.get(4);
									currentUser.blockchain.addBlock(currentUser.currentBuffer);
									byte[] reply = m.newBlockchain(currentUser.username, currentUser.blockchain);
									datagram = new DatagramPacket(reply,reply.length,group,port);
									socket.send(datagram);
									currentUser.currentBuffer.block.clear();
								}
								else{
									System.out.println("The block was invalid");
									readyForTransaction = true;
								}
							}
						}

						else if((int)channel.get(0) == Message.newBlockchain){
							readyForTransaction = true;
							System.out.println("The new block is added");
							if(((String)channel.get(1)).compareTo(currentUser.username) != 0){
								currentUser.blockchain = (Blockchain)channel.get(2);
							}
						}

						// System.out.println("Not anymore");
					}

					//Everyone but the first user
					else{
						if(readyForTransaction){
							if((int)channel.get(0) == Message.transactionContainer){
								readyForTransaction = false;
							}
							else{
								if(choice == 'y'){
									System.out.println("enter the ID number of criminal.");
									int idNo=scan.nextInt();
									String junk = scan.nextLine();								
									System.out.println("enter the details of the crime");
									String crimeDetails = scan.nextLine();
									if((int)channel.get(0) == Message.transactionContainer){
										System.out.println("There is a transaction pending in the pipeline");
										readyForTransaction = false;
										continue;
									}
									if((int)channel.get(0) == Message.hashMined){

									}
									Transaction newTransaction = new Transaction(idNo, crimeDetails);

									Random newRandomNumber = new Random();
									BigInteger randNo=new BigInteger(""+newRandomNumber.nextInt(100));
									//m x (mod p), m r (mod p), A r (mod p);
									String temp = newTransaction.getHash();
									BigInteger msg = new BigInteger(temp,16);

									BigInteger alpha = msg.modPow(currentUser.privateKey,currentUser.group.prime);
									BigInteger beta = msg.modPow(randNo,currentUser.group.prime);
									BigInteger gamma = currentUser.group.generator.modPow(randNo,currentUser.group.prime);
									BigInteger c = new BigInteger(StringUtil.applySha256(""+alpha.toString()+beta.toString()+gamma.toString()),16);
									c = c.multiply(currentUser.privateKey);
									BigInteger s = c.add(randNo);

									byte[] newByte = m.transactionContainer(currentUser.username,newTransaction,s,alpha,beta,gamma);

									System.out.println("should I send?");
									String somethingRandom = scan.nextLine();

									datagram = new DatagramPacket(newByte,newByte.length,group,port); 
									socket.send(datagram);
									readyForTransaction = false;
									System.out.println("Message sent for verification");
								}
							}
						}
						if((int)channel.get(0) == Message.intro){
							if(((String)channel.get(1)).compareTo(currentUser.username) != 0){
								// System.out.println("I welcome the new user");
								try{
									readyForTransaction = false;
									Thread.sleep(100);
								}
								catch(InterruptedException e){
									e.printStackTrace();
								}
							}
						}
						else if((int)channel.get(0) == Message.welcome){
							if(((String)channel.get(1)).compareTo(currentUser.username) == 0){
								System.out.println("I am inducted");
								currentUser.usernameToPublicKey = (HashMap<String,ArrayList<BigInteger>>)channel.get(2);
								currentUser.blockchain = (Blockchain)channel.get(3);
								currentUser.currentBuffer = (Block)channel.get(4);
								// break;
								readyForTransaction = true;
							}
							else{
								System.out.println("I welcome the new user");
								currentUser.usernameToPublicKey = (HashMap<String,ArrayList<BigInteger>>)channel.get(2);
								try{
									readyForTransaction = true;
									Thread.sleep(100);
								}
								catch(InterruptedException e){
									e.printStackTrace();
								}
							}
						}

						else if((int)channel.get(0) == Message.transactionContainer){
							boolean isVerified = currentUser.verifyTransaction(channel);
							byte[] reply = m.transactionVerificationReply(currentUser.username, (String)channel.get(1),isVerified);
							datagram = new DatagramPacket(reply,reply.length,group,port); 
							socket.send(datagram);
							System.out.println(isVerified?"Valid":"Not valid");
						}

						else if((int)channel.get(0) == Message.verificationReply){
							System.out.println((boolean)channel.get(3)?"Validated":"Not Validated");
							if((boolean)channel.get(3)){
								byte[] reply = m.storeTransactionNow(currentUser.username, tempTransaction);
								datagram = new DatagramPacket(reply,reply.length,group,port); 
								socket.send(datagram);
							}
							else{
								System.out.println("Verification failed");
								readyForTransaction = true;
							}
						}

						else if((int)channel.get(0) == Message.storeTransaction){
							System.out.println("I am storing this transaction");
							readyForTransaction = true;
							currentUser.currentBuffer.block.add((Transaction)channel.get(2));
							if(currentUser.currentBuffer.block.size() == currentUser.blockchain.limit){
								readyForTransaction = false;
								System.out.println("MINING");
								int nonce = currentUser.mineBlock();
								if((int)channel.get(0) == Message.hashMined){
									System.out.println("Already mined");
									continue;
								}
								else{
									byte[] reply = m.hashMinedInfo(currentUser.username, nonce);
									datagram = new DatagramPacket(reply,reply.length,group,port);
									socket.send(datagram);
								}
							}
						}

						else if((int)channel.get(0) == Message.hashMined){
							if(((String)channel.get(1)).compareTo(currentUser.username) != 0){
								boolean valid = currentUser.verifyMining((int)channel.get(2));
								System.out.println("Hash "+(valid?"valid":"invalid"));
								byte[] reply = m.hashVerified(currentUser.username, (String)channel.get(1), valid,(int)channel.get(2));
								datagram = new DatagramPacket(reply,reply.length,group,port);
								socket.send(datagram);
							}
						}

						else if((int)channel.get(0) == Message.hashVerified){
							if(((String)channel.get(2)).compareTo(currentUser.username) == 0){
								// increment the number of Valid replies
								if((boolean)channel.get(3)){
									String data = "";
									for(int i=0;i<currentUser.currentBuffer.block.size();i++){
										data += currentUser.currentBuffer.block.get(i).getString();
									}
									currentUser.currentBuffer.previousHash = currentUser.blockchain.getLastHash();
									currentUser.currentBuffer.hash = StringUtil.applySha256(currentUser.blockchain.getLastHash() + Integer.toString((int)channel.get(4)) + data);
									currentUser.currentBuffer.nonce = (int)channel.get(4);
									currentUser.blockchain.addBlock(currentUser.currentBuffer);
									byte[] reply = m.newBlockchain(currentUser.username, currentUser.blockchain);
									datagram = new DatagramPacket(reply,reply.length,group,port);
									socket.send(datagram);
									currentUser.currentBuffer.block.clear();
								}
							}
							else{
								System.out.println("The block was invalid");
								readyForTransaction = true;
							}
						}

						else if((int)channel.get(0) == Message.newBlockchain){
							System.out.println("The new block is added");
							readyForTransaction = true;
							if(((String)channel.get(1)).compareTo(currentUser.username) != 0)
								currentUser.blockchain = (Blockchain)channel.get(2);
							
						}
					}
				}
				
				/** application logic
				 * The first user waits for a registration before producing his transactions
				 * 
				 * If a new user is added, people wait for sometime so that his userTable and buffer
				 * can be built
				 * 
				 * If a transaction was sent to verification, it will be cancelled and resent
				 * 
				 * If a transaction was verified but not added to the buffer, do that before sending
				 * the userTable and buffer and blockchain
				 * 
				 * If mining is ongoing then the new user must wait before they can get started
				 * 
				 * Now that the user is created, they can start creating transactions and sending
				 * them to the group
				 * 
				 * A transaction is sent across to all for verification
				 * 
				 * People stop making transactions until the verification for a certain transaction
				 * request is pending
				 * 
				 * A verification reply is sent back to the sender, if >=n/2 people agree then the 
				 * transaction is sanctioned
				 * 
				 * A transaction add message is sent and everyone adds this transaction to the buffer
				 * 
				 * If the buffer is full, start the mining process and stop creating transactions
				 * 
				 * If you have found the nonce, send to all the users. Everyone stops mining and 
				 * veifies this number
				 * 
				 * Everyone sends the verification for block reply
				 * 
				 * If >=n/2 people say yes then the block is added by the miner and the new 
				 * blockchain is sent across the network
				 * 
				 * Everyone resumes operation and the new users waiting in line get their data
				 * structures
				*/

				
				// message = name + ": " + message;
				// this here contains a way to send a message 
				// Message m = new Message();
				// byte[] send = m.introduceUser("username", new BigInteger("2"), new BigInteger("97"), new BigInteger("101")); 
				// DatagramPacket datagram = new DatagramPacket(send,send.length,group,port); 
				// socket.send(datagram); 
			} 
			scan.close();
		} 
		catch(SocketException se) 
		{ 
			System.out.println("Error creating socket"); 
			se.printStackTrace(); 
		} 
		catch(IOException ie) 
		{ 
			System.out.println("Error reading/writing from/to socket"); 
			ie.printStackTrace(); 
		} 



		// System.out.print("Welcome to the network, please register yourself\n");
		// Scanner scan = new Scanner(System.in);
		// System.out.print("Enter your desired username\n");
		// String uname = scan.nextLine();
		// System.out.print("Are you the first user(yes/no)\n");
		// String first = scan.nextLine();
		// User currentUser = new User(uname,first.compareTo("yes")==0?true:false);
		// System.out.println("Welcome " +uname+", would you like to manually enter the criminal records or have a random insertion\nPress y for manual input and n for random");
		// char choice = (scan.nextLine()).charAt(0);
		// if(choice == 'y'){
		// 	while(true/* Stop this when you recieve a message */){
		// 		if(true /* activate when recieving a message */)
		// 			System.out.print("Enter the id of the criminal: ");
		// 			int idNo = scan.nextInt();
		// 			System.out.println("Enter the crime details\n");
		// 			String crime = scan.nextLine();
		// 			currentUser.createTransaction(idNo,crime);
		// 		}
		// 		else{

		// 		}
		// 	}
		// }
		// else{
		// 	System.out.println("Enter the speed of record creation is records per minute (integer values only)");
		// 	int speed = scan.nextInt();
		// 	Random random = new Random();
		// 	while(true /* Stop this when recieving a message */){
		// 		DataGenerator recordGenerator = new DataGenerator(speed);
		// 		if(true/* activate when receiving a message */){
		// 			recordGenerator.stop();
		// 			//handle the message
		// 		}
		// 	}
		// 	System.out.println();
		// }
	}

}

