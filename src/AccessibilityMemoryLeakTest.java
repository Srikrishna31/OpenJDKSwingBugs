import javax.accessibility.Accessible;
import javax.accessibility.AccessibleContext;
import javax.accessibility.AccessibleRole;
import javax.accessibility.AccessibleStateSet;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.NumberFormat;
import java.util.Locale;

public class AccessibilityMemoryLeakTest extends Button implements ActionListener, Accessible {
    public AccessibilityMemoryLeakTest() {
        super("Press this Button");
        addActionListener(this);
    }

    public AccessibleContext getAccessibleContext() {
        return new ActionAccessibleContext();
    }

    public void actionPerformed(ActionEvent e) {
        System.out.println("The button was pressed");
    }

    public void processActionEvent(ActionEvent e) {
        super.processActionEvent(e);
    }

    class ActionAccessibleContext extends AccessibleContext {
        public ActionAccessibleContext() {
            super();
            accessibleName = "Button";
            accessibleDescription = "Press the Button";
        }

        @Override public AccessibleRole getAccessibleRole() {
            return AccessibleRole.PUSH_BUTTON;
        }

        @Override public AccessibleStateSet getAccessibleStateSet() {
            return SwingUtilities.getAccessibleStateSet(AccessibilityMemoryLeakTest.this);
        }

        @Override public Locale getLocale() {
            return AccessibilityMemoryLeakTest.this.getLocale();
        }

        @Override public Accessible getAccessibleChild(int i) {
            return SwingUtilities.getAccessibleChild(AccessibilityMemoryLeakTest.this, i);
        }

        @Override public int getAccessibleIndexInParent() {
            return SwingUtilities.getAccessibleIndexInParent(AccessibilityMemoryLeakTest.this);
        }

        @Override public int getAccessibleChildrenCount() {
            return SwingUtilities.getAccessibleChildrenCount(AccessibilityMemoryLeakTest.this);
        }
    }

    public static void main(String[] args) throws Exception {
        AccessibilityMemoryLeakTest example = new AccessibilityMemoryLeakTest();

        NumberFormat nf = NumberFormat.getNumberInstance();

        JFrame frame = new JFrame("Accessibility Role MemoryLeak Test");
        SwingUtilities.invokeAndWait(() -> {
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.getContentPane().add(example, BorderLayout.CENTER);
            frame.setSize(100, 100);
            frame.setVisible(true);
        });

        AccessibleRole role = example.getAccessibleContext().getAccessibleRole();
        Runtime rt = Runtime.getRuntime();

        System.out.println("[Memory User: " + nf.format(rt.totalMemory() / 1024) +
                "kb Free=" + nf.format(rt.freeMemory() / 1024) +
                "kb Max=" + nf.format(rt.maxMemory() / 1024) + " kb]");

        for (int i = 0; i < 100000; i++) {
            String str = role.toDisplayString();

//            System.out.println("[Memory User: " + nf.format(Runtime.getRuntime().totalMemory() / 1024) +
//                    "kb Free=" + nf.format(Runtime.getRuntime().freeMemory() / 1024) +
//                    "kb Max=" + nf.format(Runtime.getRuntime().maxMemory() / 1024) + " kb]");

            if (i % 10 == 0) {
                System.out.println("[Memory User: " + nf.format(rt.totalMemory() / 1024) +
                        "kb Free=" + nf.format(rt.freeMemory() / 1024) +
                        "kb Max=" + nf.format(rt.maxMemory() / 1024) + " kb]");
            }
//                MemStats memStats = new MemStats(Runtime.getRuntime().totalMemory() / 1024,
//                        Runtime.getRuntime().freeMemory() / 1024,
//                        Runtime.getRuntime().maxMemory() / 1024);
//
//                System.out.println(memStats);
            }

        System.out.println("[Memory User: " + nf.format(rt.totalMemory() / 1024) +
                "kb Free=" + nf.format(rt.freeMemory() / 1024) +
                "kb Max=" + nf.format(rt.maxMemory() / 1024) + " kb]");

        SwingUtilities.invokeAndWait(() -> frame.dispose());
    }
}
