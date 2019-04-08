import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.util.ArrayList;

class ReadThread implements Runnable 
{ 
	private MulticastSocket socket; 
	private InetAddress group; 
	private int port; 
	private static final int MAX_LEN = 1000;
	String name; 
	ReadThread(String name, MulticastSocket socket,InetAddress group,int port) 
	{ 
		this.name = name;
		this.socket = socket; 
		this.group = group; 
		this.port = port; 
	} 
	
	@Override
	public void run() 
	{ 
		while(!Communication.finished) 
		{ 
				byte[] buffer = new byte[ReadThread.MAX_LEN]; 
				DatagramPacket datagram = new
				DatagramPacket(buffer,buffer.length,group,port); 
			try
			{ 
				Message m = new Message();
				socket.receive(datagram); 
                ArrayList<Object> message = m.unWrapMessage(buffer); //new String(buffer,0,datagram.getLength(),"UTF-8");
                if(message.size() > 0){
                    Main.channel = message;
                    // if((int)message.get(0) == Message.intro){
                    //     if(((String)message.get(1)).compareTo(name) != 0){
                    //         Main.channel = message; 
                    //     }
                    //     Main.newuser = true;
                    // }
                    // else if((int)message.get(0) == Message.welcome){
                    //     if( ((String)message.get(1)).compareTo(name) == 0){
                    //         // Main.welcome = true;
                    //         Main.channel = message;
                    //     }
                    // }
                }
				// if(!message.startsWith(Communication.name)) dislplay only others' messages
				// System.out.println("recieved by "+name+" "+message); 

			} 
			catch(Exception e) 
			{ 
				System.out.println("Socket closed!"); 
			} 
		} 
	} 
}
