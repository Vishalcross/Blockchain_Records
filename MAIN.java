import java.io.IOException;
import java.math.BigInteger;
import java.util.ArrayList;

// Java program to illustrate 
// stopping a thread using boolean flag 

// class MyThread implements Runnable { 

// 	// to stop the thread 
// 	private boolean exit; 

//     private String name;
//     public int count; 
// 	Thread t; 

// 	MyThread(String threadname) 
// 	{ 
//         name = threadname;
//         this.count = 0; 
// 		t = new Thread(this, name); 
// 		System.out.println("New thread: " + t); 
// 		exit = false; 
// 		t.start(); // Starting the thread 
// 	} 

// 	// execution of thread starts from run() method 
// 	public void run() 
// 	{ 
// 		int i = 0; 
// 		while (!exit) {
//             count++; 
// 			System.out.println(name + ": " + i); 
// 			i++; 
// 			try { 
// 				Thread.sleep(100); 
// 			} 
// 			catch (InterruptedException e) { 
// 				System.out.println("Caught:" + e); 
// 			} 
// 		} 
// 		System.out.println(name + " Stopped."); 
// 	} 

// 	// for stopping the thread 
// 	public void stop() 
// 	{ 
// 		exit = true; 
// 	} 
// } 

// Main class 
public class MAIN { 
	public static void main(String args[]) throws IOException, ClassNotFoundException{
		Message m = new Message();
		byte[] send = m.introduceUser("username", new BigInteger("2"), new BigInteger("97"), new BigInteger("101"));
		ArrayList<Object> recieve =  m.unWrapMessage(send);
		System.out.println(recieve.get(0));
		// for(int i=0;i<10;i++){
		// 	// System.out.println(i);
		// 	if(i == 9){
		// 		dg.stop();
		// 	}
		// 	try{
		// 		Thread.sleep(1000);
		// 	}
		// 	catch(InterruptedException e){
		// 		e.printStackTrace();
		// 	}
		// }
	} 
} 
