/*
JDK-8207384
 */
import javax.swing.*;
import java.awt.*;

public class FileDialogAccessibilityIssue extends JFrame {
    MyPanel panel;

    public FileDialogAccessibilityIssue() {
        setTitle("This is a frame");
        setSize(300, 200);
        panel = new MyPanel(this);
        add(panel);

        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
    }

    public static void main(String[] args) {
        EventQueue.invokeLater(() -> {
            FileDialogAccessibilityIssue frame = new FileDialogAccessibilityIssue();
            frame.pack();
            frame.setVisible(true);
        });
    }

    private static class MyPanel extends JPanel {

        int dialogCounter = 1;
        final JFrame theParent;

        public MyPanel(JFrame parent) {
            super();
            theParent = parent;
            setPreferredSize(new Dimension(300, 200));
            JButton button = new JButton("Press the button");
            button.addActionListener(e -> showDialog(theParent));

            add(button);
        }

        private void showDialog(Frame parent) {
            JDialog dialog = new JDialog(parent, "This is dialog " + dialogCounter, false);
            setupDialog(dialog);
        }

        private void setupDialog(JDialog dialog) {
            JPanel dialogPanel = new JPanel();
            dialogPanel.setPreferredSize(new Dimension(300, 200));
            dialogPanel.add(new JLabel("Current dialog count: " + dialogCounter++));
            JButton button = new JButton("Open a FileDialog");
            FileDialog fileDialog = new FileDialog(theParent, "a file dialog", FileDialog.SAVE);

            button.addActionListener(e -> {
                dialog.setVisible(false);
                fileDialog.setVisible(true);
            });
            dialogPanel.add(button);
            dialog.add(dialogPanel);
            dialog.pack();
            dialog.setModal(true);
            dialog.setVisible(true);
        }
    }
}
