
/*
 * JavaBug.java
 *
 * Created on July 30, 2002, 10:33 AM
 */

import java.awt.image.*;
import java.awt.geom.AffineTransform;
import java.awt.color.ColorSpace;
import javax.swing.JFrame;
import java.awt.Graphics2D;
import java.awt.Graphics;

/**
 *
 * @author Shawn Bisgrove
 */
public class ImageTransformBug extends JFrame {
    final BufferedImage bi;
    final javax.swing.JPanel jp;
    /** Creates a new instance of JavaBug */
    public ImageTransformBug() {

        short[] s = new short[30 * 30 * 30]; // 30 height, 30  width, 30 bands
        for (int i = 0; i < 30*30*30; ++i) {
            s[i] = (short)(5000 * Math.random());
        }
        DataBufferShort db = new DataBufferShort(s, s.length);
        ComponentSampleModel pism = new PixelInterleavedSampleModel
                (DataBuffer.TYPE_SHORT,30,30, 30,30 * 30, new int[]{ 5, 8, 9} );
        WritableRaster wr = Raster.createWritableRaster(pism, db, null);

        ComponentColorModel ccm = new ComponentColorModel(
                ColorSpace.getInstance(ColorSpace.CS_sRGB),
                (int[])null,false, false, ColorModel.OPAQUE,
                DataBuffer.TYPE_SHORT);

        bi = new BufferedImage( ccm, wr, false, null);
        jp = new javax.swing.JPanel() {
            public void paint(Graphics g) {
                System.out.println("Paint being called");
                AffineTransform at = AffineTransform.getScaleInstance((double)
                        0.5, (double)0.5);
                ((Graphics2D)g).drawImage(bi, at, null);
            }
        };


        getContentPane().add(jp);

    }


    public static void main(final String[] args) {
        ImageTransformBug jb = new ImageTransformBug();
        jb.setSize(500,500);
        jb.setDefaultCloseOperation(jb.DISPOSE_ON_CLOSE);
        jb.show();

    }

}
