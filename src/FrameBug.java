import javax.swing.*;
import java.awt.event.*;

public class FrameBug {

    public static void main(String[] args)  throws Exception {
        System.out.println(System.getProperty("sun.java2d.opengl"));
        System.out.println(System.getProperty("sun.java2d.d3d"));
        SwingUtilities.invokeAndWait(FrameBug::go);
    }

    public static void go() {
        final JFrame frame = new JFrame();
        JButton button = new JButton("Open Frame");
        frame.getContentPane().add(button);
        button.addActionListener((ActionEvent e) -> {
                        JFrame f = new JFrame();
                        f.setSize(400, 400);
                        f.setVisible(true);
                        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                    });
        frame.setSize(500, 500);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }
}
