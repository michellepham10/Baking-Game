/*
* Project 4.1.5: Escape Room Revisited
* 
* V1.0
* Copyright(c) 2024 PLTW to present. All rights reserved
*/
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.Image;
import java.awt.Point;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

import javax.imageio.ImageIO;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner; 

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
  private static final int MOVE = 10;

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
  private boolean atPrize;
  private boolean atFlour;
  private Point playerLoc;

  // walls, player level, and prizes
  private int playerLevel = 1;
  private Rectangle[] prizes;
  private Rectangle[] flour;

  // scores, sometimes awarded as (negative) penalties
  private int goodMove = 1;
  private int offGridVal = 5; // penalty 
  private int hitWallVal = 5;  // penalty 
  private int correctAns = 10;
  private int wrongAns = 7; // penalty 
  private int score = 1; 
  String tempPhrase = "";

  /**
   * Constructor for the GameGUI class.
   * 
   * Gets the player level and the questions/answers for the game 
   * from two files on disk. Creates th gameboard with a background image,
   *  walls, prizes, and a player.
   */
  public GameGUI() throws IOException
  {
    createQuiz();
    createBoard();
  }


  public void tick(){
    while(true){
      movePlayer(velX,velY);
    }
  }
  /**
   * Create array of questions and answers from the quiz.csv file.
   * 
   * @preconditon: The CSV file contains at least playerLevel number of questions.
   * (It may contain more unused questions.)
   * 
   * @postconditon: A 2D array is populated with one question and one answer per row.
   * 
   * @throws IOException
   */
  private void createQuiz() 
  {
    /* your code here */
    ArrayList<String> qList = new ArrayList<String>();

    // qList.add()

    // start
    
    int numOfLines = 0;
    try 
    {
      Scanner sc = new Scanner(new File("quiz.txt"));
      while (sc.hasNextLine())
      {
        tempPhrase = sc.nextLine().trim();
        numOfLines++;
      }
    } catch(Exception e) { System.out.println("Error reading or parsing phrases.txt"); }
    
		int randomInt = (int) (Math.random() * ((65 - 1) / 5 + 1)) * 5 + 1;
    
    try 
    {
      int count = 0;
      Scanner sc = new Scanner(new File("quiz.txt"));
      while (sc.hasNextLine())
      {
        count++;
        String temp = sc.nextLine().trim();
        if (count == randomInt)
        {
          // tempPhrase = temp;
          tempPhrase = temp + "\n" + sc.nextLine() + "\n" + sc.nextLine() + "\n" + sc.nextLine() + "\n" + sc.nextLine();
        }
      }
    } catch (Exception e) { System.out.println("Error reading or parsing phrases.txt"); }
    
    qList.add(tempPhrase);
  
    // end
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
    // P Key: If player is at a prize, ask a questiona and check for correct answer.
    // If correct, pickup prize and add correctAns to score, otherwse deduct from score.
    if (e.getKeyCode() == KeyEvent.VK_P )
    {
      /* your code here */ 
      checkForPrize();
      if (atPrize == true)
        {
          askQuestion(tempPhrase);
        }          
      pickupPrize();
      
    }

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
      velY=-MOVE;
      movePlayer(0, MOVE);
    }
    if (e.getKeyCode() == KeyEvent.VK_UP || e.getKeyCode() == KeyEvent.VK_W)
    {
      velY=MOVE;
      movePlayer(0, -MOVE);
    }
    if (e.getKeyCode() == KeyEvent.VK_LEFT || e.getKeyCode() == KeyEvent.VK_A)
    {
      velX=-MOVE;
      movePlayer(-MOVE, 0);
    }
    if (e.getKeyCode() == KeyEvent.VK_RIGHT || e.getKeyCode() == KeyEvent.VK_D)
    {
      velX=MOVE;
      movePlayer(MOVE, 0);
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
    if(e.getKeyCode()== KeyEvent.VK_P)checkForPrize();
    if (e.getKeyCode() == KeyEvent.VK_DOWN || e.getKeyCode() == KeyEvent.VK_S )
    {
      velY=0;
    }
    if (e.getKeyCode() == KeyEvent.VK_UP || e.getKeyCode() == KeyEvent.VK_W)
    {
      velY=0;
    }
    if (e.getKeyCode() == KeyEvent.VK_LEFT || e.getKeyCode() == KeyEvent.VK_A)
    {
      velX=0;
    }
    if (e.getKeyCode() == KeyEvent.VK_RIGHT || e.getKeyCode() == KeyEvent.VK_D)
    {
      velX=0;
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
    createPrizes();
    flour = new Rectangle[playerLevel];
    createFlour();


    // bgImage = ImageIO.read(new File("grid.png"));
    bgImage = ImageIO.read(new File("kitchen.png"));
    prizeImage = ImageIO.read(new File("coin.png"));
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

    checkForPrize();

     showMessage("Welcome to the Escape Room. Press h to learn how to play.");
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
    int newX = currX + incrx;
    int newY = currY + incry;

    // check if off grid horizontally and vertically
    if ( (newX < 0 || newX > WIDTH-SPACE_SIZE) || (newY < 0 || newY > HEIGHT-SPACE_SIZE) )
    {
      showMessage("You have tried to go off the grid!");
    }

    // all is well, move player
    currX += incrx;
    currY += incry;
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

  /**
   * Display a dialog that asks a question and waits for an answer
   *
   * @param the question to display
   *
   * @return the text the user entered, null otherwise
   */
  private String askQuestion(String q)
  {
    // \n was parsed as a literal by the split method, replace with escape sequence
    return JOptionPane.showInputDialog(q.replace("\\n","\n") , JOptionPane.OK_OPTION);  
  }

  /**
   * If there's a prize at the location, set atPrize to true and change player image
   *
   * @param w number of walls to create
   */
  private void checkForPrize()
  {
    double px = playerLoc.getX();
    double py = playerLoc.getY();

    for (Rectangle r: prizes)
    {
      if (r.contains(px, py))
      {
        atPrize = true;
        repaint();
        return;
      }
    }
    atPrize = false;
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
  private void pickupPrize()
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
  }

 /**
  * End the game, update and save the player level.
  */
  private void endGame() 
  {
    try {
      FileWriter fw = new FileWriter("level.csv");
      String s = playerLevel + "\n";
      fw.write(s);
      fw.close();
    } catch (IOException e)  { System.err.println("Could not level up."); }
  
    setVisible(false);
    frame.dispose();

    System.out.println("You took " + score + " steps");
  }

  /**
   * Add randomly placed prizes to be picked up.
   */
  private void createPrizes()
  {
    int s = SPACE_SIZE; 
    Random rand = new Random();
    for (int numPrizes = 0; numPrizes < playerLevel; numPrizes++)
    {
      int h = rand.nextInt(GRID_H);
      int w = rand.nextInt(GRID_W);
      Rectangle r = new Rectangle((w*s + 15),(h*s + 15), 15, 15);

       // get a rect. without a prize already there
       for (Rectangle p : prizes) {
        while (p != null && p.equals(r)) {
          h = rand.nextInt(GRID_H);
          w = rand.nextInt(GRID_W);
          r = new Rectangle((w*s + 15),(h*s + 15), 15, 15);
        }
      }
      prizes[numPrizes] = r;
    }
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

    // add prizes
    for (Rectangle p : prizes)
    {
      // pickedup prizes are 0 size so don't render
      if (p.getWidth() > 0) 
      {
      int px = (int)p.getX();
      int py = (int)p.getY();
      g.drawImage(prizeImage, px, py, null);
      }
    }

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
    if(atPrize)
    {
      g.drawImage(playerQ, currX, currY, 40,40, null);
    }
    else
    {
      g.drawImage(player, currX, currY, 40,40, null);
    }
    playerLoc.setLocation(currX, currY);
  }

}
 