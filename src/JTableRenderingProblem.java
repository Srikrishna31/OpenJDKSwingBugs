/**
 * JDK-8201309
 */

import javax.swing.*;
import java.awt.*;

public class JTableRenderingProblem  {
    public static Integer value = 3;

    public static void main(String[] args) {
        JFrame frame = new JFrame("Buggy JTable");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(600, 400);


        Object[][] rowData = {
                {1, 2, 3},
                {null, null, null}
        };
        Object[] columns = {"A", "B", "C"};
        JTable table = new JTable(rowData, columns);

        frame.getContentPane().add(table);

        // this works fine!
        //frame.getContentPane().add(new JScrollPane(table));
        //table.setTableHeader(null);

        JButton b = new JButton("Change value");
        b.addActionListener(e -> table.getModel().setValueAt(++value, 0, 2));

        frame.getContentPane().add(b, BorderLayout.SOUTH);

        frame.setVisible(true);
    }
}
