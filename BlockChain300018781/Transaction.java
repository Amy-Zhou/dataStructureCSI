public class Transaction {
   private String sender;//username of person giving money
   private String receiver;//username of person receiving money
   private int amount;//number of bitcoins invovled
   
   
   //Constructor
   public Transaction(String sender, String receiver, int amount){
      this.sender=sender;
      this.receiver=receiver;
      this.amount= amount;
   
   }
   
   //getter method for sender
   public String getSender(){
      return sender;
   }
   //getter method for receiver
   public String getReceiver(){
      return receiver;
   }
   //getter method for amount
   public int getAmount(){
      return amount;
   }
   //tostring method
   public String toString() {
      return sender + ":" + receiver + "=" + amount;
   }
}