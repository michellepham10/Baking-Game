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
  private Image kitchenBG;
  private Image bowlImage;
  private Image cashier;
  private Image flourImage;
  private Image strawberriesImage;
  private Image butterImage;
  private Image eggsImage;
  private Image milkImage;
  private Image sugarImage;
  private Image chocolateImage;
  private Image inv5;
  private Image inv6;
  private Image currentInvBar;
  private Image panImage;
  private Image filledPanImage;
  private Image player;
  private Image openOvenImage;
  private Image openFridgeImage;
  private Image greenFairyImage;
  private Image redFairyImage;
  private Image doughImage;
  private Image vanillaCakeImage;
  private Image chocolateCakeImage;
  private Image strawberryCakeImage;
  
  private int currX = 15; 
  private int currY = 15;
  private int velX;
  private int velY;
  private int npcX;
  private double npcY;
  private String panFiller;
  private Point playerLoc;
  private boolean kitchenScreen;
  private boolean isInv5;
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
  private Bowl bowlObj;
  private ArrayList<String> inventory;
  public Customers c;

  public GameGUI() throws IOException,InterruptedException
  {
    inventory = new ArrayList<String>();
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
    if(e.getKeyCode()==KeyEvent.VK_C){
      inventory=new ArrayList<String>();
    }
    // H key: help
    if (e.getKeyCode() == KeyEvent.VK_H)
    {
      String msg = "Move player: arrows or WASD keys\n" + 
      "Pickup/place ingredient: e\n" +
      "Mix things in the bowl: f\n"+
      "Receive an order on the screen to the right: r\n"+
      "Clear inventory: c\n"+
      "Quit: q\n" +
      "Help: h\n";
      showMessage(msg);
    }

    if (e.getKeyCode() == KeyEvent.VK_R)
    {
      
      if (c.getOrder() == "" && !kitchenScreen)
      {
        c.generateOrder();
        String order = c.getOrder();
        showMessage(order);
        
        if (order.contains("Chocolate") || order.contains("Strawberry")) {
            currentInvBar = inv6;
            
            } else {
            currentInvBar = inv5;
        }
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
      System.out.println(inventory);//testing
      pickupIngredient();
    }
    else if(e.getKeyCode()==KeyEvent.VK_F){
      if(bowl.contains(playerLoc)&&bowlObj.getIngredients().size()!=0){
        bowlObj.mix();
        if(bowlObj.getIngredients().get(0).contains("batter")){
          inventory.add(bowlObj.getIngredients().get(0));
        }
        bowlObj.clearBowl();
      }
    }
    
    else if (e.getKeyCode() == KeyEvent.VK_DOWN || e.getKeyCode() == KeyEvent.VK_S )
    {
      DOWN = true;
      velY=1;
    }
    else if (e.getKeyCode() == KeyEvent.VK_UP || e.getKeyCode() == KeyEvent.VK_W)
    {
      UP = true;
      velY=-1;
    }
    else if (e.getKeyCode() == KeyEvent.VK_LEFT || e.getKeyCode() == KeyEvent.VK_A)
    {
      LEFT = true;
      velX=-1;
    }
    else if (e.getKeyCode() == KeyEvent.VK_RIGHT || e.getKeyCode() == KeyEvent.VK_D)
    {
      RIGHT = true;
      velX=1;
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
      else velY=-1;
    }
    else if (e.getKeyCode() == KeyEvent.VK_UP || e.getKeyCode() == KeyEvent.VK_W)
    {
      UP = false;
      if(!DOWN)velY=0;
      else velY=1;
    }
    else if (e.getKeyCode() == KeyEvent.VK_LEFT || e.getKeyCode() == KeyEvent.VK_A)
    {
      LEFT = false;
      if(!RIGHT)velX=0;
      else velX=1;
    }
    else if (e.getKeyCode() == KeyEvent.VK_RIGHT || e.getKeyCode() == KeyEvent.VK_D)
    {
      RIGHT = false;
      if(!LEFT)velX=0;
      else velX=-1;
    }
  }

  /* override necessary but no action */
  @Override
  public void keyTyped(KeyEvent e) { }

  private void createBoard() throws IOException
  {    
    flour = new Rectangle(50,443, 200, 200);
    bowl = new Rectangle(330,530,100,100);
    strawberries = new Rectangle(917,281,80,100);
    butter = new Rectangle(995,337,100,50);
    eggs = new Rectangle(932,205,150,100);
    milk = new Rectangle(925,400,70,100);
    sugar = new Rectangle(1010,420,100,100);
    chocolate = new Rectangle(915,520,100,100);
    pan = new Rectangle(623,527,200,100);
    openOven = new Rectangle(570,640,210,210);
    openFridge = new Rectangle(840,50,350,540);
    
    npcX = WIDTH+20;
    npcY=0;
    kitchenScreen = true;
    isInv5 = false;
    panFiller="";
    custAtPos = false;
    isRedFairy = false;
    bowlObj = new Bowl();
    c = new Customers();
    
      openFridgeImage =ImageIO.read(new File("openFridge.png"));
      kitchenBG = ImageIO.read(new File("kitchen.png"));
      openOvenImage = ImageIO.read(new File("openOven.png"));
      cashier = ImageIO.read(new File("cashier.png"));
      bowlImage = ImageIO.read(new File("bowl.png"));
      flourImage = ImageIO.read(new File("flour.png"));
      strawberriesImage = ImageIO.read(new File("strawberries.png"));
      butterImage = ImageIO.read(new File("butter.png"));
      eggsImage = ImageIO.read(new File("eggs.png"));
      milkImage = ImageIO.read(new File("milk.png"));
      chocolateImage = ImageIO.read(new File("chocolate.png"));
      sugarImage = ImageIO.read(new File("sugar.png"));
      panImage = ImageIO.read(new File("pan.png"));
      inv5 = ImageIO.read(new File("5inventory.png"));
      inv6 = ImageIO.read(new File("6inventory.png"));
      greenFairyImage = ImageIO.read(new File("greenfairy.png"));
      redFairyImage = ImageIO.read(new File("redfairy.png"));
      doughImage = ImageIO.read(new File("dough.png"));
      vanillaCakeImage = ImageIO.read(new File("vanillacake.png"));
      chocolateCakeImage = ImageIO.read(new File("chocolatecake.png"));
      strawberryCakeImage = ImageIO.read(new File("straberrycake.png"));
      try{
      filledPanImage = ImageIO.read(new File("filledPan.png"));
      }catch(Exception e){System.out.println("Image \"player\" couldn't be read");}
      player = ImageIO.read(new File("player.png")); 

    // save player location
    playerLoc = new Point(currX, currY);

    // create the game frame
    frame = new JFrame();
    frame.setTitle("EscapeRoom");
    frame.setSize(WIDTH, HEIGHT);
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    frame.add(this);
    frame.setVisible(true);
    frame.setResizable(false); 
    frame.addKeyListener(this);
    showMessage("Welcome to the Baking Game! Press h to learn how to play.");
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
      if (flour.contains(playerLoc)&&!inventory.contains("flour"))
        {
          repaint();
          inventory.add("flour");
        }

      else if (strawberries.contains(playerLoc)&&!inventory.contains("strawberries"))
        {
          repaint();
          inventory.add("strawberries");
          
        }

      else if (butter.contains(playerLoc)&&!inventory.contains("butter"))
        {
          repaint();
          inventory.add("butter");
        }

      else if (eggs.contains(playerLoc)&&!inventory.contains("eggs"))
        {
          repaint();
          inventory.add("eggs");
        }
      else if (milk.contains(playerLoc)&&!inventory.contains("milk"))
        {
          repaint();
          inventory.add("milk");
        }
      else if (chocolate.contains(playerLoc)&&!inventory.contains("chocolate"))
        {
          repaint();
          inventory.add("chocolate");
        }
      else if (sugar.contains(playerLoc)&&!inventory.contains("sugar"))
        {
          repaint();
          inventory.add("sugar");
        }
      else if (pan.contains(playerLoc)&&!inventory.contains("pan"))
        {
          if(!panFiller.equals("")){
            inventory.add("pan");
          }else {
            for(int i = 0;i<inventory.size();i++){
              if(inventory.get(i).contains("batter")){
                panFiller=inventory.get(i);
                System.out.println("panfiller: "+panFiller);
                inventory.remove(i);
                break;
              }
            }
          }
          repaint();
          
        }
      else if(openOven.contains(playerLoc)&&inventory.contains("pan")){
        inventory.add(panFiller.substring(0,panFiller.indexOf(" batter")).toLowerCase());
        panFiller="";
        inventory.remove("pan");
      }
      if (bowl.contains(playerLoc)){
        for(int i = 0;i<inventory.size();i++){
          if(!inventory.get(i).contains("batter")&&!inventory.get(i).contains("tray")){
            bowlObj.addIngredient(inventory.get(i));
            inventory.remove(i);
            i--;
          }
        }
        bowlObj.addIngredient(inventory);
        
      }
    }else{
      if(inventory.contains(c.getCake())){
        inventory.remove(c.getCake());
        showMessage(c.generateReply());
        custAtPos = false;
      }
    }
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
      if(isInv5)g.drawImage(inv5, 580, 0, null);
      else g.drawImage(inv6, 415, 0, null);
      g.drawImage(bowlImage,(int)bowl.getX(),(int)bowl.getY(),null);
      if(openOven.contains(playerLoc))g.drawImage(openOvenImage, 0, 0, null);
      if(openFridge.contains(playerLoc))g.drawImage(openFridgeImage, 0, 0, null);
      if (!inventory.contains("strawberries")&&openFridge.contains(playerLoc)){
        if(!bowlObj.getIngredients().contains("strawberries"))g.drawImage(strawberriesImage, (int)strawberries.getX()+20, (int)strawberries.getY()+20, null);
      }
      else g.drawImage(strawberriesImage, WIDTH-160*(inventory.indexOf("strawberries")+1)+30, 40, null);
      if (!inventory.contains("butter")&&openFridge.contains(playerLoc)){
        if(!bowlObj.getIngredients().contains("butter"))g.drawImage(butterImage, (int)butter.getX()+20, (int)butter.getY()+20, null);
      }
      else g.drawImage(butterImage, WIDTH-160*(inventory.indexOf("butter")+1)+33, 67, null);
      if (!inventory.contains("eggs")&&openFridge.contains(playerLoc)){
        if(!bowlObj.getIngredients().contains("eggs"))g.drawImage(eggsImage, (int)eggs.getX()+30, (int)eggs.getY()+20, null);
      }
      else g.drawImage(eggsImage, WIDTH-160*(inventory.indexOf("eggs")+1)+6, 60, null);
      if (!inventory.contains("milk")&&openFridge.contains(playerLoc)){
        if(!bowlObj.getIngredients().contains("milk"))g.drawImage(milkImage, (int)milk.getX()+20, (int)milk.getY()+20, null);
      }
      else g.drawImage(milkImage, WIDTH-160*(inventory.indexOf("milk")+1)+40, 40, null);
      if (!inventory.contains("chocolate")&&openFridge.contains(playerLoc)){
        if(!bowlObj.getIngredients().contains("chocolate"))g.drawImage(chocolateImage, (int)chocolate.getX()+20, (int)chocolate.getY()+20, null);
      }
      else g.drawImage(chocolateImage, WIDTH-160*(inventory.indexOf("chocolate")+1)+30, 40, null);
      if (!inventory.contains("sugar")&&openFridge.contains(playerLoc)){
        if(!bowlObj.getIngredients().contains("sugar"))g.drawImage(sugarImage, (int)sugar.getX()+20, (int)sugar.getY()+20, null);
      }
      else g.drawImage(sugarImage, WIDTH-160*(inventory.indexOf("sugar")+1)+30, 45, null);
      if (!inventory.contains("flour")){
        if(!bowlObj.getIngredients().contains("flour"))g.drawImage(flourImage, (int)flour.getX()+22, (int)flour.getY()+20, null);
      }
      else g.drawImage(flourImage, WIDTH-160*(inventory.indexOf("flour")+1)+10, 0, null);
      for(int i = 0;i<inventory.size();i++){
        if(inventory.get(i).contains("batter"))g.drawImage(doughImage, WIDTH-160*(i+1), 40, null);
      }
      if(inventory.contains("vanilla cake"))g.drawImage(vanillaCakeImage, WIDTH-160*(inventory.indexOf("vanilla cake")+1)-15, 20, null);
      if(inventory.contains("chocolate cake"))g.drawImage(chocolateCakeImage, WIDTH-160*(inventory.indexOf("chocolate cake")+1)-5, 10, null);
      if(inventory.contains("strawberry cake"))g.drawImage(strawberryCakeImage, WIDTH-160*(inventory.indexOf("strawberry cake")+1)-17, 18, null);
      if (panFiller.equals("")&&!inventory.contains("pan"))g.drawImage(panImage, (int)pan.getX()+20, (int)pan.getY()+20, null);
      else if(!inventory.contains("pan"))g.drawImage(filledPanImage, (int)pan.getX()+20, (int)pan.getY()-1, null);
      else g.drawImage(filledPanImage, WIDTH-160*(inventory.indexOf("pan")+1), 40, null);
    }else{
      g.drawImage(cashier,0,0,null);
      if(isInv5)g.drawImage(inv5, 580, 0, null);
      else g.drawImage(inv6, 415, 0, null);
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
