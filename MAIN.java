// Java program to illustrate 
// stopping a thread using boolean flag 

class MyThread implements Runnable { 

	// to stop the thread 
	private boolean exit; 

    private String name;
    public int count; 
	Thread t; 

	MyThread(String threadname) 
	{ 
        name = threadname;
        this.count = 0; 
		t = new Thread(this, name); 
		System.out.println("New thread: " + t); 
		exit = false; 
		t.start(); // Starting the thread 
	} 

	// execution of thread starts from run() method 
	public void run() 
	{ 
		int i = 0; 
		while (!exit) {
            count++; 
			System.out.println(name + ": " + i); 
			i++; 
			try { 
				Thread.sleep(100); 
			} 
			catch (InterruptedException e) { 
				System.out.println("Caught:" + e); 
			} 
		} 
		System.out.println(name + " Stopped."); 
	} 

	// for stopping the thread 
	public void stop() 
	{ 
		exit = true; 
	} 
} 

// Main class 
public class MAIN { 
	public static void main(String args[]) 
	{ 
		// creating two objects t1 & t2 of MyThread 
		MyThread t1 = new MyThread("First thread"); 
        // MyThread t2 = new MyThread("Second thread");
        for(int i=0;i<5;i++){
            if(i == 4){ 
                try { 
                    Thread.sleep(500); 
                    t1.stop(); // stopping thread t1 
                    // t2.stop(); // stopping thread t2 
                    // Thread.sleep(500); 
                } 
                catch (InterruptedException e) { 
                    System.out.println("Caught:" + e); 
                } 
            }
        }
		System.out.println("Exiting the main Thread"); 
	} 
} 
