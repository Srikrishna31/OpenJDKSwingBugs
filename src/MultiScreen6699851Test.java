/**
 * Created by kaddepal on 7/14/2017.
 * Bug: 8176359
 */
import java.awt.event.ActionEvent;
import java.awt.GraphicsEnvironment;
import java.awt.GraphicsConfiguration;
import java.awt.GraphicsDevice;
import java.awt.Rectangle;

import javax.swing.AbstractAction;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextPane;
import javax.swing.border.EmptyBorder;
import javax.swing.SwingUtilities;

public class MultiScreen6699851Test extends JFrame
{
    public static void main(String[] args) throws Exception
    {
        SwingUtilities.invokeAndWait(() -> {
                try
                {
                    JFrame.setDefaultLookAndFeelDecorated(true);
                    new MultiScreen6699851Test();
                }
                catch(Exception e)
                {
                    e.printStackTrace();
                }
        });
    }

    public MultiScreen6699851Test() throws Exception
    {
        JPanel panel = new JPanel();
        BoxLayout layout = new BoxLayout(panel, BoxLayout.PAGE_AXIS);
        panel.setLayout(layout);
        panel.setBorder(new EmptyBorder(10,10,10,10));

        GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
        GraphicsConfiguration dgc = ge.getDefaultScreenDevice().getDefaultConfiguration();
        System.err.println("Main-Screen bounds: " + dgc.getBounds());

        final GraphicsDevice[] gs = ge.getScreenDevices();

        for (int i = 0; i < gs.length; i++)
        {
            final int screen = i;
            JButton button = new JButton(new AbstractAction("Create window for screen " + i){
                public void actionPerformed(ActionEvent evt)
                {
                    createFrame4Screen(gs, screen);
                }
            });
            panel.add(button);
            panel.add(Box.createVerticalStrut(10));
            System.out.println(gs[i].getDefaultConfiguration().getBounds());
        }
        add(panel);

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setTitle(this.getClass().getSimpleName());
        pack();
        setLocationRelativeTo(null);
        setVisible(true);
    }

    private void createFrame4Screen(GraphicsDevice[] gs, int i)
    {
        JFrame f = new JFrame(gs[i].getDefaultConfiguration());
        GraphicsConfiguration gc = f.getGraphicsConfiguration();
        Rectangle maxBounds = gc.getBounds();


        //weird - decreasing height leads to increased frame size
        //only if screen resolution of screen 0 is less than screen 1
        //and screen 1 is the main screen
        //or if screen resolution of screen 1 is less than screen 0
        //and screen 0 is the main screen
//        maxBounds.height -= 10;
//        maxBounds.x = -maxBounds.x;
//        maxBounds.y = -maxBounds.y;
//
//        maxBounds.x=0;
//        maxBounds.y = 0; //Need to figure out why y is coming out as 34
//        maxBounds.y = -30;

//        if (i == 1) {
//            maxBounds.height = 1700;
//            maxBounds.width = 2100;
//        }
        f.setMaximizedBounds(maxBounds);
        f.setTitle("Screen " + i + " " + maxBounds);
        f.setExtendedState(JFrame.MAXIMIZED_BOTH);
        f.setVisible(true);
        f.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        System.out.println("Location of the window on screen: "+f.getLocationOnScreen());
        System.out.println("Size of the window: " + f.getSize());
        JTextPane textPane= new JTextPane();
        textPane.setText("Frame bounds: " + f.getBounds() + "\n");

        System.out.println("Device bounds: " + maxBounds + "\n");
        System.out.println("Frame bounds: " + f.getBounds() + "\n");
        JScrollPane scroller = new JScrollPane(textPane);
        f.getContentPane().add(scroller);
    }
}
