/*
* Project 4.1.5: Escape Room Revisited
* 
* V1.0
* Copyright(c) 2024 PLTW to present. All rights reserved
*/
//import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.Image;
import java.awt.Point;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.lang.Thread;

import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

import javax.imageio.ImageIO;

import java.io.File;
//import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;
//import java.util.Scanner;

/**
 * A game where a player maneuvers around a gameboard to answer
 * riddles or questions, collecing prizes with correct answers.
 */
public class GameGUI extends JComponent implements KeyListener
{
  static final long serialVersionUID = 415L;

  // constants for gameboard confg
  // private static final int WIDTH = 510;
  // private static final int HEIGHT = 360;
  private static final int WIDTH = 1400;
  private static final int HEIGHT = 1010;
  private static final int SPACE_SIZE = 60;
  private static final int GRID_W = 8;
  private static final int GRID_H = 5;

  // frame and images for gameboard
  private JFrame frame;
  private Image bgImage;
  private Image prizeImage;
  private Image flourImage;
  private Image player;
  private Image playerQ;

  // player config
  private int currX = 15; 
  private int currY = 15;
  private int velX;
  private int velY;
  private boolean atFlour;
  private Point playerLoc;

  //for movement
  private boolean LEFT = false;
  private boolean RIGHT = false;
  private boolean UP = false;
  private boolean DOWN = false;
  // walls, player level, and prizes
  private int playerLevel = 1;
  private Rectangle[] prizes;
  private Rectangle[] flour;

  private int score = 1; 
  String tempPhrase = "";

  /**
   * Constructor for the GameGUI class.
   * 
   * Gets the player level and the questions/answers for the game 
   * from two files on disk. Creates th gameboard with a background image,
   *  walls, prizes, and a player.
   */
  public GameGUI() throws IOException,InterruptedException
  {
    createBoard();
    tick();
  }


  public void tick() throws InterruptedException{
    while(true){
      movePlayer(velX,velY);
      Thread.sleep(3);
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
    
    // Arrow and WASD keys: moved down, up, left or right
    if (e.getKeyCode() == KeyEvent.VK_E)
    {
      
    }
    
    if (e.getKeyCode() == KeyEvent.VK_DOWN || e.getKeyCode() == KeyEvent.VK_S )
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

  /**
  * Add player, prizes, and walls to the gameboard.
  */
  private void createBoard() throws IOException
  {    
    prizes = new Rectangle[playerLevel];
    flour = new Rectangle[playerLevel];
    createFlour();


    bgImage = ImageIO.read(new File("kitchen.png"));
    flourImage = ImageIO.read(new File("flour.png"));
    player = ImageIO.read(new File("player.png")); 
    playerQ = ImageIO.read(new File("playerQ.png")); 
    
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
    if(!((currX+incrx < 0 || currX+incrx > WIDTH-SPACE_SIZE)))currX += incrx;
    if(!((currY+incry < 0 || currY+incry > HEIGHT-SPACE_SIZE)))currY += incry;
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

  private void checkForFlour()
  {
    double fx = playerLoc.getX();
    double fy = playerLoc.getY();

    for (Rectangle r: flour)
    {
      if (r.contains(fx, fy))
      {
        atFlour = true;
        repaint();
        return;
      }
    }
    atFlour = false;
  }



  /**
   * Pickup a prize and score points. If no prize is in that location, it results in a penalty.
   */
  /*private void pickupPrize()
  {
    double px = playerLoc.getX();
    double py = playerLoc.getY();

    for (Rectangle p: prizes)
    {
      // if location has a prize, pick it up
      if (p.getWidth() > 0 && p.contains(px, py))
      {
        p.setSize(0,0);
        atPrize = false;
        repaint();
      }
    }
  }*/

 /**
  * End the game, update and save the player level.
  */
  private void endGame() 
  {
    /*try {
      FileWriter fw = new FileWriter("level.csv");
      String s = playerLevel + "\n";
      fw.write(s);
      fw.close();
    } catch (IOException e)  { System.err.println("Could not level up."); }*/
  
    setVisible(false);
    frame.dispose();
  }


  private void createFlour()
  {
    int s = SPACE_SIZE; 
    Random rand = new Random();
    for (int numPrizes = 0; numPrizes < playerLevel; numPrizes++)
    {
      int h = rand.nextInt(GRID_H);
      int w = rand.nextInt(GRID_W);
      Rectangle r = new Rectangle((w*s + 15),(h*s + 15), 15, 15);

       // get a rect. without a prize already there
       for (Rectangle f : flour) {
        while (f != null && f.equals(r)) {
          h = rand.nextInt(GRID_H);
          w = rand.nextInt(GRID_W);
          r = new Rectangle((w*s + 15),(h*s + 15), 15, 15);
        }
      }
      flour[numPrizes] = r;
    }
  }



  /* 
   * Manage board elements with graphics buffer g.
   * For internal use - do not call directly, use repaint instead.
   */
  public void paintComponent(Graphics g)
  {
    super.paintComponent(g);
    Graphics2D g2 = (Graphics2D)g;

    // draw grid
    g.drawImage(bgImage, 0, 0, null);

    for (Rectangle f : flour)
    {
      // pickedup prizes are 0 size so don't render
      if (f.getWidth() > 0) 
      {
      int fx = (int)f.getX();
      int fy = (int)f.getY();
      g.drawImage(flourImage, fx, fy, null);
      }
    }

    // add walls
    // for (Rectangle r : walls) 
    // {
    //   g2.setPaint(Color.BLACK);
    //   g2.fill(r);
    // }
   
    // draw player, saving its location
    g.drawImage(player, currX, currY, 40,40, null);
    playerLoc.setLocation(currX, currY);
  }

}
 
