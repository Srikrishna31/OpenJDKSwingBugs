
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.KeyEvent;

public class ComboBoxTest {
    private JFrame frame;
    private JComboBox<String> cb;
    private Robot robot;
    public static void main(String[] args) throws Exception {
        final ComboBoxTest test = new ComboBoxTest();
        try {
            SwingUtilities.invokeAndWait(() -> test.setupUI());
            test.test();
        } finally {
            if (test.frame != null) {
                test.frame.dispose();
            }
        }
    }

    private void setupUI() {
        frame = new JFrame();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        DefaultTableModel model = new DefaultTableModel(1, 1);
        JTable table = new JTable(model);

        cb = new JComboBox<>(new String[]{"one", "two", "three"});
        cb.setEditable(true);
        table.getColumnModel().getColumn(0).setCellEditor(new DefaultCellEditor(cb));
        frame.add(cb);

        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    private void test() throws Exception {
        robot = new Robot();
        robot.waitForIdle();
        testImpl();
        robot.waitForIdle();
        checkResult();
    }

    private void testImpl() throws Exception {
        String c = (String)cb.getSelectedItem();
        if (c.equals("One")) {
            System.out.println("Selected item equals one");
        }
    }

    private void checkResult() {
        if (cb.getSelectedItem().equals("two")) {
            System.out.println("Test passed");
        } else {
            System.out.println("Test failed");
            throw new RuntimeException("Cannot select an item " +
                    "from popup with the ENTER key.");
        }
    }
}
