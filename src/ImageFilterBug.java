/*
JDK-4263652
 */
import java.awt.*;
import java.awt.event.*;
import java.awt.image.*;
import java.util.*;

public class ImageFilterBug extends Frame {
    static ImageFilterBug MyInstance;
    private Image img= null;
    public int nfiltering = 0;

    public ImageFilterBug(String imgfilename) {
        super("Please click on client area and view the console to illustrate the bug. Each click filters the image once");
        setSize(480, 480);

        img = getToolkit().createImage(imgfilename);

        this.addMouseListener(new MouseAdapter() {
            public void mousePressed(MouseEvent e) {
                ImageProducer ip = ImageFilterBug.this.img.getSource();
                DummyFilter filter = new DummyFilter();
                Image filteredImage = null;

                // Please note that every filtering process creates a
                // new DummyFilter and FilteredImageSource objects.

                filteredImage = createImage(new FilteredImageSource(ip, filter));
                ImageFilterBug.this.img = filteredImage;

                ImageFilterBug.this.repaint();

                ++nfiltering;
            }
        });

        show();

    }


    public void paint(Graphics g) {
        g.drawImage(img, 0, 0, this);
    }

    public static void main(String[] Args) {
        if(Args.length != 1) {
            System.out.println("Usage: java ImageFilterBug image_file");
            System.exit(1);
        }
        else
            ImageFilterBug.MyInstance = new ImageFilterBug(Args[0]);
    }

}


class DummyFilter extends ImageFilter {
    private static int s = 0;
    private int n = 0;

    public void setProperties(Hashtable props) {
        System.out.println("setProperties(): Number Of Calls: instance counter = " + (++n) + ", static counter = " + (++s) + "(should be " + ImageFilterBug.MyInstance.nfiltering + ")");

        super.setProperties(props);
    }

}