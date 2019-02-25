import java.util.*;
import java.io.UnsupportedEncodingException;
import java.util.Random;

public class Block {

   private int index; // the index of the block in the list
   private java.sql.Timestamp timestamp; // time at which transaction has been processed
   private Transaction transaction; // the transaction object
   private String nonce; // random string (for proof of work)
   private String previousHash; // previous hash (set to "" in first block)
   private String hash; // hash of the block (hash of string obtained from previous variables via toString() method)
   
   
   //Constructor
  /* public Block(int index,java.sql.Timestamp timestamp,Transaction transaction, String nonce, String previousHash)throws UnsupportedEncodingException{ 
   
      this.index=index;
      this.nonce = nonce;
      this.previousHash=previousHash;
      this.transaction=transaction;
      this.timestamp = timestamp;
      
      hash= Sha1.hash(index + timestamp.toString() + transaction.toString() + nonce+ previousHash);
      
   }*/
   
   //Constructor
   public Block(int index,java.sql.Timestamp timestamp,Transaction transaction, String nonce, String previousHash, String hash)throws UnsupportedEncodingException{ 
   
      this.index= index;
      this.nonce = nonce;
      this.previousHash=previousHash;
      this.transaction=transaction;
      this.timestamp = timestamp;
      this.hash= hash;
            
   }
   
   //constructor without nonce
   public Block(int index,java.sql.Timestamp timestamp,Transaction transaction,String previousHash, String hash)throws UnsupportedEncodingException{ 
   
      this.index= index;
      this.previousHash=previousHash;
      this.transaction=transaction;
      this.timestamp = timestamp;
      this.hash= hash;
      
            
   }

   public String reneNonce()throws UnsupportedEncodingException{
       
       Random rnd = new Random();
       char rndchar;
       String select;
       //select randomly one of the char in the String below 
       select = "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ?/.,!@#$%^&*()_+-=";
       String nonce="";
       boolean flag=true;
       int counter=0; //counts how many trials of hash is generated
       int rndLength=rnd.nextInt(15+1);//random length b/w 1 to 15
       
       while (flag){
            counter++;
            this.hash= Sha1.hash(timestamp.toString() + ":" + transaction.toString()+ "." + nonce+ previousHash);
            if(! (hash.substring(0,5).equals("00000"))){//if hash isnt 00000, then we keep changing nonce
            //another way for this if statment is to use hash.startWith("00000")
            
            
               nonce="";//initionlize nonce as empty
               rndLength=rnd.nextInt(15+1);
               for(int i=0; i<rndLength; i++){//length of a random length b/w 1 to 15
            
                  rndchar = select.charAt(rnd.nextInt(select.length()));
                  nonce=nonce+rndchar;
                  
               }
               
               
            }else{
               flag=false;
            } 
      } 
      //sop for report
      /*System.out.println("# of hash trials:");
      System.out.println(counter);
      System.out.println("hash:");
      System.out.println(hash);*/
      
      return nonce;
   }
   
   
   //getter method for index
   public int getIndex(){
      return index;
   }
   //getter method for transcation
   public Transaction getTransaction(){
      return transaction;
   }
   //getter method for nonce
   public String getNonce(){
      return nonce;
   }
   //getter method for previoushash
   public String getPreviousHash(){
   
      return previousHash;
   }
   
   //getter method for timestamp
   public Long getTimestamp(){
   
      return timestamp.getTime();
   }
   //getter method for hash
   public String getHash(){
      return hash;
   
   }
   
   public String toString() {
      return timestamp.toString() + ":" + transaction.toString()+ "." + nonce+ previousHash;
   }
}