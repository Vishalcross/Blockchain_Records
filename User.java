import java.util.*;
import java.math.BigInteger;
import java.security.*;
class User{
	String userName;
	String pass;
    // ArrayList<Transaction> passBook;
    BigInteger mySecret;
    BigInteger publicsSecret;
	public User(String userName,String pass, BigInteger mySecret)
	{
		this.userName=userName;
        this.pass=pass;
        this.mySecret = mySecret;
	}
	//kahin to user call karega transaction with speed
	//create a func. to make a block
	//create a func. to send a block across the network
	//create a func. to verify a transaction
	boolean verifyTransaction(BigInteger prime, BigInteger generator){
	    Group g = new Group();
        System.out.println("Safe prime is "+g.prime+" the generator is "+g.generator);
        prime = g.prime;
        generator = g.generator;
        this.publicsSecret = generator.modPow(mySecret,prime);
		return true;
	}
	static String getMd5(String input)
    {
        try {

            // Static getInstance method is called with hashing MD5
            MessageDigest md = MessageDigest.getInstance("MD5");

            // digest() method is called to calculate message digest
            //  of an input digest() return array of byte
            byte[] messageDigest = md.digest(input.getBytes());

            // Convert byte array into signum representation
            BigInteger no = new BigInteger(1, messageDigest);
            // Convert message digest into hex value
            String hashtext = no.toString(16);
            while (hashtext.length() < 32) {
                hashtext = "0" + hashtext;
            }
            return hashtext;
        }

        // For specifying wrong message digest algorithms
        catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }
	//
	//create a func. to do mining

}
