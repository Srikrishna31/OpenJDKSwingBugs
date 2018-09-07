/*
JDK-8196384
 */
import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.event.ActionEvent;
import java.io.File;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.filechooser.FileFilter;

public class JFileChooserWoes {

    public static void main(String[] args) {
        JFrame frame = new JFrame("Chooser Test");
        Container cp = frame.getContentPane();
        cp.setLayout(new BorderLayout());
        JButton button = new JButton();
        button.setText("Choose A File");
        button.addActionListener((ActionEvent ae) -> {
                JFileChooser chooser = new JFileChooser();
                chooser.setFileFilter(new DocumentFileFilter());
                chooser.setAcceptAllFileFilterUsed(false);
                int returnVal = chooser.showOpenDialog(frame);
                if(returnVal == JFileChooser.APPROVE_OPTION) {
                    File file = chooser.getSelectedFile();
                    if (file != null) {
                        System.out.println(file.getName());
                    }
                }
        });
        cp.add(button, BorderLayout.CENTER);
        frame.pack();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }

    private static class DocumentFileFilter extends FileFilter {
        public boolean accept(File f){
            return f.getPath().toLowerCase().endsWith(".pdf");
        }

        public String getDescription() {
            String suffix = " (*.pdf)";
            return "Claim Document" + suffix;
        }
    }
}