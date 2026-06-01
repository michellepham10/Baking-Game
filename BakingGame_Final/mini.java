import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.File;
import javax.imageio.ImageIO;

public class mini extends JPanel {

    private BufferedImage cakeImage;
    private BufferedImage frostingLayer;
    private Graphics2D frostingGraphics;

    public mini() {
        try {
            cakeImage = ImageIO.read(new File("blank_cake3.png")); // your cake image
        } catch (Exception e) {
            System.out.println("Could not load cake image");
        }

        // Create a transparent layer to draw frosting on
        frostingLayer = new BufferedImage(640, 640, BufferedImage.TYPE_INT_ARGB);
        frostingGraphics = frostingLayer.createGraphics();
        frostingGraphics.setStroke(new BasicStroke(10)); // frosting thickness
        frostingGraphics.setColor(Color.PINK);           // frosting color

        addMouseMotionListener(new MouseMotionAdapter() {
            public void mouseDragged(MouseEvent e) {
                frostingGraphics.fillOval(e.getX(), e.getY(), 100, 100);
                repaint();
            }
        });
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        g.drawImage(cakeImage, 0, 0, null);       // draw cake
        g.drawImage(frostingLayer, 0, 0, null);   // draw frosting on top
    }
}



















// import javax.swing.*;
// import java.awt.*;
// import java.awt.event.MouseAdapter;
// import java.awt.event.MouseEvent;
// import java.util.ArrayList;
// import java.util.List;

// public class mini extends JFrame {
    
//     // Store coordinates and colors of each frosting dollop
//     private static class FrostingDot {
//         int x, y;
//         Color color;

//         public FrostingDot(int x, int y, Color color) {
//             this.x = x;
//             this.y = y;
//             this.color = color;
//         }
//     }

//     private final List<FrostingDot> frostingDots = new ArrayList<>();
//     private Color currentColor = Color.PINK; // Default frosting color

//     public mini() {
//         setTitle("Cake Decorating Game");
//         setSize(600, 600);
//         setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//         setLayout(new BorderLayout());

//         // Canvas Panel
//         JPanel canvas = new JPanel() {
//             @Override
//             protected void paintComponent(Graphics g) {
//                 super.paintComponent(g);
//                 drawCake(g);
//             }
//         };

//         canvas.setBackground(new Color(255, 240, 245)); // Light pink background

//         // Add mouse listener to draw on click or drag
//         canvas.addMouseListener(new MouseAdapter() {
//             @Override
//             public void mousePressed(MouseEvent e) {
//                 addFrosting(canvas, e.getX(), e.getY());
//             }
//         });
        
//         canvas.addMouseMotionListener(new MouseAdapter() {
//             @Override
//             public void mouseDragged(MouseEvent e) {
//                 addFrosting(canvas, e.getX(), e.getY());
//             }
//         });

//         // Control Panel (Buttons)
//         JPanel controls = new JPanel();
//         JButton pinkButton = new JButton("Pink Frosting");
//         pinkButton.setBackground(Color.PINK);
//         pinkButton.addActionListener(e -> currentColor = Color.PINK);

//         JButton blueButton = new JButton("Blue Frosting");
//         blueButton.setBackground(Color.CYAN);
//         blueButton.addActionListener(e -> currentColor = Color.CYAN);

//         controls.add(pinkButton);
//         controls.add(blueButton);

//         add(canvas, BorderLayout.CENTER);
//         add(controls, BorderLayout.SOUTH);
//     }

//     private void addFrosting(JPanel canvas, int x, int y) {
//         frostingDots.add(new FrostingDot(x, y, currentColor));
//         canvas.repaint(); // Triggers paintComponent to redraw
//     }

//     private void drawCake(Graphics g) {
//         // Draw Cake Base
//         g.setColor(new Color(210, 180, 140)); // Tan/Sponge color
//         g.fillRoundRect(150, 250, 300, 200, 50, 50);

//         // Draw Frosting applied by the user
//         for (FrostingDot dot : frostingDots) {
//             g.setColor(dot.color);
//             g.fillOval(dot.x - 15, dot.y - 15, 30, 30); // Draw a 30x30 frosting star
//         }
//     }

//     // public static void main(String[] args) {
//     //     SwingUtilities.invokeLater(() -> {
//     //         mini game = new mini();
//     //         game.setVisible(true);
//     //     });
//     // }
// }
