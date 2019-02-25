import java.util.Scanner;
import java.util.ArrayList;
import java.io.FileNotFoundException;
import java.util.Random;
import java.sql.Timestamp;
import java.io.FileOutputStream;
import java.io.UnsupportedEncodingException;
import java.io.PrintWriter;
import java.io.File;
import java.io.IOException;


public class BlockChain{

   private static ArrayList <Block> blocks = new ArrayList<Block>(); 

   //constructor
   public BlockChain(ArrayList <Block> blocks){ 
      this.blocks=blocks;
      
      
   
   }
   //getter method for the arraylist
   public ArrayList<Block> getBlockChain (){
   
      return blocks;
   }

   
   //reading a fille with a filename given
   public static BlockChain fromFile(String filename) throws UnsupportedEncodingException {
      
      //initialize everything to 0 or null
      int index=0;
      long init = 0L;
      java.sql.Timestamp timestamp= new Timestamp(init);
      String sender="";
      String receiver="";
      String nonce="";
      String hash="";
      int amount=0;
      int counter=0;//counter for reading each line
      Transaction trans;
      Block bl;
      ArrayList <Block> chains = new ArrayList<Block>();
      
       try{
         
         Scanner sc = new Scanner(new File (filename));
         while(sc.hasNextLine()){//if the scanner is reading and if there's a next line, when while continues
            if(counter % 7 == 0 ){//there are 7 lines, every 7 lines, we look at a new block
               index=Integer.parseInt(sc.nextLine());//first line is index
            
            }else if (counter % 7 ==1){ //2nd line is timestamp, read it as string and then parse it into long
               Long parseVar = Long.parseLong(sc.nextLine());
               timestamp = new Timestamp (parseVar);
            
            }else if (counter % 7 ==2){
               sender = sc.nextLine();//3rd, sender as string
            
            }else if (counter % 7 ==3){//4th, reciever as string
               receiver = sc.nextLine();
            
            }else if (counter % 7 ==4){//5th, amount as integer
               amount= Integer.parseInt(sc.nextLine());
            
            
            }else if (counter % 7 ==5){//nonce as string
               nonce = sc.nextLine();
            
            }else if (counter % 7 ==6){//hash as string
               hash = sc.nextLine();//when reach this if statment, it means that a whole block is read and should create and add into the chain
               
               trans = new Transaction (sender, receiver, amount);
               if (index == 0){//first block 's prev hash is 00000
                  bl= new Block (index,timestamp,trans,nonce,"00000",hash);
                  chains.add(bl);
                  
               }
               else{
                  bl= new Block (index,timestamp,trans,nonce,chains.get(index-1).getHash(),hash);
                  chains.add(bl);

               }
               
            }
            counter++;//increment counter so that its reading next line
           
         
         }
         
       
       
       sc.close();
       }catch(FileNotFoundException e){
         System.out.println("Can't find file.");
       
       }
       return new BlockChain(chains);
      
   
   }
   
      //check each blockchain if their previous is == to next hash
   public boolean validateBlockchain(){
   
      for (int i =1;i<blocks.size() ;i++){
         if (!blocks.get(i).getPreviousHash().equals (blocks.get(i-1).getHash())){//check prevhash == hash
            return false;
         }
         if(!(blocks.get(i).getIndex() ==i)){//check index
            
            return false;
         }
         try{//check hash, hash is invalid when the shal hash is diff than the hash reading 
          
            
            String temp= Sha1.hash(blocks.get(i).toString());//generated hash
           /* System.out.println(temp + "   this is temp");
            System.out.println(blocks.get(i).getHash());
            System.out.println(!(blocks.get(i).getHash().equals(temp)));
            System.out.println(blocks.get(i).toString());*/
            if(!(blocks.get(i).getHash().equals(temp))){
               return false;
            }        
         }catch(UnsupportedEncodingException e){
            System.out.println("error on encoding");
         
         }
         if(blocks.get(i).getTransaction().getAmount() > getBalance(blocks.get(i).getTransaction().getSender(),i)){//check amount (one person can't spend more than he has)
            //amount has spent should be smaller than sender's balance
            
            
            return false;
         
         } 
      }
      return true;
   }
   
   //get the balance
  public int getBalance(String username){
   
      int amount = 0 ;
   
      for (int i =0;i<blocks.size() ;i++){//for loop go thru every block and check if its sender or reciver
         
         Transaction a = blocks.get(i).getTransaction();//get transaction
         if(blocks.get(i).getTransaction().getSender() .equals (username)){//if the user is sender, deduct money
             amount = amount- a.getAmount();
               
         }
         if (blocks.get(i).getTransaction().getReceiver() .equals (username)){//if the user is receiver, add money
            amount=amount+a.getAmount();
         
         }      
      }
      if (amount < 0)//make sure no negative values for balance
         amount=amount * -1 ;
      
      return amount;
   
   
   }
   
   
    public int getBalance(String username, int size){//size is up to which block we are looking at
   
      int amount = 0 ;
   
      if(size == 0){//assume the person havn't spend anything
         return blocks.get(0).getTransaction().getAmount();
      }
   
      for (int i =0;i<size;i++){//for loop go thru every block and check if its sender or reciver
         
         Transaction a = blocks.get(i).getTransaction();//get transaction
         if(blocks.get(i).getTransaction().getSender() .equals (username)){//if the user is sender, deduct money
             amount = amount- a.getAmount();
               
         }
         if (blocks.get(i).getTransaction().getReceiver() .equals (username)){//if the user is receiver, add money
            amount=amount+a.getAmount();
         
         }      
      }
      if (amount < 0)//make sure no negative values for balance
         amount=amount * -1 ;
      
      return amount;
   
   
   }


   
   //add method with a block given
   public void add(Block block){
      /*if(validateBlockchain()){
      
         try{
            block.setNonce();
         }catch(UnsupportedEncodingException e){
         
            System.out.println("error encoding");
         }
      
      
      }*/
      blocks.add(block);
      
   }
   
         
   
   //write a txt file
   public void toFile(String fileName){
      
      try{
      
         File afile = new File (fileName);//create a new file with the given file name
         afile.createNewFile(); 
         PrintWriter pw = new PrintWriter (afile);//use PrintWriter to write into the file
         //for loop to write each line
         for(int i = 0 ;i<blocks.size(); i++){
            pw.println(blocks.get(i).getIndex());
            pw.println(blocks.get(i).getTimestamp());
            pw.println(blocks.get(i).getTransaction().getSender());
            pw.println(blocks.get(i).getTransaction().getReceiver());
            pw.println(blocks.get(i).getTransaction().getAmount());
            pw.println(blocks.get(i).getNonce());
            pw.println(blocks.get(i).getHash());
            
         
         }
         
         pw.close();
         
         
      
      }catch (IOException e){
         System.out.println("error output to a file");
      
      }
   }
   
   public static void main(String[]args)throws UnsupportedEncodingException, FileNotFoundException{
   
      
    //String filename = "bitcoin.txt";
      
      /* System.out.println(blocks.getBlockChain().get(0).toString());
      System.out.println(blocks.getBlockChain().get(1).toString());
      System.out.println(blocks.getBlockChain().get(2).toString());
      
      above is to test if block is read correctly
      
      below is to test if writing is correctly
           
       
      blocks.toFile("asd.txt");
      */
      
      //is the blockchain valide
      //System.out.println(blocks.validateBlockchain());
            

      
      int stopselection; //this is for when user choose to stop adding blocks
      java.sql.Timestamp timestamp=new Timestamp(System.currentTimeMillis());
      Block bc;
      Scanner sc = new Scanner(System.in);
      int index=0;
      String sender="";  
      String reciver="";
      int amount=0;
      Transaction transcation;
      String hash="";
      String newNonce="";
      Block blc;
      boolean checkvalid;
      int onlyValidate;
      
      
      System.out.println("Enter a filename for reading");
      String filename=sc.next();//promt user for filename
      BlockChain blocks = BlockChain.fromFile(filename);//read file
      
      //see if the user only wants to validate
      System.out.println("Enter 1 to only validate, 0 to readfile and add more block");
      onlyValidate=sc.nextInt();
      if(onlyValidate==1){//validate
      
            checkvalid=blocks.validateBlockchain();
            if (checkvalid){
               System.out.println("The block chain is valid");
            }
            else{
               System.out.println("The block chain is NOT valid");  
            }

      }else if (onlyValidate==0){//add more block
      
      
         do{//do while loop, add at least one, and then ask user if they want to continue or not
            //promt user input sender, reciver, amount, then get a new transaction 
            System.out.println("Enter a sender name");
            sender=sc.next();
      
            System.out.println("Enter a reciver name");
            reciver = sc.next();
      
            System.out.println("Enter amount");
            amount = sc.nextInt();
      
            transcation = new Transaction(sender,reciver,amount);
         
            //initilize a new block 
            bc = new Block(blocks.getBlockChain().size() , timestamp, transcation, blocks.getBlockChain().get(blocks.getBlockChain().size()-1 ).getHash()  , hash  );
            newNonce= bc.reneNonce();//get the new nonce
            hash=bc.getHash();//get the new hash
            System.out.println("nonce is : "+ newNonce);
         
            //the new block thats created from user input
            blc=new Block(blocks.getBlockChain().size() , timestamp, transcation,newNonce, blocks.getBlockChain().get(blocks.getBlockChain().size()-1 ).getHash()  , hash  );
         
         
            //add that block into blockchain
            blocks.add(blc);
         
         
            checkvalid=blocks.validateBlockchain();
            if (checkvalid){
               System.out.println("The block chain is valid so far");
            }
            else{
               System.out.println("Warning*******************************************************");
               System.out.println("The block chain is NOT valid, make changes and comeback again!");  
            }
            //enter 1 to continue add block, 0 to stop 
            System.out.println();
         
            System.out.println("Enter 1 to continue, 0 to stop");
            stopselection=sc.nextInt();
         }while (stopselection==1);
                     
     }   
     //write the file
         
      System.out.println("Enter a filename to write to:");
      filename=sc.next();      
      blocks.toFile(filename);  

     }
}


   
   
