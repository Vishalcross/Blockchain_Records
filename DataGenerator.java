import java.nio.charset.Charset;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.Random;
class DataGenerator implements Runnable{
	int speed;
	Thread t;
	private boolean exit;
	DataGenerator(int speed){
		this.speed = speed;
		this.exit = false;
		System.out.println("ss");
		t = new Thread(this,"DataGenerator");
		t.start();
	}

	public void stop(){
		System.out.println("Stopping");
		this.exit = true;
	}

	public void run(){
		Random random = new Random();
		while(!this.exit){
			int idNo = random.nextInt(1000);
			String crime = "";
			for(int i=0;i<5;i++){
				String temp = "";
				for(int j=0;j<10;j++){
					char c = (char) ('a' + random.nextInt(26));
					temp += c;
				}
				crime += temp + " ";
			}
			System.out.printf("Generated record %d %s\n",idNo,crime);
			try{
				Thread.sleep( (long)(60000/speed) );
			}
			catch (InterruptedException e){
				e.printStackTrace();
			}
		}
	}
}
