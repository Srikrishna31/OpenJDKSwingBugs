/*
JDK-8221729
 */
import java.awt.Container;
import java.awt.FlowLayout;
import javax.swing.JFrame;
import javax.swing.JTextField;
import javax.swing.JLabel;
import javax.swing.SwingUtilities;

public class TestAccessibilityApp extends JFrame{

    //Some UI components
    private JTextField inputText1,inputText2;
    private JLabel txtInputLbl;
    private JLabel standaloneLbl;

    public TestAccessibilityApp() {

        super("JAWS Test App");

        //Set the layout
        Container currentContainer = getContentPane();
        currentContainer.setLayout(new FlowLayout());

        buildUI(currentContainer);

        setSize(640,480);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setVisible(true);
    }

    private void buildUI(Container targetContainer)
    {
        //Assemble the UI
        inputText1 = new JTextField(30);
        inputText2 = new JTextField(30);
        txtInputLbl = new JLabel("Some label for input Text field:");
        txtInputLbl.setLabelFor(inputText1);

        //Add the text field
        targetContainer.add(txtInputLbl);
        targetContainer.add(inputText1);

        //Add the standalone label
        standaloneLbl = new JLabel("This is static text from standalone label");
        targetContainer.add(standaloneLbl);
        targetContainer.add(inputText2);
    }

    public static void main(String[] args) throws Exception {
        SwingUtilities.invokeAndWait(TestAccessibilityApp::new);
    }
}
