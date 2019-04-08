import java.util.*;
import java.math.BigInteger;
import java.security.MessageDigest;
// import java.security.*;/
class User{
    String username;
	// String pass;
    HashMap<String,ArrayList<BigInteger>> usernameToPublicKey;
    ArrayList<Transaction> passBook;
	ArrayList<Transaction> currentBuffer;
    BigInteger privateKey;
    BigInteger publicKey;
    Group group;
	Blockchain blockchain;
	boolean firstUser;
	public User(String username, boolean first){
        this.username = username;
        this.group = new Group();
		// this.pass=pass;
		this.firstUser = first;
        this.privateKey = new BigInteger(""+new Random().nextInt(10000));
		this.publicKey = this.group.generator.modPow(privateKey, group.prime);
		if(first){
			//handle the base case
			this.blockchain = new Blockchain(4, 5); //initialise the blockchain
			this.usernameToPublicKey = new HashMap<>();
			usernameToPublicKey.put(username,new ArrayList<BigInteger>());
			usernameToPublicKey.get(username).add(this.group.generator);
			usernameToPublicKey.get(username).add(this.group.prime);
			usernameToPublicKey.get(username).add(this.publicKey);
			currentBuffer = new ArrayList<>();
		}
    }

    Transaction createTransaction(int idNo, String crime){
		Transaction temp = new Transaction(idNo, crime);
		return temp;
    }

    
    boolean verifyTransaction(ArrayList<Object> message){
        //User verifier = new User("hello1", "world1", new BigInteger("1234") );
		//User prover = new User("hello2", "world2", new BigInteger("6789") );
		// ArrayList<String> channel = new ArrayList<>();
		// Group group = new Group();
		// String md5hash = "fc5e038d38a57032085441e7fe7010b0";

		BigInteger m = new BigInteger(((Transaction)message.get(2)).getHash(), 16);
		ArrayList<BigInteger> reference = usernameToPublicKey.get((String)message.get(1));
		BigInteger generator = reference.get(0); /*group.generator*/;
		BigInteger p = reference.get(1); /*group.prime*/;
		BigInteger publicsSecret = reference.get(2);
        
		// BigInteger privateSecret = new BigInteger("1234");
		// Random random = new Random();
		// BigInteger r = new BigInteger(""+random.nextInt(100));
        
		// System.out.println("The random number is "+r);
        
		// m x (mod p), m r (mod p), A r (mod p)
		// BigInteger alpha = m.modPow(privateSecret,p);
		// BigInteger beta = m.modPow(r,p);
		// BigInteger gamma = generator.modPow(r,p);
		// System.out.println("Alpha beta gamma are "+alpha+" "+beta+" "+gamma);
		
		
		// String temp = ""+alpha.toString()+beta.toString()+gamma.toString();
		// String tempMd5 = getMd5(temp);
		// System.out.println("tempMd5 is "+tempMd5);
        
		// BigInteger c = new BigInteger(tempMd5,16);
		// System.out.println("c is "+c);
		// c = c.multiply(privateSecret);
		// BigInteger s = c.add(r);
		// System.out.println("s is "+s);
		// channel.add(s.toString());
		// channel.add(alpha.toString());
		// channel.add(beta.toString());
		// channel.add(gamma.toString());
		// System.out.println("Digital signature published");
        
		//verifier
		BigInteger s = (BigInteger)message.get(3);
		BigInteger alpha = (BigInteger)message.get(4);
		BigInteger beta = (BigInteger)message.get(5);
		BigInteger gamma = (BigInteger)message.get(6);
		// BigInteger s1 = new BigInteger(channel.get(0));
		// BigInteger alpha1 = new BigInteger(channel.get(1));
		// BigInteger beta1 = new BigInteger(channel.get(2));
		// BigInteger gamma1 = new BigInteger(channel.get(3));
		String temp = ""+alpha.toString()+beta.toString()+gamma.toString();
		String hashOfC = StringUtil.applySha256(temp);
		// System.out.println("temp2md5 is "+temp2md5);
		BigInteger c = new BigInteger(hashOfC,16);
        
		//verify
		// A s (mod p) = (A x ) c × (A r )(mod p) and
		// m s (mod p) = (m x ) c × (m r )(mod p)
        
		BigInteger t1 = generator.modPow(s,p);
		// BigInteger t2 = publicsSecret.pow(c);
		BigInteger t2 = publicsSecret.modPow(c,p);
		t2 = t2.multiply(gamma);
		BigInteger t3 = t2.mod(p);
        
		// System.out.println("Debug***************************");
		// System.out.println("m = "+m+" r = "+r+" ");
		// System.out.println("x = "+privateSecret+" c1 = "+c1);
		// System.out.println(gamma+" "+gamma1);
		// System.out.println("Debug***************************");
        
		BigInteger t4 = m.modPow(s,p);
		BigInteger t5 = alpha.modPow(c,p);
		BigInteger t6 = t5.multiply(beta).mod(p);
		System.out.println(t4);
		System.out.println(t5);
		System.out.println(t6);
        
		if(t1.equals(t3) && t4.equals(t6) ) return true; //System.out.println("It is true");
		// else System.out.println("Va fail");
		return false;
	}
	
    int mineBlock(){
		int nonce = 0;
		
		String target = new String(new char[blockchain.difficulty]).replace('\0','0');
		String data = "";
		for(int i=0;i<currentBuffer.size();i++){
			data += currentBuffer.get(i).getString();
		}
		String hash = "";
		while(!hash.substring(0,blockchain.difficulty).equals(target)){
			nonce++;
			hash = StringUtil.applySha256(blockchain.getLastHash() + Integer.toString(nonce) + data);
		}
		System.out.println("Kar-ching: "+hash);
		return nonce;
	}

	void printPassbook(){
		for(int i=0;i<passBook.size();i++){
			System.out.println("ID: "+passBook.get(i).gunah.idNo+"\nCrime: "+passBook.get(i).gunah.crime);
		}
	}

	//kahin to user call karega transaction with speed
	//create a func. to make a block
	//create a func. to send a block across the network
	//create a func. to verify a transaction
	//
	//create a func. to do mining

}

class StringUtil {
	//Applies Sha256 to a string and returns the result.
	public static String applySha256(String input){
		try {
			MessageDigest digest = MessageDigest.getInstance("SHA-256");
			//Applies sha256 to our input,
			byte[] hash = digest.digest(input.getBytes("UTF-8"));
			StringBuffer hexString = new StringBuffer(); // This will contain hash as hexidecimal
			for (int i = 0; i < hash.length; i++) {
				String hex = Integer.toHexString(0xff & hash[i]);
				if(hex.length() == 1) hexString.append('0');
				hexString.append(hex);
			}
			return hexString.toString();
		}
		catch(Exception e) {
			throw new RuntimeException(e);
		}
	}
}
