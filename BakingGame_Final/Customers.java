public class Customers{
  //options can be edited however you want.
  private final String[] greetings = {"Good morning, ","...","Hi, ","Heyyy, ","Sup, ","Hello, ",""};
  private final String[] requests = {"I'd like ","could I get ","give me ","... umm, ",""};
  private final String[] products = {"vanilla cake","strawberry cake","chocolate cake","chocolate cupcake","cookie"};
  private final String[] frostings = {"vanilla ","strawberry ","chocolate "};
  private final String[] endings = {" please",".."," maybe",", thanks"," or whatever",""};
  private final String[] Goodreplies = {"Thank you so much!", "Oh wow!", "This is my new favorite thing.", "I love you.",
                                        "This is delicious!","I'd tip if money existed in this game.","Incredible!",
                                        "Is this what a dream tastes like?","YAHOO","OMG!","This is the best thing ever!"};
  private final String[] Badreplies = {"Eugh.", "Is this edible?", "What is this, cement?", "... You should concider changing professions.",
                                        "... I don't have words for this...","Why are you tormenting me like this?",
                                        "Are you trying to poison me?","I've never tasted anything worse."};
  private final String[] repliesP2 = {"Goodbye!", "Bye!", "Bye.", "See you next time!", "","","",""};
  private String order="";
  private String words="";
  private String reply="";
  
  public Customers(){}
  
  public String generateOrder()
  {
    int rand = (int)(Math.random()*products.length);
    order=products[rand];
    if(rand<3)order+=" with "+frostings[(int)(Math.random()*frostings.length)]+"frosting";
    words=greetings[(int)(Math.random()*greetings.length)]+
    requests[(int)(Math.random()*requests.length)]+"a "+order+
    endings[(int)(Math.random()*endings.length)]+".";
    

    return words;
  }
  public String getProduct(){
    return order;
  }
  public String generateReply(int score)
  {
    if(score>25)reply=Goodreplies[(int)(Math.random()*Goodreplies.length)]+" ";
    else if(score<10)reply=Badreplies[(int)(Math.random()*Badreplies.length)]+" ";
    reply+=repliesP2[(int)(Math.random()*repliesP2.length)];

    return reply;
  }

  public void nullifyOrder()
  {
    order="";
    words = "";
  }

  public String getOrder()
  {
    return words;
  }
  
}
