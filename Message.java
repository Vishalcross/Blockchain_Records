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
            System.out.println(this.message);
            out.writeObject((Object)this.message);
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
    
    /** Add a user
     * share your public key (generator, p, generator^privateKey (mod p))
     * and username
     * in one class
     */

    byte[] introduceUser(String sender, BigInteger generator, BigInteger prime, BigInteger publicKey) throws IOException {
        // ArrayList<Object> this.message = new ArrayList<>();
        this.message.add(intro);
        this.message.add(sender);
        this.message.add(generator);
        this.message.add(prime);
        this.message.add(publicKey);
        return wrapMessage();
    }

    /** Share userTable and current blockchain
     * First user shares the userTable and the current blockchain
     * put them in one message
     */

    byte[] welcomeUser(String reciever, HashMap<String,ArrayList<BigInteger>> userTable, Blockchain blockchain, Block currentBuffer) throws IOException{
        // ArrayList<Object> message = new ArrayList<>();
        this.message.add(welcome);
        // this.message.add(sender);
        this.message.add(reciever);
        this.message.add(userTable);
        this.message.add(blockchain);
        this.message.add(currentBuffer);
        return wrapMessage();
    } 


    /** Send Transaction
     * Transcation and username (return address)
     * then signature
     * c = hash of (m^privateKey (mod p), m^randomNumber (mod p), generator^RandomNumber (mod p) ) 
     * (s = c*privateKey + randomNumber, m^privateKey (mod p), m^randomNumber (mod p), generator^RandomNumber (mod p))
     * 
    */

    byte[] transactionContainer(String sender, Transaction transaction, BigInteger s, BigInteger alpha, BigInteger beta, BigInteger gamma) throws IOException {
        // ArrayList<Object> message = new ArrayList<>();
        this.message.add(transactionContainer);
        this.message.add(sender);
        this.message.add(transaction);
        this.message.add(s);
        this.message.add(alpha);
        this.message.add(beta);
        this.message.add(gamma);
        return wrapMessage();
        
    }

    /** Transaction reply
     * username (sender address) , VALID
    */
    
    byte[] transactionVerificationReply(String sender, String reciever, boolean validity) throws IOException {
        // ArrayList<Object> message = new ArrayList<>();
        this.message.add(verificationReply);
        this.message.add(sender);
        this.message.add(reciever);
        this.message.add(validity);
        return wrapMessage();
    }


    /** Put the transaction
     * PUT, transaction
     */

    byte[] storeTransactionNow(String sender, Transaction transaction) throws IOException {
        this.message.add(storeTransaction);
        this.message.add(sender);
        this.message.add(transaction);
        return wrapMessage();
    }

    /** hash mined
     * send nonce, username (return address)
     */

    byte[] hashMinedInfo(String sender, int nonce) throws IOException{
        this.message.add(hashMined);
        this.message.add(sender);
        this.message.add(nonce);
        return wrapMessage();
    }

    /** hash verified
     * send YES, username (sender's address)
     */

    byte[] hashVerified(String sender, String reciever, boolean validity, int nonce) throws IOException{
        this.message.add(hashVerified);
        this.message.add(sender);
        this.message.add(reciever);
        this.message.add(validity);
        this.message.add(nonce);
        return wrapMessage();
    }

    /** send the updated blockchain from the user who mined
     * send blockchain with the transactions embedded
     */

    byte[] newBlockchain(String sender, Blockchain blockchain) throws IOException{
        this.message.add(newBlockchain);
        this.message.add(sender);
        this.message.add(blockchain);
        return wrapMessage();
    }
}