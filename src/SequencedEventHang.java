import java.awt.AWTEvent;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.lang.reflect.Constructor;
import javax.swing.JButton;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.SwingUtilities;
import javax.swing.Timer;
import sun.awt.AppContext;
import sun.awt.SunToolkit;

/*
 * Running this code causes the AWT Event Queues to be blocked on OpenJDK11
 * @author Laurent Bourges
 */
public class SequencedEventHang extends JFrame implements ActionListener {

    private static final long serialVersionUID = 1L;

    private int counter = 0;
    private JButton btn;

    public static void main(String[] args) {
        createWin(1);
        createWin(2);
    }

    private static void createWin(int tgNum) {
        ThreadGroup tg = new ThreadGroup("TG " + tgNum);

        Thread t = new Thread(tg, new Runnable() {
            public void run() {
                AppContext context = SunToolkit.createNewAppContext();

                SwingUtilities.invokeLater(new Runnable() {
                    public void run() {
                        final SequencedEventHang window = new SequencedEventHang(tgNum);
                        window.setVisible(true);

                        new Timer(10, window).start();
                    }
                });
            }
        });
        t.start();
    }

    public SequencedEventHang(final int num) {
        super("Test Window + " + num);
        setMinimumSize(new Dimension(300, 200));
        setLocation(100 + 400 * (num - 1), 100);

        setLayout(new BorderLayout());
        JLabel textBlock = new JLabel("Lorem ipsum dolor sit amet...");
        add(textBlock);

        btn = new JButton("TEST");
        btn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println("Button#" + num + " clicked: " + counter);
            }

        });
        add(btn, BorderLayout.SOUTH);

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        pack();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        AWTEvent eventOne = getSequencedEvent(WindowEvent.WINDOW_GAINED_FOCUS);
        AWTEvent eventTwo = getSequencedEvent(WindowEvent.WINDOW_LOST_FOCUS);

        btn.setText("TEST " + (counter++));

        Toolkit.getDefaultToolkit().getSystemEventQueue().postEvent(eventOne);
        Toolkit.getDefaultToolkit().getSystemEventQueue().postEvent(eventTwo);
    }

    private AWTEvent getSequencedEvent(int id) {
        AWTEvent wrapMe = new WindowEvent(this, id);
        try {
            @SuppressWarnings("unchecked")
            Class<? extends AWTEvent> seqClass = (Class<? extends AWTEvent>) Class.forName("java.awt.SequencedEvent");
            Constructor<? extends AWTEvent> seqConst = seqClass.getConstructor(AWTEvent.class);
            seqConst.setAccessible(true);
            AWTEvent instance = seqConst.newInstance(wrapMe);
            return instance;
        } catch (Throwable err) {
            throw new Error("Unable to instantiate SequencedEvent", err);
        }
    }
}
