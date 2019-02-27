/*
JDK-8218917
 */
import javax.swing.JTextField;
import javax.swing.JFrame;
import javax.swing.SwingUtilities;

import java.awt.event.KeyListener;
import java.awt.event.KeyEvent;

public class AltKeyBug {

    public static void main(String[] args) throws Exception {
        SwingUtilities.invokeAndWait(() -> {
            JTextField comp = new JTextField();
            comp.addKeyListener(new KeyListener() {
                @Override public void keyTyped(KeyEvent e) {}
                @Override public void keyPressed(KeyEvent e) {
                    System.out.println("ModEx : " +e.getModifiersEx());
                    System.out.println("Mod : " +e.getModifiers());
                    System.out.println("ALT_DOWN : " + e.isAltDown());
                    System.out.println("ALT_GR_DOWN: " + e.isAltGraphDown());
                    System.out.println("-----------");
                }
                @Override public void keyReleased(KeyEvent e) {}
            });
            JFrame f = new JFrame();
            f.add(comp);
            f.setSize(100,100);
            f.setVisible(true);
        });
    }
}
