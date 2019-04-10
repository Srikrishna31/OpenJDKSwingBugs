/*
JDK-8222250: JLabel with an html img src not working anymore.
 */
import java.awt.BorderLayout;
import java.awt.Font;
import java.lang.reflect.InvocationTargetException;

import javax.swing.SwingUtilities;
import javax.swing.JLabel;
import javax.swing.JFrame;

public class HtmlImgSrcTest
{
    public static void main(String[] args) throws InvocationTargetException, InterruptedException
    {
        SwingUtilities.invokeAndWait(() -> {
            JFrame frame = new JFrame();
            frame.setLayout(new BorderLayout());
            JLabel label = new JLabel(
                    "<html>"
                            + "Hello "
                            + "<img src=\"https://media.smau.it/x-exhibition/upload/partner/2014/06/12/oracle2014.jpg\">"
                            + " World</html>");
            label.setFont(new Font("Tahoma", Font.PLAIN, 30));
            frame.add(label, BorderLayout.CENTER);
            frame.setBounds(100, 100, 700, 300);
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setVisible(true);
        });
    }
}