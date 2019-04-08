
import java.util.*;
import java.net.*;
import java.io.*;
import java.math.BigInteger;
class Main{
	public static String name;
	public static boolean newuser = false;
	// public static boolean welcome = false; 
	static boolean running = true;
	static ArrayList<Object> channel;
	public static void main(String[] args) {
		try
			{ 
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
				// System.out.println("Start typing messages...\n"); 
				while(running){ 
					// String message; 
					// message = sc.nextLine(); 
					// if(message.equalsIgnoreCase(Communication.TERMINATE)) 
					// { 
					// 	finished = true; 
					// 	socket.leaveGroup(group); 
					// 	socket.close(); 
					// 	break; 
					// }

					if(currentUser.firstUser){
						if((int)channel.get(0) == Message.intro){
							System.out.println("welcome "+(String)channel.get(1));
							newuser = false;
							ArrayList<BigInteger> temp = new ArrayList<>();
							temp.add( (BigInteger)channel.get(2));
							temp.add((BigInteger)channel.get(3));
							temp.add((BigInteger)channel.get(4));
							currentUser.usernameToPublicKey.put((String)channel.get(1),temp);

							byte[] welkum = m.welcomeUser((String)channel.get(1), currentUser.usernameToPublicKey, currentUser.blockchain, currentUser.currentBuffer);
							datagram = new DatagramPacket(welkum,welkum.length,group,port); 
							socket.send(datagram);
						}
						if(currentUser.usernameToPublicKey.size() == 1){
							System.out.println("I'm lonely");
						}
						else{
							break;
						}
						// System.out.println("Not anymore");
					}
					else{
						if(newuser){
							if(((String)channel.get(1)).compareTo(currentUser.username) != 0){
								System.out.println("I welcome the new user");
								try{
									Thread.sleep(100);
								}
								catch(InterruptedException e){
									e.printStackTrace();
								}
								newuser = false;
							}
							else{
								System.out.println("I am inducted");
								currentUser.usernameToPublicKey = (HashMap<String,ArrayList<BigInteger>>)channel.get(2);
								currentUser.blockchain = (Blockchain)channel.get(3);
								currentUser.currentBuffer = (ArrayList<Transaction>)channel.get(4);
								newuser = false;
								break;	
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
					 * If a transaction was verified but now added to the buffer, do that before sending
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

