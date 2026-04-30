public class Customers{
  //options can be edited however you want.
  private final String[] greetings = {"Good morning, ","...","Hi, ","Heyyy, ","Sup, ","Hello, ",""};
  private final String[] requests = {"I'd like ","could I get ","give me ","... umm, ",""};
  private final String[] cakes = {"vanilla ","chocolate "};
  private final String[] frostings = {"vanilla ","chocolate "};
  private final String[] endings = {" please",".."," maybe",", thanks"," or whatever",""};
  private String order="";
  private String words="";
  
  public Customers(){}//will be implemented with the GUI
  public String generateOrder(){
    String order=cakes[(int)(Math.random()*cakes.length)]+"cake with "+
    frostings[(int)(Math.random()*frostings.length)]+"frosting";
    
    String words=greetings[(int)(Math.random()*greetings.length)]+
    requests[(int)(Math.random()*requests.length)]+"a "+order+
    endings[(int)(Math.random()*endings.length)]+".";
    
    //words are saved just in case idk
    return words;
  }
  public String getOrder(){
    return order;
  }
  public static void main(String[] args){
    Customers c = new Customers();
    System.out.println(c.generateOrder());
  }
}