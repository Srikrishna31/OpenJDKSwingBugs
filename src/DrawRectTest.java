/*
JDK-6568969
 */

import java.awt.*;
import javax.swing.*;
public class DrawRectTest extends JPanel {
    public static void main(String[] args) {
        JFrame frame = new JFrame("AA test");
        frame.add(new DrawRectTest());
        frame.setPreferredSize(new Dimension(500, 500));
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }

    protected void paintComponent(Graphics g) {
        Graphics2D g2d = (Graphics2D)g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setStroke(new BasicStroke(1, BasicStroke.CAP_BUTT,
                BasicStroke.JOIN_ROUND, 1.0f,
                new float[] {10.0f, 3.0f, 3.0f, 3.0f}, 0));
        g2d.drawRect(49, 24, 2147483627, 2147483627);
    }
}