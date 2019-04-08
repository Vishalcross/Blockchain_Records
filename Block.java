import java.util.ArrayList;
import java.io.Serializable;
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
	public String calculateHash(String previousHash, int nonce){
		String h = "";
		for(int i=0;i<block.size();i++){
			h += block.get(i).gunah.crime;
		}
		return StringUtil.applySha256(previousHash + Integer.toString(nonce) + h);
	}
}
