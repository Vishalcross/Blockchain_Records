import java.net.*;
import java.io.*;
import java.math.BigInteger;
import java.util.*;

public class Communication 
{ 
	private static final String TERMINATE = "Exit"; 
	static String name; 
	static volatile boolean finished = false; 

	public static void main(String[] args) 
	{ 
		if (args.length != 2) 
			System.out.println("Two arguments required: <multicast-host> <port-number>"); 
		else
		{ 
			try
			{ 
				InetAddress group = InetAddress.getByName(args[0]); 
				int port = Integer.parseInt(args[1]); 
				Scanner sc = new Scanner(System.in); 
				System.out.print("Enter your name: "); 
				name = sc.nextLine(); 
				MulticastSocket socket = new MulticastSocket(port); 
			
				// Since we are deploying 
				socket.setTimeToLive(0); 
				//this on localhost only (For a subnet set it as 1) 
				
				socket.joinGroup(group); 
				Thread t = new Thread(new ReadThread(name,socket,group,port)); 
			
				// Spawn a thread for reading messages 
				t.start(); 
				
				// sent to the current group 
				System.out.println("Start typing messages...\n"); 
				while(true) 
				{ 
					String message; 
					message = sc.nextLine(); 
					if(message.equalsIgnoreCase(Communication.TERMINATE)) 
					{ 
						finished = true; 
						socket.leaveGroup(group); 
						socket.close(); 
						break; 
					} 
					// message = name + ": " + message; 
					Message m = new Message();
					byte[] send = m.introduceUser("username", new BigInteger("2"), new BigInteger("97"), new BigInteger("101")); 
					DatagramPacket datagram = new DatagramPacket(send,send.length,group,port); 
					socket.send(datagram); 
				} 
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
		} 
	} 
} 
