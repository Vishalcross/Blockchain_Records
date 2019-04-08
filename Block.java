import java.util.ArrayList;
import java.util.Date;
import java.io.Serializable;
import java.security.MessageDigest;
class Block implements Serializable{
	private static final long serialVersionUID = 4096183216294081308L;
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
