import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import java.awt.Graphics2D;
import java.awt.Graphics;
import java.awt.Color;
import java.awt.geom.Line2D;

public class MetalJFrame {
    public static void main(String[] args) throws Exception {
        SwingUtilities.invokeAndWait(() -> {
            JFrame myFrame = new JFrame("Metal Frame");
            myFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            myFrame.setSize(300, 300);
            myFrame.add(new JPanel(){
                @Override
                public void paint(Graphics g) {
                    Graphics2D g2 = (Graphics2D)g;
                    g2.setColor(Color.GREEN);
                    g2.draw(new Line2D.Double(180,100,20,230));
                }
            });
            myFrame.setVisible(true);
        });
    }
}
