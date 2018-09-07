/**
 * Created by kaddepal on 7/17/2017.
 */
import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class BuggyDialog extends javax.swing.JDialog {

    private int _curVal;

    public BuggyDialog(java.awt.Frame parent, boolean modal) {
        super(parent, modal);

        initComponents();
        setLocationRelativeTo(null);
        _curVal = _scrollBar.getValue();
    }

    private void initComponents() {
        java.awt.GridBagConstraints gridBagConstraints;

        _scrollBar = new javax.swing.JScrollBar();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        getContentPane().setLayout(new java.awt.GridBagLayout());

        _scrollBar.setMinimumSize(new java.awt.Dimension(17, 200));
        _scrollBar.setPreferredSize(new java.awt.Dimension(17, 200));
        _scrollBar.addAdjustmentListener(new java.awt.event.AdjustmentListener() {
            public void adjustmentValueChanged(java.awt.event.AdjustmentEvent evt) {
                _scrollBarAdjustmentValueChanged(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        getContentPane().add(_scrollBar, gridBagConstraints);

        pack();
    }

    private void _scrollBarAdjustmentValueChanged(java.awt.event.AdjustmentEvent evt) {
        if (evt.getValueIsAdjusting() || evt.getValue() == _curVal) {
            return;
        }
        _curVal = evt.getValue();
        System.out.println(evt.getValue());
//
//        JFrame frame = new JFrame("Test");
        JDialog frame = new JDialog(this, true);
        frame.setTitle("Test");
        frame.setAlwaysOnTop(true);
        Rectangle bds = this.getBounds();
        frame.setBounds(bds.x + 30, bds.y + 30, bds.width, bds.height);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setModalExclusionType(ModalExclusionType.APPLICATION_EXCLUDE);
        frame.setVisible(true);

        Toolkit tk = Toolkit.getDefaultToolkit();
        EventQueue eq = tk.getSystemEventQueue();
        SecondaryLoop loop = eq.createSecondaryLoop();

//        // Spawn a new thread to do the work
//        Thread worker = new WorkerThread();
//        worker.start();

        // Enter the loop to block the current event
        // handler, but leave UI responsive
        if (!loop.enter()) {
            // Report an error
        }
//        JOptionPane.showMessageDialog(this, "Testing");
    }

    public static void main(String args[]) {
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                BuggyDialog dialog = new BuggyDialog(new javax.swing.JFrame(), true);
                dialog.addWindowListener(new java.awt.event.WindowAdapter() {
                    @Override
                    public void windowClosing(java.awt.event.WindowEvent e) {
                        System.exit(0);
                    }
                });
                dialog.setVisible(true);
            }
        });
    }

    private javax.swing.JScrollBar _scrollBar;
}