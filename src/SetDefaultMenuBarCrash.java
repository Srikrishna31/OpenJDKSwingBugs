import javax.swing.*;
import javax.swing.event.MenuEvent;
import javax.swing.event.MenuListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.util.Random;

// import com.apple.eawt.Application;

/*

Synopsis:
    macOS 10.13 crashes when setting DefaultMenuBar and using accessibility zoom
Full OS version:
    Apple Mac OS X 10.13.2 (17C88)
Additional Configuration Information:
    Accessibility zoom must be activated.
        System Preferences -> Accessibility -> Zoom

Development Kit or Runtime version:
    Java(TM) SE Runtime Environment (build 9.0.4+11)
    Java(TM) SE Runtime Environment (build 1.8.0_152-b16)
    openjdk version "11.0.0.1-internal" 2018-06-07
Description:
    On macOS 10.13 (macOS High Sierra) the java vm crashes when setting the default menu bar
    (JDK9: Desktop.getDesktop().setDefaultMenuBar)
    and using accessibility zoom in/out feature.
    see: System Preferences -> Accessibility -> Zoom

    The JMenuBar that is used should not be completely static. Therefore items should be added and removed after
    setDefaultMenuBar is called.

Steps to Reproduce:
    - Create a JMenuBar that is not static, i.e. in MenuListener.menuSelected(MenuEvent arg0)
        add and remove some menu items. Use this menu bar as default menu bar on macOS 10.13.
        One menu item should show a new JFrame
        (see SetDefaultMenuBarCrash.java example)
    - Launch the java application.
        if you run the example close the initial JFrame
    - Zoom in and out (accessibility zoom: "ALT-Command-8" or scroll gesture)
    - switch to Finder
    - switch back to java application
    - open a new JFrame via menu. Use the mouse to select the menu item!
        in example select menu "Test-DEFAULT" ->  "Show New Frame"

Expected Results:
    JFrame should show up.

Actual Result:
    JVM crashes, a hs_pidXXXX.log file is created
    -> A fatal error has been detected by the Java Runtime Environment: SIGILL (0x4)

Frequency:
    on macOS 10.13 Mostly, not every time
    Not on macOS 10.12

Error Message:
    A fatal error has been detected by the Java Runtime Environment

Workaround:
    none

Severity:
    fatal

author Robert.Straub@sap.com
 */
public class SetDefaultMenuBarCrash {
    public SetDefaultMenuBarCrash() {
        JFrame frame = new JFrame("DefaultMenuBar");
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setSize(300, 300);
        frame.add(new JLabel("Close this frame to activate DefaultMenuBar."));
        frame.setJMenuBar(createMB(""));
        frame.setVisible(true);
    }

    public static JMenuBar createMB(String txt) {
        int shortcutKeyMask = Toolkit.getDefaultToolkit().getMenuShortcutKeyMask();

        // create a menubar
        JMenuBar mb = new JMenuBar();
        JMenu menu = new JMenu("Test" + txt);
        mb.add(menu);
        JMenuItem mi = new JMenuItem("A Menu Item");
        mi.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent arg0) {
                // show a message when action is fired
                JOptionPane.showMessageDialog(null, "Menu Item selected: \n" + arg0);
            }
        });
        // assign a keystroke
        mi.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_O, shortcutKeyMask));
        menu.add(mi);

        // create a new frame
        mi = new JMenuItem("Show New Frame");
        mi.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent arg0) {
                new SetDefaultMenuBarCrash();
            }
        });
        // assign a keystroke
        mi.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_N, shortcutKeyMask));
        menu.add(mi);

        // create some menus with sub menus
        // these submenus are changed in the Handler.menuSelected()
        for(int k=0; k<3;k++) {
            JMenu subMenu = new JMenu("SubMenu-"+k);
            for(int i = 0; i < 20; i++) {
                subMenu.add(new JMenuItem("Item - " + i));
            }
            menu.add(subMenu);
        }
        menu.addMenuListener(new Handler());
        return mb;
    }

    public static class Handler implements MenuListener {
        @Override
        public void menuCanceled(MenuEvent arg0) {}

        @Override
        public void menuDeselected(MenuEvent arg0) {}

        @Override
        public void menuSelected(MenuEvent arg0) {
            // get the JFrame and JMenuBar from component hierarchy
            Object obj = arg0.getSource();
            if (obj instanceof JMenuItem) {
                JMenuItem mi = (JMenuItem)obj;
                Container cont = mi;
                JMenuBar mb = null;
                boolean setMenu = true;
                while (cont!=null) {
                    cont = cont.getParent();
                    if (cont instanceof JMenuBar)
                        mb = (JMenuBar)cont;
                    // when used as DefaultMenu there is no JFrame in the hierarchy
                    if (cont instanceof JFrame)
                        setMenu = false;
                }
                if (setMenu && mb!=null) {
                    addItems(mb);
                }
            }
        }

        /**
         * Change all sub menus by adding a random number of menu items.
         * @param mb JMenuBar to change
         */
        public void addItems(JMenuBar mb) {
            JMenu menu = mb.getMenu(0);
            for(int j=0; j<menu.getMenuComponentCount(); j++) {
                Component mic = menu.getMenuComponent(j);
                if(mic instanceof JMenu) {
                    JMenu m = (JMenu)mic;
                    // first remove all menu items
                    m.removeAll();

                    // now add a random number of menu items
                    Random generator = new Random();
                    int anz = generator.nextInt(100) + 1;
                    // System.err.println("adding "+anz+" menu items -> "+menu.getText()+" / "+m.getText());
                    for (int k=0; k<anz; k++) {
                        m.add(new JMenuItem("Item - "+k));
                    }
                }
            }
        }
    }

    private static void createAndShowGUI() {
        System.err.println("java.version: " + System.getProperty("java.runtime.version"));

        new SetDefaultMenuBarCrash();

        // for JDK 9 ff we can call Desktop.getDesktop().setDefaultMenuBar
        //Desktop.getDesktop().setDefaultMenuBar(createMB("-DEFAULT"));
    }

    public static void main(String[] args) {
        // on macOS show menubar as ScreenMenuBar
        System.setProperty("apple.laf.useScreenMenuBar", "true");

        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                createAndShowGUI();
            }
        });
    }
}
