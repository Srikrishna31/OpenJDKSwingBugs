import javax.swing.JFrame;
import javax.swing.JRadioButton;
import javax.swing.ButtonGroup;
import javax.swing.SwingUtilities;
import javax.swing.WindowConstants;

public class RadioButtonBug {

    RadioButtonBug() {
        JFrame f = new JFrame();

        JRadioButton r1 = new JRadioButton("1");
        JRadioButton r2 = new JRadioButton("2");
        JRadioButton r3 = new JRadioButton("3");

        r1.setBounds(75, 50, 100, 30);
        r2.setBounds(75, 100, 100, 30);
        r3.setBounds(75, 150, 100, 30);

        ButtonGroup bg = new ButtonGroup();
        bg.add(r1); bg.add(r2); bg.add(r3);

        f.add(r1); f.add(r2); f.add(r3);
        f.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        f.setSize(300, 300);
        f.setVisible(true);

        System.out.println(f.getFocusTraversalPolicy());
    }

    public static void main(String[] args) throws Exception {
        SwingUtilities.invokeAndWait(() -> new RadioButtonBug());
    }
}
