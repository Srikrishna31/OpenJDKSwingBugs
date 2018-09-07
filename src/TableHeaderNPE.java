/*
 * Created on 25.07.2008
 *
 */

import javax.swing.JFrame;
import javax.swing.JTable;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.table.JTableHeader;

/**
 * NPE if header not attached to a table. Basic issue is that there are various
 * code places where the access to the header's table is not guarded against null.
 * Not only in Vista context (f.i. #6668281 which is incorrectly marked as fixed) but
 * in BasicHeaderUI as well.
 *
 * To reproduce two:
 * - move a column by mouse drag to a different position
 * - double click on a column
 *
 * Beware: there are other places in the ui delegates! You need to comb thoroughly through
 * all calls to header.getTable() and make sure to guard against null before calling
 * any of table's methods.
 *
 */
public class TableHeaderNPE {

    static public void showUI() {

        try {
            UIManager.setLookAndFeel(UIManager
                    .getCrossPlatformLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }
        // this table is used only to quickly create a column model
        JTable table = new JTable(10, 5);
        // instantiate a tableHeader without attaching to a table
        JTableHeader header = new JTableHeader(table.getColumnModel());
        JFrame frame = new JFrame("standalone header");
        frame.add(header);
        frame.pack();
        frame.setVisible(true);
    }


    public static void main(String[] args) throws Exception {
        SwingUtilities.invokeAndWait(TableHeaderNPE::showUI);
    }
}