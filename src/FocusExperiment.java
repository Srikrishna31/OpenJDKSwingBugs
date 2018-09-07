
import javax.swing.*;
//import java.awt.event.WindowAdapter;
import java.awt.Dimension;
//import java.awt.event.WindowEvent;

public class FocusExperiment extends JDialog{
    private static JSpinner m_Spinner;
    private static JButton m_Button;
    public FocusExperiment() {
        super(new JFrame(), false);
    }
    public static void main(String[] args)  throws Exception {
        UIManager.LookAndFeelInfo[] lafs = UIManager.getInstalledLookAndFeels();
        for (UIManager.LookAndFeelInfo laf : lafs) {
            if (laf.getClassName().contains("metal")) {
                UIManager.setLookAndFeel(laf.getClassName());
            }
        }
        m_Spinner = new JSpinner();
        m_Spinner.setMaximumSize(new Dimension(100, 100));
        m_Spinner.setBorder(null);
        m_Button = new JButton("Test");
        m_Button.setMaximumSize(new Dimension(100, 50));
        SwingUtilities.invokeAndWait(() -> {
            FocusExperiment d = new FocusExperiment();
            d.addWindowListener(new java.awt.event.WindowAdapter() {
                @Override
                public void windowClosing(java.awt.event.WindowEvent e) {
                    System.exit(0);
                }
            });
            d.add(m_Spinner);
            //d.add(m_Button);
            d.setSize(640,480);
            //d.pack();
            d.setVisible(true);
        });
    }
}
