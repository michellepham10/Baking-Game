/*
* Project 4.1.5: Escape Room Revisited
* 
* V1.0
* Copyright(c) 2024 PLTW to present. All rights reserved
*/
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;
import java.awt.image.ImageObserver;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;
import javax.imageio.ImageIO;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JOptionPane;


public class GameGUI extends JComponent implements KeyListener
{
  // constants for gameboard confg
  private static final int WIDTH = 1400;
  private static final int HEIGHT = 1036;
  
  // frame and images for gameboard
  private JFrame frame;
  private BufferedImage kitchenBG;
  private BufferedImage bowlImage;
  private BufferedImage cashier;
  private BufferedImage flourImage;
  private BufferedImage strawberriesImage;
  private BufferedImage butterImage;
  private BufferedImage eggsImage;
  private BufferedImage milkImage;
  private BufferedImage sugarImage;
  private BufferedImage chocolateImage;
  private BufferedImage inv1;
  private BufferedImage panImage;
  private BufferedImage filledPanImage;
  private BufferedImage player;
  private BufferedImage openOvenImage;
  private BufferedImage openFridgeImage;
  private BufferedImage greenFairyImage;
  private BufferedImage redFairyImage;
  private BufferedImage doughImage;
  private BufferedImage closedFridgeImage;
  private BufferedImage sourcreamImage;
  
  private int currX = 15; 
  private int currY = 15;
  private int velX;
  private int velY;
  private int npcX;
  private double npcY;
  private String panFiller;
  private Point playerLoc;
  private boolean kitchenScreen;
  private boolean custAtPos;
  private boolean isRedFairy;
  
  private boolean LEFT = false;
  private boolean RIGHT = false;
  private boolean UP = false;
  private boolean DOWN = false;
  private Rectangle flour;
  private Rectangle bowl;
  private Rectangle strawberries;
  private Rectangle butter;
  private Rectangle eggs;
  private Rectangle milk;
  private Rectangle sugar;
  private Rectangle chocolate;
  private Rectangle pan;
  private Rectangle openOven;
  private Rectangle openFridge;
  private Rectangle sourcream;
  private Bowl bowlObj;
  private String inventory;
  private BufferedImage invImage;
  private ArrayList<BufferedImage> kitchenImages;
  private ArrayList<BufferedImage> custImages;
  private ArrayList<Rectangle> kitchenRects;
  private ArrayList<BufferedImage> productImages;
  public Customers c;

  public GameGUI() throws IOException,InterruptedException
  {
    inventory = "";
    createBoard();
    tick();
  }


  public void tick() throws InterruptedException{
    while(true){
      movePlayer(velX,velY);
      Thread.sleep(1);
    }
  }

  
  /**
   * Manage the input from the keybard: arrow keys, wasd keys, p, q, and h keys.
   * Key input is not case sensivite.
   * 
   * @param the key that was pressed
   */
  @Override
  public void keyPressed(KeyEvent e)
  {

    // Q key: quit game if all questions have been answered
    if (e.getKeyCode() == KeyEvent.VK_Q)
    {
      /* your code here */ 
      endGame();
    }
    if(e.getKeyCode()==KeyEvent.VK_L){
      setIngredients();
      
    }
    // H key: help
    if (e.getKeyCode() == KeyEvent.VK_H)
    {
      String msg = "Move player: arrows or WASD keys\n" + 
      "Pickup/place ingredient: e\n" +
      "Mix things in the bowl: f\n"+
      "Receive an order on the screen to the right: c\n"+
      "Clear inventory: l\n"+
      "Quit: q\n" +
      "Help: h\n";
      showMessage(msg);
    }

    if (e.getKeyCode() == KeyEvent.VK_C)
    {
      
      if (c.getOrder() == "" && !kitchenScreen)
      {
        c.generateOrder();
        String order = c.getOrder();
        showMessage(order);
      }
  

      else if (c.getOrder() != "")
      {
        showMessage(c.getOrder());
      }

      else if (c.getOrder() == "" && kitchenScreen)
      {
        showMessage("You do not have an order yet!");
      }
      repaint();
    }
    
    // Arrow and WASD keys: moved down, up, left or right
    if (e.getKeyCode() == KeyEvent.VK_E)
    {
      pickupIngredient();
    }
    else if(e.getKeyCode()==KeyEvent.VK_F){
      if(bowl.contains(playerLoc)&&bowlObj.getIngredients().size()!=0){
        bowlObj.mix();
        if(bowlObj.getIngredients().get(0).contains("batter")){
          inventory=bowlObj.getIngredients().get(0);
          invImage = doughImage;
          bowlObj.clearBowl();
        }
        
      }
    }
    else if(e.getKeyCode()==KeyEvent.VK_T){
      frame.setLocation(20,40);
      recipeBook book = new recipeBook();

    }
    
    else if (e.getKeyCode() == KeyEvent.VK_DOWN || e.getKeyCode() == KeyEvent.VK_S )
    {
      DOWN = true;
      velY=2;
    }
    else if (e.getKeyCode() == KeyEvent.VK_UP || e.getKeyCode() == KeyEvent.VK_W)
    {
      UP = true;
      velY=-2;
    }
    else if (e.getKeyCode() == KeyEvent.VK_LEFT || e.getKeyCode() == KeyEvent.VK_A)
    {
      LEFT = true;
      velX=-2;
    }
    else if (e.getKeyCode() == KeyEvent.VK_RIGHT || e.getKeyCode() == KeyEvent.VK_D)
    {
      RIGHT = true;
      velX=2;
    }
  } 

  /**
   * Manage the key release, checking if the player is at a prize.
   * 
   * @param the key that was pressed
   */
  @Override
  public void keyReleased(KeyEvent e) 
  { 
    if (e.getKeyCode() == KeyEvent.VK_DOWN || e.getKeyCode() == KeyEvent.VK_S )
    {
      DOWN = false;
      if(!UP)velY=0;
      else velY=-2;
    }
    else if (e.getKeyCode() == KeyEvent.VK_UP || e.getKeyCode() == KeyEvent.VK_W)
    {
      UP = false;
      if(!DOWN)velY=0;
      else velY=2;
    }
    else if (e.getKeyCode() == KeyEvent.VK_LEFT || e.getKeyCode() == KeyEvent.VK_A)
    {
      LEFT = false;
      if(!RIGHT)velX=0;
      else velX=2;
    }
    else if (e.getKeyCode() == KeyEvent.VK_RIGHT || e.getKeyCode() == KeyEvent.VK_D)
    {
      RIGHT = false;
      if(!LEFT)velX=0;
      else velX=-2;
    }
  }

  /* override necessary but no action */
  @Override
  public void keyTyped(KeyEvent e) { }

  private void createBoard() throws IOException
  {
    custImages = new ArrayList<BufferedImage>();
    productImages = new ArrayList<BufferedImage>();

    openOvenImage = ImageIO.read(new File("openOven.png"));
    openFridgeImage =ImageIO.read(new File("openFridge.png"));
    closedFridgeImage=ImageIO.read(new File("closedFridge.png"));
    inv1= ImageIO.read(new File("1inventory.png"));
    custImages.add(greenFairyImage = ImageIO.read(new File("greenfairy.png")));
    custImages.add(redFairyImage = ImageIO.read(new File("redfairy.png")));
    doughImage = ImageIO.read(new File("dough.png"));
    filledPanImage = ImageIO.read(new File("filledPan.png"));
    player = ImageIO.read(new File("player.png"));

    productImages.add(ImageIO.read(new File("vanillacake.png")));
    productImages.add(ImageIO.read(new File("straberrycake.png")));
    productImages.add(ImageIO.read(new File("chocolatecake.png")));
    productImages.add(ImageIO.read(new File("chocolatecupcake.png")));
    productImages.add(ImageIO.read(new File("cookie.png")));

    flourImage = ImageIO.read(new File("flour.png"));
    bowlImage = ImageIO.read(new File("bowl.png"));
    strawberriesImage = ImageIO.read(new File("strawberries.png"));
    butterImage = ImageIO.read(new File("butter.png"));
    eggsImage = ImageIO.read(new File("eggs.png"));
    milkImage = ImageIO.read(new File("milk.png"));
    sugarImage = ImageIO.read(new File("sugar.png"));
    chocolateImage = ImageIO.read(new File("chocolate.png"));
    panImage = ImageIO.read(new File("pan.png"));
    sourcreamImage = ImageIO.read(new File("sourcream.png"));
    
    flour = new Rectangle(40,433, (int)flourImage.getWidth(), (int)flourImage.getHeight());
    bowl = new Rectangle(293,493,(int)bowlImage.getWidth(),(int)bowlImage.getHeight());
    strawberries = new Rectangle(901,271,(int)strawberriesImage.getWidth(),(int)strawberriesImage.getHeight());
    butter = new Rectangle(975,327,(int)butterImage.getWidth(),(int)butterImage.getHeight());
    eggs = new Rectangle(915,180,(int)eggsImage.getWidth(),(int)eggsImage.getHeight());
    milk = new Rectangle(898,398,(int)milkImage.getWidth(),(int)milkImage.getHeight());
    sugar = new Rectangle(983,413,(int)sugarImage.getWidth(),(int)sugarImage.getHeight());
    chocolate = new Rectangle(895,505,(int)chocolateImage.getWidth(),(int)chocolateImage.getHeight());
    pan = new Rectangle(595,495,(int)filledPanImage.getWidth(),(int)filledPanImage.getHeight());
    sourcream = new Rectangle(983,510,(int)sourcreamImage.getWidth(),(int)sourcreamImage.getHeight());
    openOven = new Rectangle(570,620,250,250);
    openFridge = new Rectangle(820,50,(int)closedFridgeImage.getWidth(),(int)closedFridgeImage.getHeight()-100); 

    
    npcX = WIDTH+20;
    npcY=0;
    kitchenScreen = true;
    panFiller="";
    custAtPos = false;
    isRedFairy = false;
    bowlObj = new Bowl();
    c = new Customers();
    
    kitchenBG = ImageIO.read(new File("kitchen.png"));
    cashier = ImageIO.read(new File("cashier.png"));
    
    

    setIngredients();
    // save player location
    playerLoc = new Point(currX, currY);
    // create the game frame
    frame = new JFrame();
    frame.setTitle("Baking Game");
    frame.setSize(WIDTH, HEIGHT);
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    frame.add(this);
    frame.setLocationRelativeTo(null);
    
    frame.setVisible(true);
    frame.setResizable(false); 
    frame.addKeyListener(this);
    showMessage("Welcome to the Baking Game! Press h to learn how to play.");
  }
  public void setIngredients()
  {
    inventory = "";
    invImage=null;

    kitchenImages = new ArrayList<BufferedImage>();
    kitchenRects = new ArrayList<Rectangle>();
    bowlObj.clearBowl();

    kitchenImages.add(flourImage);
    kitchenImages.add(bowlImage);
    kitchenImages.add(strawberriesImage);
    kitchenImages.add(butterImage);
    kitchenImages.add(eggsImage);
    kitchenImages.add(milkImage);
    kitchenImages.add(sugarImage);
    kitchenImages.add(chocolateImage);
    kitchenImages.add(panImage);
    kitchenImages.add(sourcreamImage);
    
    kitchenRects.add(flour);
    kitchenRects.add(bowl);
    kitchenRects.add(strawberries);
    kitchenRects.add(butter);
    kitchenRects.add(eggs);
    kitchenRects.add(milk);
    kitchenRects.add(sugar);
    kitchenRects.add(chocolate);
    kitchenRects.add(pan);
    kitchenRects.add(sourcream);
    kitchenRects.add(openOven);
    kitchenRects.add(openFridge);     
  }
  /**
   * Increment/decrement the player location by the amount designated.
   * 
   * @param incrx amount to move player in x direction
   * @param incry amount to move player in y direciton
   * 
   * @return penaly for hitting a wall or trying to go off the grid, goodMove otherwise
   */
  private void movePlayer(int incrx, int incry)
  {
    // check if off grid horizontally and vertically
    if(currX+incrx > WIDTH-60&&kitchenScreen){
      kitchenScreen=false;
      currX=60;
    }
    else if(currX+incrx<0&&!kitchenScreen){
      kitchenScreen=true;
      currX=(WIDTH-120);
    }
    else currX += incrx;
    if(!((currY+incry < 0 || currY+incry > HEIGHT-60)))currY += incry;
    repaint();
  }
  
  /**
   * Displays a dialog with a simple message and an OK button
   * 
   * @param str the message to show
   */
  private void showMessage(String str)
  {
    JOptionPane.showMessageDialog(frame,str );
  }

private void pickupIngredient()
  {
    if(kitchenScreen){
      if(inventory.equals("")){
        if (flour.contains(playerLoc)&&kitchenRects.contains(flour)){
          invImage=flourImage;
          kitchenImages.remove(flourImage);
          kitchenRects.remove(flour);
          inventory="flour";
        }
        else if (strawberries.contains(playerLoc)&&kitchenRects.contains(strawberries)){
          invImage=strawberriesImage;
          kitchenImages.remove(strawberriesImage);
          kitchenRects.remove(strawberries);
          inventory="strawberries";
          
        }

        else if (butter.contains(playerLoc)&&kitchenRects.contains(butter)){
          invImage=butterImage;
          kitchenImages.remove(butterImage);
          kitchenRects.remove(butter);
          inventory="butter";
        }

        else if (eggs.contains(playerLoc)&&kitchenRects.contains(eggs)){
          invImage=eggsImage;
          kitchenImages.remove(eggsImage);
          kitchenRects.remove(eggs);
          inventory="eggs";
        }
        else if (milk.contains(playerLoc)&&kitchenRects.contains(milk)){
          invImage=milkImage;
          kitchenImages.remove(milkImage);
          kitchenRects.remove(milk);
          inventory="milk";
        }
        else if (chocolate.contains(playerLoc)&&kitchenRects.contains(chocolate)){
          invImage=chocolateImage;
          kitchenImages.remove(chocolateImage);
          kitchenRects.remove(chocolate);
          inventory="chocolate";
        }
        else if (sugar.contains(playerLoc)&&kitchenRects.contains(sugar)){
          invImage=sugarImage;
          kitchenImages.remove(sugarImage);
          kitchenRects.remove(sugar);
          inventory="sugar";
        }
        else if(sourcream.contains(playerLoc)&&kitchenRects.contains(sourcream)){
          invImage=sourcreamImage;
          kitchenImages.remove(sourcreamImage);
          kitchenRects.remove(sourcream);
          inventory="sourcream";
        }
        else if(pan.contains(playerLoc)&&!panFiller.equals("")){
          invImage=filledPanImage;
          kitchenImages.remove(filledPanImage);
          kitchenRects.remove(pan);
          inventory="pan";
        }
      }
      else{
        if(pan.contains(playerLoc)&&inventory.contains("batter")){
          panFiller=inventory;
          inventory="";
          invImage=null;
          kitchenImages.set(kitchenImages.indexOf(panImage),filledPanImage);
        }
        else if(openOven.contains(playerLoc)&&inventory.contains("pan")){
          invImage=productImages.get(bowlObj.findRecipeIdx(panFiller));
          inventory=panFiller.substring(0,panFiller.indexOf(" batter"));
          panFiller="";
          kitchenImages.add(panImage);
          kitchenRects.add(kitchenImages.indexOf(panImage),pan);
        }
        else if(bowl.contains(playerLoc)){
          if(!inventory.contains("batter")&&!inventory.contains("tray")){
            bowlObj.addIngredient(inventory);
            inventory="";
            invImage=null;
          }
          
        }
      }
    }
    else{
      System.out.println(inventory+","+c.getProduct());
      if(inventory.equals(c.getProduct())){
      invImage=null;
      inventory="";
      showMessage(c.generateReply());
      c.nullifyOrder();
      custAtPos = false;
    }}
    
  }

  /**
    * End the game
  */
  private void endGame() 
  {

    setVisible(false);
    frame.dispose();
  }


  /* 
   * Manage board elements with graphics buffer g.
   * For internal use - do not call directly, use repaint instead.
   */
  public void paintComponent(Graphics g)
  {
    super.paintComponent(g);
    Graphics2D g2 = (Graphics2D)g;
    if(kitchenScreen){
      g.drawImage(kitchenBG, 0, 0, null);
      g.drawImage(inv1, 1220, 0, null);
      if(openOven.contains(playerLoc))g.drawImage(openOvenImage, 0, 0, null);
      if(openFridge.contains(playerLoc)){
        g.drawImage(openFridgeImage, 0, 0, null);
      }
      for(int i = 0;i< kitchenImages.size();i++){
        g.drawImage(kitchenImages.get(i),(int)kitchenRects.get(i).getX()+37,(int)kitchenRects.get(i).getY()+37,null);
      }
      if(!openFridge.contains(playerLoc)){
        g.drawImage(closedFridgeImage, 839, 147, null);
      }
      try{g.drawImage(invImage,WIDTH-95-invImage.getWidth()/2, 70-invImage.getHeight()/2,null);}
      catch(Exception e){}
    }else{
      g.drawImage(cashier,0,0,null);
      if(!custAtPos){
        npcY+=0.015;
        npcX--;
        if(npcY==360)npcY=0;
        if(npcX==350)custAtPos=true;
        else if(npcX==-20){
          isRedFairy=!isRedFairy;
          npcX=WIDTH+20;
        }
      }
      if(isRedFairy)g.drawImage(redFairyImage, npcX, 370+(int)(25*Math.sin(npcY)), null);
      else g.drawImage(greenFairyImage, npcX, 370+(int)(25*Math.sin(npcY)), null);
    }
    
    g.drawImage(player, currX, currY, 70,70, null);
    playerLoc.setLocation(currX, currY);
  }

}
