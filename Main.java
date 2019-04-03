import java.util.*;
import java.math.BigInteger;
class Main {
	public static void main(String[] args) {
		// find a way to make application interactive
		// User user = new User("hello", "world", 0);
		// int prime, generator;
		// user.verifyTransaction(prime, generator);
		// String encryptedMessage = user.getMd5();
		// System.out.println(user.getMd5("hello world"));
		// User verifier = new User("hello1", "world1", 1234);
		// User prover = new User("hello2", "world2", 6789);
		ArrayList<String> channel = new ArrayList<>();
		Group group = new Group();
		String md5hash = "fc5e038d38a57032085441e7fe7010b0";
		BigInteger m = new BigInteger(Integer.toString(Integer.valueOf(md5hash, 16)));
		int p = group.prime;
		int generator = group.generator;
		int privateSecret = 1234;
		int publicsSecret = Group.FastExpModP(generator,privateSecret,p);
		Random random = new Random();
		int r = random.nextInt(100);
		System.out.println("The random number is "+r);

		// m x (mod p), m r (mod p), A r (mod p)
		int alpha = Group.FastExpModP(m,privateSecret,p);
		int beta = Group.FastExpModP(m,r,p);
		int gamma = Group.FastExpModP(generator,r,p);
		System.out.println("Alpha beta gamma are "+alpha+" "+beta+" "+gamma);

		String temp = ""+alpha+beta+gamma;
		String tempMd5 = User.getMd5(temp);
		System.out.println("tempMd5 is "+tempMd5);

		int c = Integer.parseInt(tempMd5,16);
		int s = c*privateSecret + r;
		System.out.println("c and s are "+c+" "+s);
		channel.add(Integer.toString(s));
		channel.add(Integer.toString(alpha));
		channel.add(Integer.toString(beta));
		channel.add(Integer.toString(gamma));
		System.out.println("Digital signature published");

		//verifier
		int alpha1 = Integer.parseInt(channel.get(1));
		int beta1 = Integer.parseInt(channel.get(2));
		int gamma1 = Integer.parseInt(channel.get(3));
		int s1 = Integer.parseInt(channel.get(0));
		String temp2 = ""+alpha1+beta1+gamma1;
		String temp2md5 = User.getMd5(temp2);
		System.out.println("temp2md5 is "+temp2md5);

		//verify
		// A s (mod p) = (A x ) c × (A r )(mod p) and
		// m r (mod p) = (m x ) c × (m r )(mod p)
		int t1 = Group.FastExpModP(generator,s1,p);
		int t2 = Group.FastExpModP(publicsSecret,c,p);
		int t3 = (t2*gamma1)%p;
		if(t1 == t3) System.out.println("It is true");
		else System.out.println("Va fail");
	}

}
