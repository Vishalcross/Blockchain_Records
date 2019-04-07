import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectInputStream;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashMap;

class Message{
    /** Add a user
     * share your public key (generator, p, generator^privateKey (mod p))
     * and username
     * in one class
     */
    private ArrayList<Object> message;
    static final int intro = 1;
    static final int welcome = 2;
    static final int transactionContainer = 3;
    static final int verificationReply = 4;
    static final int storeTransaction = 5;
    static final int hashMined = 6;
    static final int hashVerified = 7;
    static final int newBlockchain = 8;

    Message(){
        this.message = new ArrayList<>();
    }

    byte[] wrapMessage() throws IOException{
        try(ByteArrayOutputStream bos = new ByteArrayOutputStream();
        ObjectOutput out = new ObjectOutputStream(bos)){
            out.writeObject(this.message);
            this.message.clear();
            return bos.toByteArray();
        }
    }

    ArrayList<Object> unWrapMessage(byte[] message) throws IOException, ClassNotFoundException{
        try (ByteArrayInputStream bis = new ByteArrayInputStream(message);
         ObjectInput in = new ObjectInputStream(bis)) {
            return (ArrayList<Object>)in.readObject();
        } 
    }


    byte[] introduceUser(String username, BigInteger generator, BigInteger prime, BigInteger publicKey) throws IOException {
        // ArrayList<Object> this.message = new ArrayList<>();
        this.message.add(intro);
        this.message.add(username);
        this.message.add(generator);
        this.message.add(prime);
        this.message.add(publicKey);
        return wrapMessage();
    }
    //types of messages

    /** Share userTable and current blockchain
     * First user shares the userTable and the current blockchain
     * put them in one message
     */

    byte[] welcomeUser(HashMap<String,BigInteger> userTable, ArrayList<Block> blockchain) throws IOException{
        // ArrayList<Object> message = new ArrayList<>();
        this.message.add(welcome);
        this.message.add(userTable);
        this.message.add(blockchain);
        return wrapMessage();
    } 


    /** Send Transaction
     * Transcation and username (return address)
     * then signature
     * c = hash of (m^privateKey (mod p), m^randomNumber (mod p), generator^RandomNumber (mod p) ) 
     * (s = c*privateKey + randomNumber, m^privateKey (mod p), m^randomNumber (mod p), generator^RandomNumber (mod p))
     * 
    */

    byte[] transactionContainer(Transaction transaction, String username, String s, String alpha, String beta, String gamma) throws IOException {
        // ArrayList<Object> message = new ArrayList<>();
        this.message.add(transactionContainer);
        this.message.add(transaction);
        this.message.add(username);
        this.message.add(s);
        this.message.add(alpha);
        this.message.add(beta);
        this.message.add(gamma);
        return wrapMessage();
        
    }

    /** Transaction reply
     * username (sender address) , VALID
     */
    
    byte[] transactionVerificationReply(boolean validity, String username) throws IOException {
        // ArrayList<Object> message = new ArrayList<>();
        this.message.add(verificationReply);
        this.message.add(validity);
        this.message.add(username);
        return wrapMessage();
    }


    /** Put the transaction
     * PUT, transaction
     */

    byte[] storeTransactionNow(Transaction transaction) throws IOException {
        this.message.add(storeTransaction);
        this.message.add(transaction);
        return wrapMessage();
    }

    /** hash mined
     * send nonce, username (return address)
     */

    byte[] hashMinedInfo(int nonce, String username) throws IOException{
        this.message.add(hashMined);
        this.message.add(nonce);
        this.message.add(username);
        return wrapMessage();
    }

    /** hash verified
     * send YES, username (sender's address)
     */

    byte[] hashVerified(boolean validity, String username) throws IOException{
        this.message.add(hashVerified);
        this.message.add(validity);
        this.message.add(username);
        return wrapMessage();
    }

    /** send the updated blockchain from the user who mined
     * send blockchain with the transactions embedded
     */

    byte[] newBlockchain(Blockchain blockchain) throws IOException{
        this.message.add(newBlockchain);
        this.message.add(blockchain);
        return wrapMessage();
    }
}