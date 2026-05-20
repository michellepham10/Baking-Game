import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
public class Minigame {
    private int score;
    private int timeLeft;
    private Timer gameTimer;
    private JLabel scoreLabel;
    private JLabel timerLabel;
    private JButton mashButton;
    public Minigame() {
        score = 0;
        timeLeft = 0;
    }
    public void mixDough(){
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
        
        game.add(timerLabel);
        game.add(mashButton);
        game.add(scoreLabel);

        gameTimer.start();
        game.setVisible(true);
    }
    public void crackEggs(){
        timeLeft=12;
        JFrame game = new JFrame("Egg cracking");
        game.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        game.setSize(250, 200);
        game.setLayout(new FlowLayout());
        game.setResizable(false);
        game.setLocationRelativeTo(null);
        mashButton = new JButton("Click!");
        scoreLabel = new JLabel("Score: 0");
        timerLabel = new JLabel("Count: 3");

        mashButton.setFont(new Font("Arial", Font.BOLD, 24));
        mashButton.setPreferredSize(new Dimension(200, 80));

        // Action when button is clicked
        mashButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (timeLeft > 0) {
                    score+=(1+(12-timeLeft)%4);
                    scoreLabel.setText("Test: "+score);
                }
            }
        });

        // Countdown Timer
        gameTimer = new Timer(500, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (timeLeft > 0) {
                    timeLeft--;
                    timerLabel.setText("Count: " + (3-(12-timeLeft)%4));
                } else {
                    gameTimer.stop();
                    mashButton.setEnabled(false);
                    JOptionPane.showMessageDialog(game, "Game Over! Final Score: " + score);
                    game.dispose();
                }
            }
        });
        
        game.add(timerLabel);
        game.add(mashButton);
        game.add(scoreLabel);

        gameTimer.start();
        game.setVisible(true);
    }
    public int getScore(){return score;}
}
