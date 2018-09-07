/**
 * Created by kaddepal on 7/12/2017.
 */

/**
 *class: AnimationSwing
 * version: 0.9
 * Created by kaddepalli on 4/14/2017.
 * Copyright: confidential
 */
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;

import javax.swing.filechooser.FileSystemView;

class UI extends JFrame
{
    private float angle = 0.0f;
    private Timer timer;
    private BufferedImage backBuffer;
    private Graphics2D backGraphics;
    static final int ANIMATION_TIME = 30; //Time in seconds
    static final int DELAY = 35; //Time in milliseconds
    static final float ANGLE_INCR = 360.0f /
            (ANIMATION_TIME  * 1000.0f / DELAY);
    private float totalCallbacks = 0.0f;

    UI()
    {
        super("Animation");
        setSize(600, 600);
        setLayout(null);
        setResizable(false);
        setVisible(true);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        ActionListener al = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent arg0)
            {
                angle += ANGLE_INCR;
                totalCallbacks += DELAY / 1000.0f;
                timer.setRepeats(totalCallbacks < ANIMATION_TIME);
                repaint();
            }
        };
        timer = new Timer(DELAY, al);
        timer.start();
    }

    public void paint(Graphics g)
    {
        if (backBuffer == null)
        {
            backBuffer = new BufferedImage(getWidth(),
                    getHeight(),
                    BufferedImage.TYPE_INT_RGB);

            backGraphics = backBuffer.createGraphics();
            float dash1[] = {10.0f};
            BasicStroke stroke = new BasicStroke(10.0f,
                    BasicStroke.CAP_BUTT,
                    BasicStroke.JOIN_BEVEL,
                    10.0f,
                    dash1,
                    0.0f );
            backGraphics.setStroke(stroke);
        }

        int width = backBuffer.getWidth();
        int height = backBuffer.getHeight();
        backGraphics.setColor(Color.WHITE);
        backGraphics.fillRect(0, 0, width, height);

        backGraphics.setColor(Color.RED);
        AffineTransform saveXForm = backGraphics.getTransform();
        backGraphics.rotate(Math.toRadians(angle), width / 2, height / 2);
        backGraphics.drawOval(100, 200, 400, 200);

        backGraphics.setTransform(saveXForm);

        g.drawImage(backBuffer, 0,0,this);
    }
}

public class Main
{
    public static void main(String[] args)
    {
        new UI();
    }
}
