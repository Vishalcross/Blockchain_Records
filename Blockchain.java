import java.io.Serializable;
import java.util.ArrayList;
class Blockchain implements Serializable{
	private static final long serialVersionUID = 6008132331540391693L;
	ArrayList<Block> blockchain;
	int difficulty;
	public Blockchain(int difficulty, int limit){
		this.difficulty = difficulty;
		blockchain = new ArrayList<>();
		Block b = new Block(limit);
		b.previousHash = "0";
		b.hash = StringUtil.applySha256("GENESIS BLOCK");
		blockchain.add(b);
	}
	String getLastHash(){
		return blockchain.get(blockchain.size()-1).hash;
	}
	void addBlock(Block b){
		blockchain.add(b);
	}
	boolean verifyBlockchain(){
		Block current;
		Block previous;
		String hashTarget = new String(new char[difficulty]).replace('\0', '0');
		for(int i=1;i<blockchain.size();i++){
			current = blockchain.get(i);
			previous = blockchain.get(i-1);
			if(!previous.hash.equals(current.previousHash)){
				System.out.println("Previous hash mismatch");
				return false;
			}
			if(!current.hash.equals(current.calculateHash(current.nonce))){
				System.out.println("Hashes not equal, the blockchain is invalid");
				return false;
			}
			if(!current.hash.substring( 0, difficulty).equals(hashTarget)) {
				System.out.println("This block hasn't been mined");
				return false;
			}
		}
		return true;
	}
}