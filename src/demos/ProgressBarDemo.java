
package demos;

import javax.swing.*;
import javax.swing.border.BevelBorder;
import javax.swing.border.SoftBevelBorder;
import java.awt.*;
import java.awt.event.ActionEvent;

/**
 * JProgressBar Demo
 *
 * @author Jeff Dinkins
# @author Peter Korn (accessibility support)
 */
public class ProgressBarDemo extends DemoModule {

    /**
     * main method allows us to run as a standalone demo.
     */
    public static void main(String[] args) {
        ProgressBarDemo demo = new ProgressBarDemo(null);
        demo.mainImpl();
    }

    /**
     * demos.SwingSet2.ProgressBarDemo Constructor
     */
    public ProgressBarDemo(SwingSet2 swingset) {
        // Set the title for this demo, and an icon used to represent this
        // demo inside the SwingSet2 app.
        super(swingset, "demos.SwingSet2.ProgressBarDemo", "toolbar/JProgressBar.gif");

        createProgressPanel();
    }

    javax.swing.Timer timer = new javax.swing.Timer(18, createTextLoadAction());
    Action loadAction;
    Action stopAction;
    JProgressBar progressBar;
    JTextArea progressTextArea;

    void updateDragEnabled(boolean dragEnabled) {
        progressTextArea.setDragEnabled(dragEnabled);
    }

    public void createProgressPanel() {
        getDemoPanel().setLayout(new BorderLayout());

        JPanel textWrapper = new JPanel(new BorderLayout());
        textWrapper.setBorder(new SoftBevelBorder(BevelBorder.LOWERED));
        textWrapper.setAlignmentX(LEFT_ALIGNMENT);
        progressTextArea = new MyTextArea();

        progressTextArea.getAccessibleContext().setAccessibleName(getString("demos.SwingSet2.ProgressBarDemo.accessible_text_area_name"));
        progressTextArea.getAccessibleContext().setAccessibleName(getString("demos.SwingSet2.ProgressBarDemo.accessible_text_area_description"));
        textWrapper.add(new JScrollPane(progressTextArea), BorderLayout.CENTER);

        getDemoPanel().add(textWrapper, BorderLayout.CENTER);

        JPanel progressPanel = new JPanel();
        getDemoPanel().add(progressPanel, BorderLayout.SOUTH);

        progressBar = new JProgressBar(JProgressBar.HORIZONTAL, 0, text.length()) {
            public Dimension getPreferredSize() {
                return new Dimension(300, super.getPreferredSize().height);
            }
        };
        progressBar.getAccessibleContext().setAccessibleName(getString("demos.SwingSet2.ProgressBarDemo.accessible_text_loading_progress"));

        progressPanel.add(progressBar);
        progressPanel.add(createLoadButton());
        progressPanel.add(createStopButton());
    }

    public JButton createLoadButton() {
        loadAction = new AbstractAction(getString("demos.SwingSet2.ProgressBarDemo.start_button")) {
            public void actionPerformed(ActionEvent e) {
                loadAction.setEnabled(false);
                stopAction.setEnabled(true);
                if (progressBar.getValue() == progressBar.getMaximum()) {
                    progressBar.setValue(0);
                    textLocation = 0;
                    progressTextArea.setText("");
                }
                timer.start();
            }
        };
        return createButton(loadAction);
    }

    public JButton createStopButton() {
        stopAction = new AbstractAction(getString("demos.SwingSet2.ProgressBarDemo.stop_button")) {
            public void actionPerformed(ActionEvent e) {
                timer.stop();
                loadAction.setEnabled(true);
                stopAction.setEnabled(false);
            }
        };
        return createButton(stopAction);
    }

    public JButton createButton(Action a) {
        JButton b = new JButton();
        // setting the following client property informs the button to show
        // the action text as it's name. The default is to not show the
        // action text.
        b.putClientProperty("displayActionText", Boolean.TRUE);
        b.setAction(a);
        return b;
    }


    int textLocation = 0;

    String text = getString("demos.SwingSet2.ProgressBarDemo.text");

    public Action createTextLoadAction() {
        return new AbstractAction("text load action") {
            public void actionPerformed (ActionEvent e) {
                if(progressBar.getValue() < progressBar.getMaximum()) {
                    progressBar.setValue(progressBar.getValue() + 1);
                    progressTextArea.append(text.substring(textLocation, textLocation+1));
                    textLocation++;
                } else {
                    timer.stop();
                    loadAction.setEnabled(true);
                    stopAction.setEnabled(false);
                }
            }
        };
    }


    class MyTextArea extends JTextArea {
        public MyTextArea() {
            super(null, 0, 0);
            setEditable(false);
            setText("");
        }

        public float getAlignmentX () {
            return LEFT_ALIGNMENT;
        }

        public float getAlignmentY () {
            return TOP_ALIGNMENT;
        }
    }
}

