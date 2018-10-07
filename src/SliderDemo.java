/*
JDK-7124293
 */

import javax.swing.JFrame;
import javax.swing.JSlider;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

public class SliderDemo extends JFrame{
    public SliderDemo() {
        JSlider slider = new JSlider(JSlider.HORIZONTAL, 0, 50, 25);
        JPanel panel=new JPanel();
        panel.add(slider);
        add(panel);
    }

    public static void main(String s[]) throws Exception {
        SliderDemo frame=new SliderDemo();
        SwingUtilities.invokeAndWait(() -> {
            frame.pack();
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setVisible(true);
        });
    }
}
