
import java.util.*;

class Main{
	public static void main(String[] args) {
		System.out.print("Welcome to the network, please register yourself\n");
		Scanner scan = new Scanner(System.in);
		System.out.print("Enter your desired username\n");
		String uname = scan.nextLine();
		System.out.print("Are you the first user(yes/no)\n");
		String first = scan.nextLine();
		User currentUser = new User(uname,first.compareTo("yes")==0?true:false);
		System.out.println("Welcome " +uname+", would you like to manually enter the criminal records or have a random insertion\nPress y for manual input and n for random");
		char choice = (scan.nextLine()).charAt(0);
		if(choice == 'y'){
			while(true/* Stop this when you recieve a message */){
				if(true /* activate when recieving a message */)
					System.out.print("Enter the id of the criminal: ");
					int idNo = scan.nextInt();
					System.out.println("Enter the crime details\n");
					String crime = scan.nextLine();
					currentUser.createTransaction(idNo,crime);
				}
				else{

				}
			}
		}
		else{
			System.out.println("Enter the speed of record creation is records per minute (integer values only)");
			int speed = scan.nextInt();
			Random random = new Random();
			while(true /* Stop this when recieving a message */){
				DataGenerator recordGenerator = new DataGenerator(speed);
				if(true/* activate when receiving a message */){
					recordGenerator.stop();
					//handle the message
				}
			}
			System.out.println();
		}
		scan.close();
	}

}
