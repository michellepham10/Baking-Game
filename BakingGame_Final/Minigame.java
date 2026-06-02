import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;

public class Minigame extends JComponent{
    private int score,totalTime,timeLeft,count,gameID;
    private final int MIX_DOUGH = 1;
    private final int CRACK_EGGS = 2;
    public Timer gameTimer;
    private JPanel panel;
    private JLabel scoreLabel;
    private JLabel timerLabel;
    private JButton mashButton;
    private JFrame game;
    private BufferedImage MixGameBG;
    private BufferedImage spoon;
    private BufferedImage EggGameBG;
    private BufferedImage oneEgg;
    private BufferedImage brokenEgg;
    private boolean stun;
    private boolean hit;
    public Minigame() throws IOException{
        gameID = 0;
        score = 0;
        totalTime=0;
        timeLeft = 0;
        spoon = ImageIO.read(new File("spoon.png"));
        EggGameBG = ImageIO.read(new File("EggGameBG.png"));
        oneEgg = ImageIO.read(new File("oneEgg.png"));
        brokenEgg = ImageIO.read(new File("brokenEgg.png"));
        MixGameBG= ImageIO.read(new File("MixGameBG.png"));
        stun=false;
        hit=false;

    }
    public void mixDough(){
        gameID = MIX_DOUGH;
        timeLeft=5;
        game = new JFrame("Mixer");
        game.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        game.setSize(332, 400);
        game.setLayout(new BoxLayout(game.getContentPane(),BoxLayout.PAGE_AXIS));
        game.setResizable(false);
        game.setLocationRelativeTo(null);
        mashButton = new JButton("MIX!");
        scoreLabel = new JLabel("Score: 0");
        timerLabel = new JLabel("Time Remaining: 5s");
        
        mashButton.setFont(new Font("Arial", Font.BOLD, 24));
        scoreLabel.setFont(new Font("Arial", Font.BOLD, 24));
        timerLabel.setFont(new Font("Arial", Font.BOLD, 24));

        mashButton.setPreferredSize(new Dimension(100, 40));
        MovingImagePanel pan = new MovingImagePanel();
        pan.setPreferredSize(new Dimension(MixGameBG.getWidth(),MixGameBG.getHeight()));
        pan.setXVal(75);
        JPanel pan2 = new JPanel();
        // Action when button is clicked
        mashButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (timeLeft > 0) {
                    score++;
                    scoreLabel.setText("Score: " + score);
                    pan.setXVal((int)((Math.sin(score*Math.PI/6))*70)+75);
                    pan.repaint();
                }
            }
        });

        // Countdown Timer
        gameTimer = new Timer(1000, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (timeLeft > 0) {
                    timeLeft--;
                    timerLabel.setText("Time Remaining: " + timeLeft + "s");
                    
                    
                } else {
                    gameTimer.stop();
                    mashButton.setEnabled(false);
                    JOptionPane.showMessageDialog(game, "Game Over! Final Score: " + score);
                    game.dispose();
                }
            }
        });
        game.add(pan);
        pan2.add(timerLabel);
        pan2.add(mashButton);
        pan2.add(scoreLabel);
        game.add(pan2);
        gameTimer.start();
        game.setVisible(true);
    }
    public void crackEggs(){
        score=0;//crutch omg
        gameID = CRACK_EGGS;
        count=3;
        totalTime=24;
        timeLeft=totalTime;

        game = new JFrame();
        game.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        game.setSize(500, 900);
        game.setLayout(new BoxLayout(game.getContentPane(),BoxLayout.PAGE_AXIS));
        game.setResizable(false);
        game.setLocationRelativeTo(null);

        mashButton = new JButton("Click!");
        scoreLabel = new JLabel("Score: "+score);
        timerLabel = new JLabel("Count: "+(count-1));

        mashButton.setFont(new Font("Arial", Font.BOLD, 30));
        scoreLabel.setFont(new Font("Arial", Font.BOLD, 30));
        timerLabel.setFont(new Font("Arial", Font.BOLD, 30));
    
        mashButton.setPreferredSize(new Dimension(300, 30));

        MovingImagePanel p = new MovingImagePanel();
        JPanel p2= new JPanel();
        JPanel p3 = new JPanel();
        p.setPreferredSize(new Dimension(500,448));
        p3.setPreferredSize(new Dimension(500,50));
        p2.add(timerLabel);
        p3.add(mashButton);
        p2.add(scoreLabel);
        game.add(p);
        game.add(p2);
        game.add(p3);
        
        
        


        // Action when button is clicked
        mashButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (timeLeft > 0) {
                    scoreLabel.setText("Score: "+score);
                    if((totalTime-timeLeft)%count==0){
                        score+=((totalTime-timeLeft-1)%count);
                        hit=true;
                    }
                    else if(!stun){
                        stun=true;
                        score+=((totalTime-timeLeft-1)%count);
                    }else stun=false;
                    
                }
            }
        });

        // Countdown Timer
        gameTimer = new Timer(350, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (timeLeft > 0) {
                    if((totalTime-timeLeft)%count==0)stun=false;
                    else hit=false;
                    p.setXVal((p.getXVal()+100)%(50*count));
                    p.repaint();
                    timerLabel.setText("Count: " + (count-1-(totalTime-timeLeft)%count));
                    timeLeft--;
                    
                } else {
                    gameTimer.stop();
                    mashButton.setEnabled(false);
                    JOptionPane.showMessageDialog(game, "Game Over! Final Score: " + score);
                    game.dispose();
                }
                repaint();
            }
        });

        gameTimer.setInitialDelay(0);
        gameTimer.start();
        game.pack();
        game.setVisible(true);
        
        
        
    }
    public void cakeDecor(Color c){
        timerLabel = new JLabel("Time Remaining: 5s");
        timeLeft=3;
        timerLabel.setFont(new Font("Arial", Font.BOLD, 24));
        
        game = new JFrame("Cake Decorating Game");
        game.setLocation(600,200);
        game.setLayout(new BoxLayout(game.getContentPane(),BoxLayout.PAGE_AXIS));
        game.setSize(640, 720);
        game.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        mini min = new mini(c);   // Call the other class
        min.setPreferredSize(new Dimension(640,640));
        gameTimer = new Timer(1000, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (timeLeft > 0) {
                    timerLabel.setText("Time Remaining: "+timeLeft+"s");
                    timeLeft--;
                    
                } else {
                    gameTimer.stop();
                    score += min.numTransparent()/8000;
                    JOptionPane.showMessageDialog(game, "Game Over! Final Score: " + score);
                    game.dispose();
                }
                repaint();
            }
        });
        
        JPanel panel2 = new JPanel();
        panel2.setPreferredSize(new Dimension(640,50));
        panel2.add(timerLabel);
        game.add(min);
        game.add(panel2);
        
        gameTimer.setInitialDelay(0);
        gameTimer.start();
        game.setVisible(true);
    }
    public int getScore(){return score;}

    public class MovingImagePanel extends JPanel{
        private int x = 0; // Starting X position
        public MovingImagePanel() {}

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            if(gameID==MIX_DOUGH){
                g.drawImage(MixGameBG,getWidth()/2-MixGameBG.getWidth()/2,0,this);
                g.drawImage(spoon,x+70,33,this);
            }
            else if(gameID==CRACK_EGGS){
                g.drawImage(EggGameBG,getWidth()/2-EggGameBG.getWidth()/2,0,this);
                if (stun) {
                    g.drawImage(brokenEgg, 325, 285, this);
                }else if(hit){
                    g.drawImage(brokenEgg, 235, 190, game);
                }else g.drawImage(oneEgg, x+255, 20, this);
            }
        }
        public void setXVal(int x){this.x=x;};
        public int getXVal(){return x;};
    }
    public class mini extends JPanel {

    private BufferedImage cakeImage;
    private BufferedImage frostingLayer;
    private Graphics2D frostingGraphics;

    public mini(Color c) {
        try {
            cakeImage = ImageIO.read(new File("blank_cake3.png")); // your cake image
        } catch (Exception e) {
            System.out.println("Could not load cake image");
        }

        // Create a transparent layer to draw frosting on
        frostingLayer = new BufferedImage(640, 640, BufferedImage.TYPE_INT_ARGB);
        frostingGraphics = frostingLayer.createGraphics();
        frostingGraphics.setStroke(new BasicStroke(7)); // frosting thickness
        frostingGraphics.setColor(c);           // frosting color

        addMouseMotionListener(new MouseMotionAdapter() {
            public void mouseDragged(MouseEvent e) {
                frostingGraphics.fillOval(e.getX()-25, e.getY()-25, 50, 50);
                repaint();
            }
        });
    }
    public int numTransparent(){
        int count = 0;
        for(int row = 0;row<640;row++){
            for(int col=0;col<640;col++){
                if((frostingLayer.getRGB(row,col)>>24) != 0x00 )count++;
            }
        }
        return count;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        g.drawImage(cakeImage, 0, 0, null);       // draw cake
        g.drawImage(frostingLayer, 0, 0, null);   // draw frosting on top
    }
}
}
