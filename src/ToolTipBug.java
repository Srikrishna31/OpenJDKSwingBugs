/*
JDK-8196132
 */
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class ToolTipBug {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(
                new Runnable() {
                    public void run() {
                        start();
                    }
                });
    }

    public static void start() {
        JFrame f = new JFrame();
        Container c = f.getContentPane();
        c.setLayout(new GridLayout(2, 1));
        JPanel p = new JPanel();
        p.setToolTipText("<html>This large tooltip makes it<br>"
                + "easier<br>to<br>reproduce<br>the<br>problem</html");
        c.add(p);
        c.add(new JButton("Test"));
        f.setSize(400, 400);
        f.setVisible(true);
    }
} 