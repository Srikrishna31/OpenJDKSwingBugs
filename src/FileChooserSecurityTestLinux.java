import java.io.File;
//import javax.swing.JFileChooser;
//import javax.swing.SwingUtilities;
import javax.swing.filechooser.FileSystemView;

public class FileChooserSecurityTestLinux {
    public static void main(String[] args) throws Exception {
        File defaultDir = FileSystemView.getFileSystemView().getDefaultDirectory();
        System.out.println("defaultDir: " + defaultDir);

//        SwingUtilities.invokeAndWait(() -> {
//                JFileChooser fileChooser = new JFileChooser();
//
//                fileChooser.showOpenDialog(null);
//        });
    }
} 
