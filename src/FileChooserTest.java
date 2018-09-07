import javax.swing.*;
import javax.swing.filechooser.FileSystemView;
import java.io.File;

/*
JDK-8194044
JDK-8194049
 */
public class FileChooserTest extends JDialog {
    private JButton run = new JButton("run");

    public FileChooserTest(JFrame frame) {
        super(frame, "FileChooserTest");

        add("South", run);
        run.addActionListener(e -> {
            JFileChooser fc = new JFileChooser();
            fc.showOpenDialog(null);
        });
        pack();
        setVisible(true);
    }

    public static void main(String[] args) throws Exception {
        SwingUtilities.invokeAndWait(() ->{
            JFrame frame = new JFrame();
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            new FileChooserTest(frame);
        });

//        File f = new File("C:\\");
//        FileSystemView fsv = FileSystemView.getFileSystemView();
//        boolean isRoot = fsv.isFileSystemRoot(f);
//        assert(isRoot);
    }
}
