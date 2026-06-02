import javax.swing.*;
import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.io.IOException;
public class RecipeBook extends JFrame{
    private ImageIcon[] pages;
    private int idx;
    private JButton next;
    private JButton previous;
    private JPanel panel;
    public RecipeBook() throws IOException{
        idx=0;
        pages = new ImageIcon[5];
        
        pages[0]=new ImageIcon(ImageIO.read(new File("1.png")));
        pages[1]=new ImageIcon(ImageIO.read(new File("2.png")));
        pages[2]=new ImageIcon(ImageIO.read(new File("3.png")));
        pages[3]=new ImageIcon(ImageIO.read(new File("4.png")));
        pages[4]=new ImageIcon(ImageIO.read(new File("5.png")));
        setTitle("Recipe Book");
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        setLocation((int)(screenSize.width*0.02+1375), (int)(screenSize.height*0.2));
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLayout(new BoxLayout(this.getContentPane(), BoxLayout.Y_AXIS));
        panel = new JPanel();
        panel.setAlignmentX(Component.LEFT_ALIGNMENT);

        
        setSize(new Dimension(512,662));
        JLabel label = new JLabel();
        label.setIcon(pages[0]);
        label.setAlignmentX(Component.LEFT_ALIGNMENT);
        label.setPreferredSize(new Dimension(512,512));
        next = new JButton("Next");
        next.setFont(new Font("Arial", Font.BOLD, 24));
        next.setAlignmentX(Component.LEFT_ALIGNMENT);
        next.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(idx<4){
                    idx++;
                    label.setIcon(pages[idx]);
                }
            }
        });
        previous = new JButton("Previous");
        previous.setFont(new Font("Arial", Font.BOLD, 24));
        previous.setAlignmentX(Component.LEFT_ALIGNMENT);
        previous.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(idx>0){
                    idx--;
                    label.setIcon(pages[idx]);
                }
            }
        });
        add(label);
        panel.add(previous);
        panel.add(next);
        add(panel);
        //pack();
    }
}

