import java.awt.BorderLayout;
import java.awt.Dimension;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

public class SwingTextFieldTest {

    public SwingTextFieldTest() {
        final JFrame frame = new JFrame("SwingTextFieldTest");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout());

        JPanel swingPanel = new JPanel();
        swingPanel.setLayout(new BorderLayout());
        swingPanel.setPreferredSize(new Dimension(300, 200));
        frame.getContentPane().add(swingPanel, BorderLayout.CENTER);

        JTextField swingTextField = new JTextField(15);

        swingPanel.add(swingTextField, BorderLayout.NORTH);

        // show frame
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new SwingTextFieldTest());
    }
}
