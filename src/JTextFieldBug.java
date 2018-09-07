
/*
JDK-8194733
 */
import java.awt.ComponentOrientation;
import javax.swing.JTextField;
public class JTextFieldBug extends javax.swing.JFrame {

    public static void main(String[] args) {
        JTextFieldBug frame = new JTextFieldBug();
        frame.show();
    }

    public JTextFieldBug() {
        javax.swing.JTextField textField = new JTextField();

        textField.setFont(new java.awt.Font("Tahoma", 0, 24)); // NOI18N
        textField.setText("\u0635\u0646\u062F\u0648\u0642 \u06F4\u06F0×\u06F3\u06F0 \u067E\u0627\u06CC\u0647 \u062F\u0627\u0631 \u0648\u0627\u06CC\u0631\u0646\u06AF \u0645\u06CC\u062A\u0631 \u062A\u06A9 \u0641\u0627\u0632");
        textField.setComponentOrientation(ComponentOrientation.RIGHT_TO_LEFT);
        textField.setHorizontalAlignment(JTextField.RIGHT);

        getContentPane().add(textField);
        pack();
        this.setLocationRelativeTo(null); //enusre get showed at screen center
        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
    }
}
