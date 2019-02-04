
/*
Testing JDK-8061381
 */
import java.awt.event.ActionEvent;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.KeyStroke;
import javax.swing.SwingUtilities;

public class JMenuItemAcceleratorTest {

    public JMenuItemAcceleratorTest() {
        initComponents();
    }

    public static void main(String[] args) throws Exception {
        //create Swing components on EDT
        SwingUtilities.invokeAndWait(JMenuItemAcceleratorTest::new);
    }

    private void initComponents() {
        JFrame frame = new JFrame("JDK-8028241");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JMenuBar menuBar = new JMenuBar();
        JMenu menu = new JMenu("File");
        menu.setMnemonic(KeyEvent.VK_F);
        menuBar.add(menu);

        JMenuItem menuItemA = new JMenuItem("Alpha");
        menuItemA.setMnemonic(KeyEvent.VK_A);
        menuItemA.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_A, InputEvent.CTRL_MASK));
        menuItemA.addActionListener((ActionEvent actionEvent) ->
                System.out.println("Alpha"));

        JMenuItem menuItemF1 = new JMenuItem("F1");
        menuItemF1.setMnemonic(KeyEvent.VK_1);
        menuItemF1.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F1, InputEvent.CTRL_MASK));
        menuItemF1.addActionListener((ActionEvent actionEvent) ->
                System.out.println("F1"));

        JMenuItem menuItemB = new JMenuItem("Bravo");
        menuItemB.setMnemonic(KeyEvent.VK_B);
        menuItemB.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_B,
                InputEvent.CTRL_MASK + InputEvent.SHIFT_MASK));
        menuItemB.addActionListener((ActionEvent actionEvent) ->
                System.out.println("Bravo"));

        JMenuItem menuItemF2 = new JMenuItem("F2");
        menuItemF2.setMnemonic(KeyEvent.VK_2);
        menuItemF2.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F2,
                InputEvent.CTRL_MASK + InputEvent.SHIFT_MASK));
        menuItemF2.addActionListener((ActionEvent actionEvent) ->
                System.out.println("F2"));

        JMenuItem menuItemC = new JMenuItem("Charlie");
        menuItemC.setMnemonic(KeyEvent.VK_C);
        menuItemC.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_C,
                InputEvent.CTRL_MASK
                        + InputEvent.ALT_MASK
                        + InputEvent.SHIFT_MASK));
        menuItemC.addActionListener((ActionEvent actionEvent) ->
                System.out.println("Charlie"));

        JMenuItem menuItemF3 = new JMenuItem("F3");
        menuItemF3.setMnemonic(KeyEvent.VK_3);
        menuItemF3.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F3,
                InputEvent.CTRL_MASK
                        + InputEvent.ALT_MASK
                        + InputEvent.SHIFT_MASK));
        menuItemF3.addActionListener((ActionEvent actionEvent) ->
                System.out.println("F3"));

        JMenuItem menuItemDel = new JMenuItem("Del");
        menuItemDel.setMnemonic(KeyEvent.VK_DELETE);
        menuItemDel.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_DELETE,
                InputEvent.CTRL_MASK
                        + InputEvent.ALT_MASK
                        + InputEvent.SHIFT_MASK));
        menuItemDel.addActionListener((ActionEvent actionEvent) ->
                System.out.println("Delete"));

        JMenuItem menuItemExit = new JMenuItem("Exit");
        menuItemExit.setMnemonic(KeyEvent.VK_X);
        menuItemExit.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_X, InputEvent.CTRL_MASK));
        menuItemExit.addActionListener((ActionEvent actionEvent) -> {
                System.out.println("Bye Bye");
                System.exit(0);
        });

        menu.add(menuItemA);
        menu.add(menuItemF1);
        menu.add(menuItemB);
        menu.add(menuItemF2);
        menu.add(menuItemC);
        menu.add(menuItemF3);
        menu.add(menuItemDel);
        menu.add(menuItemExit);

        frame.setJMenuBar(menuBar);
        frame.pack();
        frame.setVisible(true);
    }
}
