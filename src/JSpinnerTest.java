import javax.swing.*;
import java.awt.event.*;
import java.awt.*;

public class JSpinnerTest extends javax.swing.JPanel {

    /** Creates new form BuoyancyEditor */
    private javax.swing.JSpinner x;

    public JSpinnerTest() {
    }

    public static void display(String name, Component c) {
        JFrame	frame	= new JFrame(name);
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.getContentPane().add(c);
        frame.pack();
        frame.setVisible(true);
    }

    public JSpinnerTest bug() {
        x = new javax.swing.JSpinner();

        setLayout(new javax.swing.BoxLayout(this, javax.swing.BoxLayout.Y_AXIS));

        /* Initializing editor before model */
        x.setEditor(new JSpinner.NumberEditor(x, "#,##0.000"));
        x.setModel(new SpinnerNumberModel(0.0, 0.0, 1.0, 0.001));

        x.setValue(new Double(0.0));
        add(x);

        return this;
    }

    public JSpinnerTest works() {
        x = new javax.swing.JSpinner();

        setLayout(new javax.swing.BoxLayout(this, javax.swing.BoxLayout.Y_AXIS));

        /* Initializing model before editor */
        x.setModel(new SpinnerNumberModel(0.0, 0.0, 1.0, 0.001));
        x.setEditor(new JSpinner.NumberEditor(x, "#,##0.000"));

        x.setValue(new Double(0.0));
        add(x);

        return this;
    }

    public static void main(String[] args) {
        display("Bug", new JSpinnerTest().bug());
        display("Works", new JSpinnerTest().works());
    }
}

