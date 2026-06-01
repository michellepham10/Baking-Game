import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
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
        EggGameBG = ImageIO.read(new File("EggGameBG.png"));
        oneEgg = ImageIO.read(new File("oneEgg.png"));
        brokenEgg = ImageIO.read(new File("brokenEgg.png"));
        stun=false;
        hit=false;

    }
    public void mixDough(){
        gameID = MIX_DOUGH;
        timeLeft=5;
        JFrame game = new JFrame("Mixer");
        game.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        game.setSize(250, 200);
        game.setLayout(new FlowLayout());
        game.setResizable(false);
        game.setLocationRelativeTo(null);
        mashButton = new JButton("MIX!");
        scoreLabel = new JLabel("Score: 0");
        timerLabel = new JLabel("Time Remaining: 5s");

        mashButton.setFont(new Font("Arial", Font.BOLD, 24));
        mashButton.setPreferredSize(new Dimension(200, 80));

        // Action when button is clicked
        mashButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (timeLeft > 0) {
                    score++;
                    scoreLabel.setText("Score: " + score);
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
        game.add(panel);
        game.add(timerLabel);
        game.add(mashButton);
        game.add(scoreLabel);

        gameTimer.start();
        game.setVisible(true);
    }
    public void crackEggs(){
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
        //game.add(this);
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
    public int getScore(){return score;}

    public class MovingImagePanel extends JPanel{
        private int x = 0; // Starting X position
        public MovingImagePanel() {}

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            // Draw the image at the updated x and y coordinates
            g.drawImage(EggGameBG,getWidth()/2-EggGameBG.getWidth()/2,0,this);
            if (stun) {
                g.drawImage(brokenEgg, 325, 285, this);
            }else if(hit){
                g.drawImage(brokenEgg, 235, 190, game);
            }else g.drawImage(oneEgg, x+255, 20, this);
        }
        public void setXVal(int x){this.x=x;};
        public int getXVal(){return x;};
    }
}

