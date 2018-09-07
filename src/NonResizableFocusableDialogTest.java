import java.awt.AWTException;
import java.awt.BorderLayout;
import java.awt.Robot;
import java.awt.event.KeyEvent;
import java.lang.reflect.InvocationTargetException;
import javax.swing.JDialog;
import javax.swing.SwingUtilities;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JScrollPane;
import javax.swing.border.EmptyBorder;
import javax.swing.JLabel;

public class NonResizableFocusableDialogTest {
    private JTextArea textArea;
    private JDialog jDialog;

    private void createDialogWithTextArea() {
        jDialog = new JDialog();
        jDialog.setTitle("JDialog with a JTextArea");
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setOpaque(false);

        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setBorder(new EmptyBorder(0, 5, 5, 0));
        JLabel topLabel = new JLabel("This is a automatic test");
        topLabel.setOpaque(false);

        topPanel.add(topLabel, BorderLayout.NORTH);
        mainPanel.add(topPanel, BorderLayout.NORTH);

        final TextPanel textPanel = new TextPanel(40);
        mainPanel.add(textPanel, BorderLayout.CENTER);
        mainPanel.setBorder(new EmptyBorder(5, 5, 5, 5));

        jDialog.add(mainPanel, BorderLayout.CENTER);
        jDialog.pack();
        jDialog.setResizable(false);
        jDialog.setVisible(true);
    }

    private void enterTextInTextArea() throws InvocationTargetException, InterruptedException {
        try {
            Robot robot = new Robot();
            robot.keyPress(KeyEvent.VK_T);
            robot.keyRelease(KeyEvent.VK_T);
            robot.waitForIdle();

            //verify if text is entered in text area, if not throw exception
            if(!"t".equals(textArea.getText())) {
                throw new RuntimeException("JTextArea is not focusable, text can't be" +
                        " entered in text area. TEST FAILED");
            }
        } catch (AWTException e) {
            throw new RuntimeException("Robot creation failed. TEST FAILED");
        } finally {
            SwingUtilities.invokeAndWait(()-> jDialog.dispose() );
        }
    }

    public static void main(String args[]) throws Exception {
        NonResizableFocusableDialogTest dialog = new NonResizableFocusableDialogTest();
        //create a JTextArea in JDialog and check if it's focusable.
        SwingUtilities.invokeAndWait(dialog::createDialogWithTextArea);
        //Try to enter text in JTextArea and check if text can be entered
        dialog.enterTextInTextArea();
    }

    private class TextPanel extends JPanel {
        TextPanel(int charactersPerLine) {
            textArea = new JTextArea(20, charactersPerLine);
            textArea.setEditable(true);
            setLayout(new BorderLayout());
            final JScrollPane textScroll = new JScrollPane(textArea,
                    JScrollPane.VERTICAL_SCROLLBAR_NEVER,
                    JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
            add(textScroll, BorderLayout.CENTER);
        }
    }
}
