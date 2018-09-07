import javax.swing.*;

public class AccessibleJTableTest {

    private static void runTest() {
        JFrame frame = new JFrame("AccessibilityTest");
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        JTable table = new JTable(
                new String[][]{{"ONE", "TWO"}, {"FIVE", "SIX"},
                        {"MORE", "LESS"}}, new String[]{"Cleanup", "Run"});
        table.getAccessibleContext().setAccessibleName("Hello funny table");
        frame.getContentPane().add(new JScrollPane(table));
        frame.pack();
        frame.setVisible(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(AccessibleJTableTest::runTest);
    }
} 
