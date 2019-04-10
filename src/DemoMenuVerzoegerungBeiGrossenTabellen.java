/*
Reproducing JDK-8220683
 */

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.MenuBar;
import java.awt.event.KeyEvent;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.swing.BoxLayout;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableModel;
import javax.swing.UIManager.LookAndFeelInfo;

/**
 * DEMO: Die Tabelle enthält insgesamt TOTAL_RESULTS Einträge, die in Portionsgrößen von jeweils PAGE_SIZE geladen
 * werden, sobald ein Datenfeld des Eintrags benötigt wird - das ist normaler Weise der Fall, wenn die Einträge in der
 * Tabelle in den sichtbaren Bereich gescrollt werden. Das Laden der Einträge wird im Feld Hintergrundaktionen
 * protokolliert.
 * <p>Wenn JAWS gestart ist (die AccessBridge aktiviert wird?) und im geöffneten Dialog mittels [Alt] das Menü aktiviert
 * werden bereits alle Einträge der Tabelle geladen - zu sehen an der Protokollierung im Feld Hintergrundaktionen - obwohl die
 * Einträge noch nicht in der Tabelle sichtbar sind. Ursache ist anscheinend, dass die AccessBridge initial alle
 * Datenfelder aller Tabelleneinträge ermittelt und somit das Laden der Einträge auslöst.</p>
 * <p>Problematischer Effekt
 * dabei ist, das dieses initiale Laden das Fenster \"einfrieren\" lässt - es ist direkt nach Öffnen des Dialogs zB
 * nicht möglich, das Menü mittels Tastaturbedienung zu verwenden. Außerdem wird die Optimierung des Datenabrufs
 * bezüglich Performance und Last zunichte gemacht.</p>
 *
 * @author FiebigS003
 */
@SuppressWarnings("serial")
public class DemoMenuVerzoegerungBeiGrossenTabellen extends JFrame {

    static final int PAGE_SIZE = 100;
    static final int TOTAL_RESULTS = 1500;
    private MyTableModel tableModel;
    private JTextArea logFeld;



    public DemoMenuVerzoegerungBeiGrossenTabellen() {
        super("Demo Menü-Verzögerung bei grossen Tabellen - Client friert ein");
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setMinimumSize(new Dimension(700, 500));
        build();
    }

    //simuliert Service-Call mit Antwortzeit von 1s
    public void ladeEintraege(int page) {
        StringBuilder b = new StringBuilder();
        b.append("Lade Einträge ");
        b.append(page*PAGE_SIZE);
        b.append(" - ");
        b.append(page*PAGE_SIZE+PAGE_SIZE);
        b.append(" (Page ");
        b.append(page);
        b.append(")");
        log(b.toString());
        try {
            Thread.currentThread().sleep(1000);
            for (int i = 0; i < PAGE_SIZE;i++) {
                tableModel.eintraege.get(page*PAGE_SIZE+i).init();
            }
        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    private void log(String msg) {
        System.out.println(msg);
        logFeld.append(msg);
        logFeld.append("\n");
    }

    void build() {
        setJMenuBar(createMenuBar());
        setContentPane(createContentPane());
        pack();
    }

    private JComponent createContentPane() {
        JPanel panel = new JPanel(new BorderLayout(20, 0));
        panel.add(createInfo(),BorderLayout.NORTH);
        panel.add(createTable(),BorderLayout.CENTER);
        panel.add(createLog(),BorderLayout.SOUTH);
        return panel;
    }

    private Component createLog() {
        JPanel p = new JPanel();
        BoxLayout layout = new BoxLayout(p,BoxLayout.PAGE_AXIS);
        p.setLayout(layout);
        p.add(new JLabel("Hintergrundaktionen:",SwingConstants.LEFT));
        logFeld = new JTextArea();
        logFeld.setEditable(false);
        logFeld.setColumns(80);
        logFeld.setRows(10);
        logFeld.setWrapStyleWord(true);
        JScrollPane scroll = new JScrollPane(logFeld);
        p.add(scroll);
        return p;
    }

    private Component createInfo() {
        JTextArea info = new JTextArea();
        info.setEditable(false);
        info.setBackground(getBackground());
        info.setColumns(80);
        info.setRows(18);
        info.setLineWrap(true);
        info.setWrapStyleWord(true);
        info.append("DEMO: ");
        info.append("Die Tabelle enthält insgesamt " + TOTAL_RESULTS + " Einträge, die in Portionsgrößen von jeweils " + PAGE_SIZE + " geladen werden, sobald ein Datenfeld des Eintrags benötigt wird - das ist normaler Weise der Fall, wenn die Einträge in der Tabelle in den sichtbaren Bereich gescrollt werden. ");
        info.append("Das Laden der Einträge wird im Feld Hintergrundaktionen protokolliert. ");
        info.append("\n\n");
        info.append("Wenn JAWS gestart ist (die AccessBridge aktiviert wird?) und im geöffneten Dialog mittels [Alt] das Menü aktiviert wird, werden bereits alle Einträge der Tabelle geladen - zu sehen an der Protokollierung im Feld Hintergrundaktionen - obwohl die Einträge noch nicht in der Tabelle sichtbar sind. ");
        info.append("Ursache ist anscheinend, dass die AccessBridge initial alle Datenfelder aller Tabelleneinträge ermittelt und somit das Laden der Einträge auslöst. ");
        info.append("\n\n");
        info.append("Problematischer Effekt dabei ist, das dieses initiale Laden das Fenster \"einfrieren\" lässt - ");
        info.append("es ist direkt nach Öffnen des Dialogs zB nicht möglich, das Menü mittels Tastaturbedienung zu verwenden. ");
        info.append("Außerdem wird die Optimierung des Datenabrufs bezüglich Performance und Last zunichte gemacht.");
        info.setFocusable(true);
        return info;
    }

    private Component createTable() {
        JTable tabelle = new JTable(createTableModel());
        JScrollPane scroll = new JScrollPane(tabelle);
        return scroll;
    }

    private TableModel createTableModel() {
        tableModel = new MyTableModel();
//		ladeEintraege(0);
        return tableModel;
    }

    private JMenuBar createMenuBar() {
        JMenuBar menu = new JMenuBar();
        JMenu menu1 = new JMenu("Menu 1");
        menu1.setMnemonic('1');
        menu1.add("Eintrag X");
        menu1.add("Eintrag Y");
        menu1.add("Eintrag Z");
        JMenu menu2 = new JMenu("Menu 2");
        menu2.setMnemonic('2');
        menu2.add("Eintrag A");
        menu2.add("Eintrag B");
        menu2.add("Eintrag C");
        menu2.add("Eintrag D");
        menu2.add("Eintrag E");
        JMenu menu3 = new JMenu("Menu 3");
        menu3.setMnemonic('3');
        menu3.add("Eintrag U");
        menu3.add("Eintrag V");
        menu3.add("Eintrag W");
        menu.add(menu1);
        menu.add(menu2);
        menu.add(menu3);
        return menu;
    }

    public static void main(String[] args) throws UnsupportedLookAndFeelException,
                        IllegalAccessException, InstantiationException, ClassNotFoundException,
                        InterruptedException, InvocationTargetException {
        LookAndFeelInfo[] lnfs = UIManager.getInstalledLookAndFeels();
        for (LookAndFeelInfo lnf : lnfs) {
            if (lnf.getClassName().contains("Windows")){
                UIManager.setLookAndFeel(lnf.getClassName());
            }
        }
        SwingUtilities.invokeAndWait(() -> {
                JFrame f = new DemoMenuVerzoegerungBeiGrossenTabellen();
                f.setVisible(true);
        });
    }

    //Lazy-initialisierter Tabelleneintrag.
    //Die Datenfelder werden erst mittels eines paginierten Lademechanismus befüllt,
    //wenn Daten über die Getter-Methoden abgerufen werden.
    private class TabellenEintrag {
        private int index;
        private boolean geladen = false;
        private String nr;
        private String wert;

        TabellenEintrag(final int index) {
            this.index = index;
        }

        void init() {
            //System.out.println("Initialisierung Tabelleneintrag #" + index);
            geladen = true;
            nr = "#" + index;
            wert = "Eintrag " + index;
        }

        String getNr(){
            if (index == 123) {
                System.err.println(Arrays.deepToString(Thread.currentThread().getStackTrace()));
            }
            if (!geladen) {
                ladeEintraege(index / PAGE_SIZE);
            }
            return nr;
        }
        String getWert() {
            if (!geladen) {
                ladeEintraege(index / PAGE_SIZE);
            }
            return wert;
        }
    }

    //TableModel: Es werden bei Initialisierung alle Tabelleneinträge angelegt
    //diese sind aber leer, solange keine Daten über deren Getter-Methoden abgefragt werden.
    //Normaler Weise ruft getValueAt() Daten der Einträge ab, wenn die Einträge in der Tabelle
    //sichtbar werden.
    private class MyTableModel extends AbstractTableModel {

        List<TabellenEintrag> eintraege = new ArrayList<>(TOTAL_RESULTS);

        public MyTableModel() {
            for (int i = 0; i<TOTAL_RESULTS;i++) {
                eintraege.add(new TabellenEintrag(i));
            }
        }

        @Override
        public int getColumnCount() {
            return 2;
        }

        @Override
        public int getRowCount() {
            return TOTAL_RESULTS;
        }

        @Override
        public Object getValueAt(int zeile, int spalte) {
            switch (spalte) {
                case 0: return eintraege.get(zeile).getNr();
                case 1: return eintraege.get(zeile).getWert();
                default: return "???";
            }
        }
    }
}
//Stack-Trace, wenn Tabelleneintrag von der AccessBridge abgefragt wird:
//
//java.lang.Thread.getStackTrace(Unknown Source),
//test.components.DemoMenuVerzoegerungBeiGrossenTabellen$TabellenEintrag.getNr(DemoMenuVerzoegerungBeiGrossenTabellen.java:216),
//test.components.DemoMenuVerzoegerungBeiGrossenTabellen$MyTableModel.getValueAt(DemoMenuVerzoegerungBeiGrossenTabellen.java:258)
//javax.swing.JTable.getValueAt(Unknown Source)
//javax.swing.JTable$AccessibleJTable$AccessibleJTableCell.getCurrentAccessibleContext(Unknown Source)
//javax.swing.JTable$AccessibleJTable$AccessibleJTableCell.getAccessibleStateSet(Unknown Source),
//com.sun.java.accessibility.AccessBridge$164.call(AccessBridge.java:4594),
//com.sun.java.accessibility.AccessBridge$164.call(AccessBridge.java:4591),
//com.sun.java.accessibility.AccessBridge$InvocationUtils$CallableWrapper.run(AccessBridge.java:7180),
//java.awt.event.InvocationEvent.dispatch(Unknown Source),
//java.awt.EventQueue.dispatchEventImpl(Unknown Source),
//java.awt.EventQueue.access$300(Unknown Source),
//java.awt.EventQueue$3.run(Unknown Source),
//java.awt.EventQueue$3.run(Unknown Source),
//java.security.AccessController.doPrivileged(Native Method),
//java.security.ProtectionDomain$JavaSecurityAccessImpl.doIntersectionPrivilege(Unknown Source),
//java.awt.EventQueue.dispatchEvent(Unknown Source),
//java.awt.EventDispatchThread.pumpOneEventForFilters(Unknown Source),
//java.awt.EventDispatchThread.pumpEventsForFilter(Unknown Source),
//java.awt.EventDispatchThread.pumpEventsForHierarchy(Unknown Source),
//java.awt.EventDispatchThread.pumpEvents(Unknown Source),
//java.awt.EventDispatchThread.pumpEvents(Unknown Source),
//java.awt.EventDispatchThread.run(Unknown Source)]
