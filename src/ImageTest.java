import javax.swing.JEditorPane;
import javax.swing.JFrame;
import javax.swing.SwingUtilities;
import javax.swing.WindowConstants;
import java.awt.*;
import javax.imageio.ImageIO;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.concurrent.CountDownLatch;

public class ImageTest {
     private static final String ABSOLUTE_FILE_PATH =
            "D:\\Code_Base\\jdklatest\\client\\open\\test\\jdk\\javax\\swing\\JEditorPane\\8195095\\anotherCircle.png";


     private static BufferedImage actual;

     static {
         Toolkit tk = Toolkit.getDefaultToolkit();
         try {
             actual = ImageIO.read(new File(ABSOLUTE_FILE_PATH));
         } catch (IOException e) {
             System.out.println("Image could not be loaded.");
         }
     }
    public static void main(String[] args) throws Exception{
        Robot r = new Robot();

        CountDownLatch latch = new CountDownLatch(1);
        JEditorPane editorPane = new JEditorPane("text/html",
                "<img height=\"100\" src=\"file:///" + ABSOLUTE_FILE_PATH + "\">");
         SwingUtilities.invokeAndWait(() -> {
            JFrame f = new JFrame();
         // the following line is expected to work in the same
        //way
        //as the next (commented) line, as the image has aspect ratio 1:1
//        JEditorPane editorPane = new JEditorPane("text/html",
//                "<img width=\"200\" src=\"file:///" + ABSOLUTE_FILE_PATH + "\">");
//         JEditorPane editorPane = new JEditorPane("text/html",
//        "<img width=\"200\" height=\"200\" src=\"file:///" + ABSOLUTE_FILE_PATH +
//                "\">");
            editorPane.setEditable(false);
            f.add(editorPane);

//            editorPane.addComponentListener(new ComponentAdapter() {
//                @Override
//                public void componentShown(ComponentEvent e) {
//                    latch.countDown();
//                }
//            });
//            f.addWindowListener(new WindowAdapter() {
//                 @Override public void windowOpened(WindowEvent we) {
//                     latch.countDown();
//                 }
//             });
            f.addComponentListener(new ComponentAdapter() {
                @Override public void componentShown(ComponentEvent e) {
                        latch.countDown();
                }
            });
            f.setSize(220, 240);
            f.setLocationRelativeTo(null);
            f.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
            f.setVisible(true);
         });

         //editorPane
        r.waitForIdle();
        r.delay(1000);
        latch.await();
        Insets insets = editorPane.getInsets();
        Rectangle bounds = editorPane.getBounds();
        Point pt = editorPane.getLocationOnScreen();
        SwingUtilities.invokeAndWait(() -> {
            final Color black = Color.BLUE;
            final int offset = 10;
            Color center = r.getPixelColor(pt.x + bounds.width / 2 + insets.left, pt.y + bounds.height / 2 + insets.top);
            Color left = r.getPixelColor(pt.x + insets.left + offset, pt.y + bounds.height / 2 + insets.top);
            Color top = r.getPixelColor(pt.x + bounds.width / 2 + insets.left, pt.y + insets.top + offset);
            Color bottom = r.getPixelColor(pt.x + bounds.width / 2 + insets.left, pt.y + bounds.height + insets.top - offset);
            Color right = r.getPixelColor(pt.x + bounds.width + insets.left - offset, pt.y + bounds.height / 2 + insets.top);
            if (!(black.equals(center) || black.equals(left) || black.equals(right) || black.equals(top) || black.equals(bottom))) {
                throw new RuntimeException("Test failed: Image not rendered correctly");
            }
        });

        r.waitForIdle();
        bounds.x = pt.x;
        bounds.y = pt.y;
        BufferedImage capture = r.createScreenCapture(bounds);

        for (int i = 0; i < capture.getHeight(); i++){
            for (int j = 0; j < capture.getWidth(); j++) {
                if (capture.getRGB(i, j) != actual.getRGB(i, j)) {
                    throw new RuntimeException("Test failed: Image not rendered correctly");
                }
            }
        }
    }
}
