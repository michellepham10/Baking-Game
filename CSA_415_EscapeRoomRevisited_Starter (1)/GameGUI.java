/*
* Project 4.1.5: Escape Room Revisited
* 
* V1.0
* Copyright(c) 2024 PLTW to present. All rights reserved
*/
//import java.awt.Color;
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
//import java.util.Scanner;

import org.w3c.dom.css.Rect;

/**
 * A game where a player maneuvers around a gameboard to answer
 * riddles or questions, collecing prizes with correct answers.
 */
public class GameGUI extends JComponent implements KeyListener
{
  // constants for gameboard confg
  private static final int WIDTH = 1400;
  private static final int HEIGHT = 1036;

  // frame and images for gameboard
  private JFrame frame;
  private Image kitchenBG;
  private Image customerBG;
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
  private Image panImage;
  private Image filledPanImage;
  private Image player;
  private Image openOvenImage;
  private Image openFridgeImage;
  
  // player config
  private int currX = 15; 
  private int currY = 15;
  private int velX;
  private int velY;
  private String panFiller;
  private Point playerLoc;
  private boolean kitchenScreen;

  //for movement
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

  /**
   * Constructor for the GameGUI class.
   * 
   * Gets the player level and the questions/answers for the game 
   * from two files on disk. Creates th gameboard with a background image,
   *  walls, prizes, and a player.
   */
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

    // H key: help
    if (e.getKeyCode() == KeyEvent.VK_H)
    {
      String msg = "Move player: arrows or WASD keys\n" + 
      "Pickup prize: p\n" +
      "Quit: q\n" +
      "Help: h\n";
      showMessage(msg);
    }

    if (e.getKeyCode() == KeyEvent.VK_R)
    {
      
      if (c.getOrder() == "" && !kitchenScreen)
      {
        c.generateOrder();
        showMessage(c.getOrder());
      }

      else if (c.getOrder() != "")
      {
        showMessage(c.getOrder());
      }

      else if (c.getOrder() == "" && kitchenScreen)
      {
        showMessage("You do not have an order yet!");
      }
      
    }
    
    // Arrow and WASD keys: moved down, up, left or right
    if (e.getKeyCode() == KeyEvent.VK_E)
    {
      System.out.println(inventory);//testing
      pickupIngredient();
    }
    else if(e.getKeyCode()==KeyEvent.VK_F){
      bowlObj.mix();
      ArrayList<String> temp = bowlObj.getIngredients();
      for(String n:temp){inventory.add(n);}
      bowlObj.clearBowl();
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
    flour = new Rectangle(70,460, 30, 30);
    bowl = new Rectangle(200,500,30,30);
    strawberries = new Rectangle(935,300,50,50);
    butter = new Rectangle(970,300,70,40);
    eggs = new Rectangle(460,230,100,80);
    milk = new Rectangle(0,0,100,80);
    sugar = new Rectangle(860,440,100,80);
    chocolate = new Rectangle(860,600,100,80);
    pan = new Rectangle(500,500,160,160);
    openOven = new Rectangle(560,620,280,240);
    openFridge = new Rectangle(840,50,350,540);
    
    kitchenScreen = true;
    panFiller="";
    bowlObj = new Bowl();
    c = new Customers();
    
      openFridgeImage =ImageIO.read(new File("openFridge.png"));

      kitchenBG = ImageIO.read(new File("kitchen.png"));

      openOvenImage = ImageIO.read(new File("openOven.png"));
    // customerBG = ImageIO.read(new File("customerScreen.png"));
    // cashier = ImageIO.read(new File("cashier.png"));
      bowlImage = ImageIO.read(new File("bowl.png"));

      flourImage = ImageIO.read(new File("flour.png"));

      strawberriesImage = ImageIO.read(new File("strawberries.png"));

      butterImage = ImageIO.read(new File("butter.png"));

      eggsImage = ImageIO.read(new File("eggs.png"));

      milkImage = ImageIO.read(new File("milk.png"));

      chocolateImage = ImageIO.read(new File("chocolate.png"));
      sugarImage = ImageIO.read(new File("sugar.png"));
      panImage = ImageIO.read(new File("pan.png"));
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
    //showMessage("Welcome to the Baking Game! Press h to learn how to play.");
  }

  /**
   * Increment/decrement the player location by the amount designated.
   * This method checks for bumping into walls and going off the grid,
   * both of which result in a penalty.
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
    
    if (flour.contains(playerLoc))
      {
        repaint();
        inventory.add("flour");
      }

    else if (strawberries.contains(playerLoc))
      {
        repaint();
        inventory.add("strawberries");
        
      }

    else if (butter.contains(playerLoc))
      {
        repaint();
        inventory.add("butter");
      }

    else if (eggs.contains(playerLoc))
      {
        repaint();
        inventory.add("eggs");
      }
      
    else if (pan.contains(playerLoc))
      {
        if(panFiller.equals("")){
          inventory.add("pan");
        }else {
          for(int i = 0;i<inventory.size();i++){
            if(inventory.get(i).contains("batter")){
              panFiller=inventory.get(i);
              inventory.remove(i);
            }
          }
        }
        repaint();
        
      }
    else if(openOven.contains(playerLoc)&&inventory.contains("pan")){
      
      inventory.add(panFiller.substring(0,panFiller.indexOf("batter"))+"cake");
      panFiller="";
    }
    if (bowl.contains(playerLoc)){
      for(int i = 0;i<inventory.size();i++){
        if(!inventory.get(i).contains("batter")&&!inventory.get(i).contains("tray")){
          bowlObj.addIngredient(inventory.get(i));
          inventory.remove(i);
        }
      }
      bowlObj.addIngredient(inventory);
      
    }
  }

  /**
    * End the game, update and save the player level.
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
      g.drawImage(bowlImage,(int)bowl.getX(),(int)bowl.getY(),null);
      if(openOven.contains(playerLoc))g.drawImage(openOvenImage, 0, 0, null);
      else if(openFridge.contains(playerLoc)){
        g.drawImage(openFridgeImage, 0, 0, null);
        if (!inventory.contains("strawberries")) g.drawImage(strawberriesImage, (int)strawberries.getX(), (int)strawberries.getY(), null);
          else g.drawImage(strawberriesImage, WIDTH-100*(inventory.indexOf("strawberries")+1), 40, null);
        if (!inventory.contains("butter")) g.drawImage(butterImage, (int)butter.getX(), (int)butter.getY(), null);
          else g.drawImage(butterImage, WIDTH-100*(inventory.indexOf("butter")+1), 40, null);
        if (!inventory.contains("eggs")) g.drawImage(eggsImage, (int)eggs.getX(), (int)eggs.getY(), null);
          else g.drawImage(eggsImage, WIDTH-100*(inventory.indexOf("eggs")+1), 40, null);
        if (!inventory.contains("milk")) g.drawImage(milkImage, (int)milk.getX(), (int)milk.getY(), null);
          else g.drawImage(milkImage, WIDTH-100*(inventory.indexOf("milk")+1), 40, null);
        if (!inventory.contains("chocolate")) g.drawImage(chocolateImage, (int)chocolate.getX(), (int)chocolate.getY(), null);
          else g.drawImage(chocolateImage, WIDTH-100*(inventory.indexOf("chocolate")+1), 40, null);
        if (!inventory.contains("sugar")) g.drawImage(sugarImage, (int)sugar.getX(), (int)sugar.getY(), null);
          else g.drawImage(sugarImage, WIDTH-100*(inventory.indexOf("sugar")+1), 40, null);
      }
      if (!inventory.contains("flour")) g.drawImage(flourImage, (int)flour.getX(), (int)flour.getY(), null);
      else g.drawImage(flourImage, WIDTH-100*(inventory.indexOf("flour")+1), 40, null);
      if (panFiller.equals("")&&!inventory.contains("pan")) g.drawImage(panImage, (int)pan.getX(), (int)pan.getY(), null);
      else if(!inventory.contains("pan"))g.drawImage(filledPanImage, (int)pan.getX(), (int)pan.getY(), null);
        else g.drawImage(panImage, WIDTH-100*(inventory.indexOf("pan")+1), 40, null);
    }else{
      g.drawImage(customerBG,0,0,null);
    }
    g.drawImage(player, currX, currY, 40,40, null);
    playerLoc.setLocation(currX, currY);
  }

}
