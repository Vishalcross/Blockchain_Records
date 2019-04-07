class DataGenerator extends Thread{
	int speed;
	public void run(){
		while(){
			
		}
		int idNo = random.nextInt(1000);
		String crime = "";
		for(int i=0;i<5;i++){
			byte[] array = new byte[7]; // length is bounded by 7
			new Random().nextBytes(array);
			crime += new String(array, Charset.forName("UTF-8"));
		}
		System.out.printf("Generated record %d %s",idNo,crime);
	}
	DataGenerator(int speed){
		this.speed = speed;
	}
}
