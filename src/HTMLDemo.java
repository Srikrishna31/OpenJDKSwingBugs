/*
JDK-7052268
 */

import javax.swing.*;

public class HTMLDemo {
    public static void main(String[] args) {
        JEditorPane html = new JEditorPane("text/html",
                "<html><body><FORM ACTION=\"examplescript.cgi\"><INPUT type=\"submit\" value=\"Submit\"></FORM></BODY></HTML>");
        html.setEditable(false);

        JFrame f = new JFrame();
        f.getContentPane().add(html);
        f.setSize(200,100);
        f.setDefaultCloseOperation(f.EXIT_ON_CLOSE);
        f.setVisible(true);

        // Uncomment this line to see the Exception on JDK 7
        SwingUtilities.updateComponentTreeUI(html);
    }
}