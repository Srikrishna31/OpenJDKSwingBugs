import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.GraphicsConfiguration;
import java.awt.geom.AffineTransform;

public class HiDpiScaleFactor {
    public static void main(String[] args) {

        GraphicsDevice graphicsDevice = GraphicsEnvironment
                .getLocalGraphicsEnvironment()
                .getDefaultScreenDevice();
        GraphicsConfiguration graphicsConfig = graphicsDevice
                .getDefaultConfiguration();

        AffineTransform tx = graphicsConfig.getDefaultTransform();
        double scaleX = tx.getScaleX();
        double scaleY = tx.getScaleY();

        System.out.printf("scaleX: %f, scaleY: %f\n", scaleX, scaleY);
    }
}
