/*
JDK-6699851
 */
import java.awt.GraphicsConfiguration;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.Insets;
import java.awt.Rectangle;

import javax.swing.JFrame;

public class TestFrame extends JFrame{

    public TestFrame(GraphicsConfiguration gc) {
        super(gc);
    }
    @Override
    public synchronized void setExtendedState(int state) {
        if((state & MAXIMIZED_BOTH) !=0){
            Insets insets = getToolkit().getScreenInsets(getGraphicsConfiguration());

            Rectangle bounds = getGraphicsConfiguration().getBounds();
            System.out.println("--------------");
            System.out.println(getGraphicsConfiguration());
            System.out.println(bounds);
            bounds.x = insets.left;
            bounds.y = insets.top;
            bounds.width -= Math.abs(insets.left - insets.right);
            bounds.height -= Math.abs(insets.top - insets.bottom);
            System.out.println(bounds);
            setMaximizedBounds(bounds);
        }
        super.setExtendedState(state);
    }
    public static void main(String args[]){
        GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
        GraphicsDevice gd[] = ge.getScreenDevices();
        TestFrame.setDefaultLookAndFeelDecorated(true);

        for (int i = 0; i < gd.length; i++) {
            TestFrame frame = new TestFrame(gd[i].getDefaultConfiguration());
            Rectangle bounds = frame.getGraphicsConfiguration().getBounds();
            frame.setBounds(bounds.x+bounds.width/4, bounds.y+bounds.height/4, bounds.width/2, bounds.height / 2);
            frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setVisible(true);
        }
    }
}
