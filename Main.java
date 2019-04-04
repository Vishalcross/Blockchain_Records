import java.util.*;
import java.math.BigInteger;
class Main {
	public static void main(String[] args) {

		// Group g = new Group();


		// find a way to make application interactive
		// User user = new User("hello", "world", 0);
		// BigInteger prime, generator;
		// user.verifyTransaction(prime, generator);
		// String encryptedMessage = user.getMd5();
		// System.out.println(user.getMd5("hello world"));
		User verifier = new User("hello1", "world1", new BigInteger("1234") );
		User prover = new User("hello2", "world2", new BigInteger("6789") );
		ArrayList<String> channel = new ArrayList<>();
		Group group = new Group();
		String md5hash = "fc5e038d38a57032085441e7fe7010b0";
		BigInteger m = new BigInteger(md5hash, 16);
		BigInteger p = new BigInteger("937")/*group.prime*/;
		BigInteger generator = new BigInteger("5")/*group.generator*/;

		BigInteger privateSecret = new BigInteger("1234");
		BigInteger publicsSecret = generator.modPow(privateSecret,p);
		Random random = new Random();
		BigInteger r = new BigInteger(""+random.nextInt(100));

		System.out.println("The random number is "+r);

		// m x (mod p), m r (mod p), A r (mod p)
		BigInteger alpha = m.modPow(privateSecret,p);
		BigInteger beta = m.modPow(r,p);
		BigInteger gamma = generator.modPow(r,p);
		System.out.println("Alpha beta gamma are "+alpha+" "+beta+" "+gamma);

		String temp = ""+alpha.toString()+beta.toString()+gamma.toString();
		String tempMd5 = User.getMd5(temp);
		System.out.println("tempMd5 is "+tempMd5);

		BigInteger c = new BigInteger(tempMd5,16);
		System.out.println("c is "+c);
		c = c.multiply(privateSecret);
		BigInteger s = c.add(r);
		System.out.println("s is "+s);
		channel.add(s.toString());
		channel.add(alpha.toString());
		channel.add(beta.toString());
		channel.add(gamma.toString());
		System.out.println("Digital signature published");

		//verifier
		BigInteger s1 = new BigInteger(channel.get(0));
		BigInteger alpha1 = new BigInteger(channel.get(1));
		BigInteger beta1 = new BigInteger(channel.get(2));
		BigInteger gamma1 = new BigInteger(channel.get(3));
		String temp2 = ""+alpha1.toString()+beta1.toString()+gamma1.toString();
		String temp2md5 = User.getMd5(temp2);
		System.out.println("temp2md5 is "+temp2md5);
		BigInteger c1 = new BigInteger(temp2md5,16);

		//verify
		// A s (mod p) = (A x ) c × (A r )(mod p) and
		// m r (mod p) = (m x ) c × (m r )(mod p)

		BigInteger t1 = generator.modPow(s1,p);
		// BigInteger t2 = publicsSecret.pow(c);
		BigInteger t2 = publicsSecret.modPow(c1,p);
		t2 = t2.multiply(gamma1);
		BigInteger t3 = t2.mod(p);

		System.out.println("Debug***************************");
		System.out.println("m = "+m+" r = "+r+" ");
		System.out.println("x = "+privateSecret+" c1 = "+c1);
		// System.out.println(gamma+" "+gamma1);
		System.out.println("Debug***************************");

		BigInteger t4 = m.modPow(s1,p);
		BigInteger t5 = alpha1.modPow(c1,p);
		BigInteger t6 = t5.multiply(beta1).mod(p);
		System.out.println(t4);
		System.out.println(t5);
		System.out.println(t6);

		if(t1.equals(t3) ) System.out.println("It is true");
		// else System.out.println("Va fail");
	}

}
