import java.util.ArrayList;
import java.util.Date;
import java.security.MessageDigest;
class Block{
	ArrayList<Transaction> block;
	String previousHash;
	String hash;
	int limit;
	int nonce;
	public Block(int limit){
		this.limit = limit;
		block = new ArrayList<>();
	}
	public void addTransacion(Transaction transaction){
		block.add(transaction);
	}
	public String calculateHash(int nonce){
		String h = "";
		this.nonce = nonce;
		for(int i=0;i<block.size();i++){
			h += block.get(i).gunah.crime;
		}
		return StringUtil.applySha256(previousHash + Long.toString(new Date().getTime()) + Integer.toString(nonce) + h);
	}
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