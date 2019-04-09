*************************************************
USING BLOCKCHAIN TO STORE CRIMINAL RECORDS
*************************************************

*************************************************
@AUTHORS
2015B5A70605H Ch Vishal
2015B1A70823H Shubham Paliwal
2015B3A70809H Raj Nandwani
2015B4A70482H Prateek Gupta
2015B4A70575H Ajitesh Singla
*************************************************

************************************************
STRUCTURE AND FUNCTIONALITY
The application has been written in Java 8, so please install Oracle-jdk-8 before proceeding.

The whole application has these class files
Gunah.java - Stores a single crime record along with ID number of the criminal

Transaction.java - Stores a single "Criminal record" or Gunah as a transaction

Block.java - Stores a bunch of Transactions

Blockchain.java - Stores a bunch of Blocks as a blockchain

Group.java - Contains the implementation for creating a new multiplicative group for finding primes and group generators.

User.java - Stores details about a user and performs vital functions like Proof of work (mineBlock) and     Zero Knowledge Proof (verifyTransaction), it also holds the users past transaction entries and a Blockchain.

ReadThread.java - It contains the Multicast implementation for reading the messages

DataGenerator.java - It contains the random criminal record generator, currently unused.

Message.java - Contains format for various messages and flag bits.

Main.java - Contains the core application logic.

The 4 functions that we were asked to implement are
createBlock() - happens automatically inside the User.java class
verifyTransaction() - find this exactly inside User.java class
mineBlock() - find this exactly inside User.java class
viewUser() - find this as printPassbook() inside User.java class

This application works only on a single device on multiple linux terminals. It has been tested for 2 users so far. It uses Multicasting for communication and hence has some synchronization issues which cannot be easily fixed, so if you obtain some issues, don't panic, try again. The known issues are blocks being not added to the blockchain sometimes, the transactions in a block being inconsistent or more than the limit. There may be more issues. 

Currently the messages are not encrypted via RSA, but the procedure is not long so it is not a major achilles heel. The current blockchain is not stored in a persistent storage as well, but that isn't a major issue as it hardly requires 10-20 lines to do so.

We have implemented a random data generator but it has not been integrated, so currently manual input is the only way to create criminal records. For more than 3 users, it can also lead to issues because of Multicasting. Recommended use is therefore 2 "users" or terminals on a single pc interacting with each other.

The current no. of transactions in a block is set to be 3 and the difficulty of mining is set to be 5. You can easily change that by changing a few lines in the User.java file, lines 25 (the first argument is difficulty and the second in the block limit) and 31 (the argument is block limit).

For a quick idea on how the application logic is implemented, check out the huge comment section near line number 492. Obviously many steps have not been implemented, but the game plan is listed there.
*************************************************

*************************************************
USAGE STEPS:
1. The application first needs a single user to begin. To do that, run the bash script "script.sh"
2. You can do that by typing in a linux terminal "bash script.sh"
3. Enter the username, then type "yes" as you are the first user.
4. Then give 'y' as input for manual input.
5. The first user prints "I'm lonely" until another user joins the network
6. To create another user, simply run "java Main" in another instance of linux terminal.
7. Enter "no" as you are not the first user.
8. Go for manual input by typing 'y'.
9. You must see "I'm inducted" on the terminal of the second user, and a prompt saying "Enter the criminal id" in both the terminals.
10. This means that both the users are ready to add crime records.
11. Enter the idno of the criminal (integer) and the crime (any string), one after another to send this transaction for verification.
12. If another transaction is in the pipeline, you will be alerted and after verification you can resume.
13. Otherwise "Waiting for verification" will be shown until the other parties accept the transcation.
14. This can be done until the block limit is reached, after which mining will be performed.
15. Then the block will be added to the chain and will be shared to all.
16. This creates the blockchain.
*************************************************