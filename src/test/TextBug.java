/*
Bug: JDK-6927431
 */
package test;
import javax.swing.*;
import java.awt.*;
import java.util.Locale;

public class TextBug extends JFrame {
    TextBug() {
        getContentPane().setLayout(new GridLayout(2,2));
        // Thai
        TextArea area = new TextArea(" \u0E02\u0E01\u0E34\u0E03 ");
        area.setEditable(true);
        area.setFont(new Font("Lucida Sans regular",Font.PLAIN,24));
        getContentPane().add(area);
        JTextArea ta = new JTextArea(" \u0E02\u0E01\u0E34\u0E03 ", 10,3);
        ta.setFont(new Font("Lucida Sans regular",Font.PLAIN,24));
        ta.setEditable(true);
        getContentPane().add(ta);
        // Latin
        area = new TextArea(" \u0062\u0061\u0308\u0063 ");
        area.setEditable(true);
        area.setFont(new Font("Lucida Sans regular",Font.PLAIN,24));
        getContentPane().add(area);
        ta = new JTextArea(" \u0062\u0061\u0308\u0063 ", 10, 3);
        ta.setFont(new Font("Lucida Sans regular",Font.PLAIN,24));
        ta.setEditable(true);
        getContentPane().add(ta);
    }

    public static void main(String[] args) {
        JFrame f= new TextBug();
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        f.pack();
        f.setSize(400,400);
        f.setVisible(true);
    }
}

