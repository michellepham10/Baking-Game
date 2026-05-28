import javax.swing.*;
import java.awt.*;
public class RecipeBook extends JFrame{
    String taText = "Vanilla cake batter: butter, eggs, flour, milk, sugar\n\n"+
              "Strawberry cake batter: butter, eggs, flour, milk, strawberries, sugar\n\n"+
              "Chocolate cake batter: butter, chocolate, eggs, flour, milk, sugar\n\n"+
              "Chocolate cupcake batter: butter, chocolate, eggs, flour, milk, sourcream, sugar\n\n"+
              "Cookie batter: butter, chocolate, eggs, flour, sugar\n\n";

    public RecipeBook(){
        setTitle("Recipe Book");
        setLocation(1500, 200);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        JTextArea ta = new JTextArea(15, 20);
        ta.setLineWrap(true);
        ta.setWrapStyleWord(true);
        ta.setText(taText);
        ta.setEditable(false);
        ta.setBackground(new Color(242, 222, 255));
        ta.setFont(ta.getFont().deriveFont(25f));
        JPanel panel = new JPanel();
        panel.setBackground(new Color(188, 132, 224));
        panel.add(ta);
        getContentPane().add(panel);
        pack();
        setVisible(true);
    }
}

