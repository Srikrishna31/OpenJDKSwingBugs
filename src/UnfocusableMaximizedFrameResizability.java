import java.awt.Toolkit;
import java.awt.Frame;
import java.awt.Rectangle;
import java.awt.AWTException;
import java.awt.event.InputEvent;
import java.awt.Robot;

public class UnfocusableMaximizedFrameResizability {

    private static Frame frame;
    private static Robot robot;
    private static boolean isProgInterruption = false;
    private static Thread mainThread = null;
    private static int sleepTime = 300000;

    private static void createAndShowFrame() {

        //The MAXIMIZED_BOTH state is not supported by the toolkit. Nothing to test.
        if (!Toolkit.getDefaultToolkit().isFrameStateSupported(Frame.MAXIMIZED_BOTH)) {
            return;
        }

        System.out.println("test starts");
        // Create the maximized unfocusable frame
        frame = new Frame("Unfocusable frame");
        frame.setMaximizedBounds(new Rectangle(0, 0, 300, 300));
        frame.setSize(200, 200);
        frame.setVisible(true);
        frame.setExtendedState(Frame.MAXIMIZED_BOTH);
        frame.setFocusableWindowState(false);

        try {
            robot = new Robot();
        } catch (AWTException e) {
            throw new RuntimeException("Robot creation failed");
        }
        //delay is added for the frame to set maximized bounds
        robot.delay(2000);

        System.out.println("Final frame bounds 0: " + frame.getBounds());
        // The initial bounds of the frame
        final Rectangle bounds = frame.getBounds();
        System.out.println("Final frame bounds 1: " + frame.getBounds());

        // Let's move the mouse pointer to the bottom-right coner of the frame (the "size-grip")
        robot.mouseMove(bounds.x + bounds.width - 2, bounds.y + bounds.height - 2);
        robot.waitForIdle();

        // ... and start resizing
        robot.mousePress(InputEvent.BUTTON1_DOWN_MASK);
        robot.waitForIdle();
        System.out.println("Final frame bounds 2: " + frame.getBounds());
        robot.mouseMove(bounds.x + bounds.width + 20, bounds.y + bounds.height + 15);
        robot.waitForIdle();
        System.out.println("Final frame bounds 3: " + frame.getBounds());

        robot.mouseRelease(InputEvent.BUTTON1_DOWN_MASK);
        robot.waitForIdle();

        // The bounds of the frame after the attempt of resizing is made
        final Rectangle finalBounds = frame.getBounds();
        System.out.println("Final frame bounds: " + frame.getBounds());

        if (!finalBounds.equals(bounds)) {
            cleanup();
            throw new RuntimeException("The maximized unfocusable frame can be resized.");
        }
        cleanup();
    }

    private static void cleanup() {
        frame.dispose();
        isProgInterruption = true;
        mainThread.interrupt();
    }

    public static void main(String args[]) throws InterruptedException {

        mainThread = Thread.currentThread();

        try {
            System.out.println("try called");
            createAndShowFrame();
            mainThread.sleep(sleepTime);
        } catch (InterruptedException e) {
            System.out.println("catch exception");
            if (!isProgInterruption) {
                throw e;
            }
        }

        if (!isProgInterruption) {
            System.out.println("last exception");
            throw new RuntimeException("Timed out after " + sleepTime / 1000
                    + " seconds");
        }
    }
}

