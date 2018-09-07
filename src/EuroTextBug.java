/*
Bug: JDK-8186414
 */
import javax.swing.*;
import javax.swing.text.StyledEditorKit;
import java.awt.*;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.logging.Level;
import java.util.logging.Logger;

public class EuroTextBug {

    public EuroTextBug() {
        JFrame frame = new JFrame();
        JTextPane pane = new JTextPane();
        JButton button = new JButton("Button");

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        pane.setPreferredSize(new Dimension(200, 200));
        frame.add(pane);
//        frame.add(button);
        frame.pack();
        frame.setVisible(true);
    }

    public static void main(String[] args) {

        new EuroTextBug();
        //fixEuroCharForANSIEncoding();
    }
    private static void fixEuroCharForANSIEncoding() {
        try {
            final Class<?> rtfReaderClass = Class.forName("javax.swing.text.rtf.RTFReader");
            final Method getCharacterSetMethod = rtfReaderClass.getDeclaredMethod("getCharacterSet", String.class);
            getCharacterSetMethod.setAccessible(true);
            final char[] charSet = (char[]) getCharacterSetMethod.invoke(null, "ansi");
            assert (charSet[128] == 8364); // 8364 == '€'
        } catch (ClassNotFoundException | NoSuchMethodException | SecurityException | IllegalAccessException
                | IllegalArgumentException | InvocationTargetException e) {
            Logger.getLogger(StyledEditorKit.class.getName()).log(Level.SEVERE, e.getMessage(), e);
        }
    }
}
