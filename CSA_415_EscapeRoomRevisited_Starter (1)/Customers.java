public class Customers{
  //options can be edited however you want.
  private final String[] greetings = {"Good morning, ","...","Hi, ","Heyyy, ","Sup, ","Hello, ",""};
  private final String[] requests = {"I'd like ","could I get ","give me ","... umm, ",""};
  private final String[] cakes = {"vanilla ","strawberry ","chocolate "};
  //private final String[] frostings = {"vanilla ","chocolate "};
  private final String[] endings = {" please",".."," maybe",", thanks"," or whatever",""};
  private final String[] repliesP1 = {"Thank you so much!", "Thank you!", "Thanks.", ""};
  private final String[] repliesP2 = {"Goodbye!", "Bye!", "Bye.", "See you next time!", ""};
  private String order="";
  private String words="";
  private String reply="";
  
  public Customers(){}//will be implemented with the GUI
  
  public String generateOrder()
  {
    order=cakes[(int)(Math.random()*cakes.length)]+"cake";//+" with "
    //frostings[(int)(Math.random()*frostings.length)]+"frosting";
    
    words=greetings[(int)(Math.random()*greetings.length)]+
    requests[(int)(Math.random()*requests.length)]+"a "+order+
    endings[(int)(Math.random()*endings.length)]+".";
    
    //words are saved just in case idk

    return words;
  }
  public String getCake(){
    return order;
  }
  public String generateReply()
  {
    reply=repliesP1[(int)(Math.random()*repliesP1.length)]+" "+
    repliesP2[(int)(Math.random()*repliesP2.length)]+".";

    return reply;
  }

  public void nullifyOrder()
  {
    words = "";
  }

  public String getOrder()
  {
    return words;
  }
//   public static void main(String[] args)
//   {
//     Customers c = new Customers();
//     System.out.println(c.generateOrder());
//   }
}
