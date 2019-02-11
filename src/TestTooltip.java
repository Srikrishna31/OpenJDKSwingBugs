/*
JDK-8218674
 */

import java.awt.BorderLayout;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.ToolTipManager;
import javax.swing.WindowConstants;
import javax.swing.SwingUtilities;
public class TestTooltip {

    public static void main(String[] args) throws Exception {
        SwingUtilities.invokeAndWait(() -> {
            JFrame frame = new JFrame("Tooltip test");
            JPanel pane = new JPanel(new BorderLayout());
            JTextField textfield = new JTextField("text with tooltip");
            textfield.setEditable(false);
            ToolTipManager.sharedInstance().setInitialDelay(100);
            ToolTipManager.sharedInstance().setReshowDelay(100);
            textfield.setToolTipText("<html><img src=\"https://pbs.twimg.com/profile_images/1145865357/openjdk_400x400.png\"></html>");
            pane.add(textfield);
            frame.add(pane);
            frame.setSize(800, 600);
            frame.setLocationRelativeTo(frame);
            frame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
            frame.setVisible(true);
        });
    }
} 
