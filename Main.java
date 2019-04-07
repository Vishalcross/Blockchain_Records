import java.nio.charset.Charset;
import java.util.*;

class Main{
	public static void main(String[] args) {
		System.out.print("Welcome to the network, please register yourself");
		Scanner scan = new Scanner(System.in);
		System.out.print("Enter your desired username: ");
		String uname = scan.nextLine();
		User currentUser = new User(uname);
		System.out.println("Welcome "+uname+", would you like to manually enter the criminal records or have a random insertion\nPress y for manual input and n for random");
		char choice = (scan.nextLine()).charAt(0);
		if(choice == 'y'){
			while(true/* Stop this when you recieve a message */){
				System.out.print("Enter the id of the criminal: ");
				int idNo = scan.nextInt();
				System.out.println("Enter the crime details\n");
				String crime = scan.nextLine();
				currentUser.createTransaction(idNo,crime);
			}
		}
		else{
			System.out.println("Enter the speed of record creation is records per minute (integer values only)");
			int speed = scan.nextInt();
			Random random = new Random();
			while(true /* Stop this when recieving a message */){
				DataGenerator recordGenerator = new DataGenerator(speed);
				
				
			}
			System.out.println();
		}
		scan.close();
	}

}
