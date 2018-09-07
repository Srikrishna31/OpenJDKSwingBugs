/*
JDK-8196673
 */
import javax.swing.*;
import javax.swing.filechooser.FileSystemView;
import java.io.File;
import java.nio.file.Paths;
public class JFileChooserBug18 {
    public static void main(String args[]) throws Exception {
//        Paths.get("C:\\testspace  ");
        SwingUtilities.invokeAndWait(() -> {
                        File f = new File("testspace");
                        f.mkdir();

                        f = new File("C:\\testspace    ");
                        String str;
                        try {
                            str = f.getCanonicalPath();
                        } catch (Exception ex) {}
                        FileSystemView fileSystemView = FileSystemView.getFileSystemView();

                        String name = fileSystemView.getSystemDisplayName(new File("C:\\testspace   "));
                        JFileChooser fc = new JFileChooser();
                        fc.setCurrentDirectory(f.getAbsoluteFile().getParentFile());
                        String name1 = fc.getName(new File("C:\\testspace    "));
                        fc.showSaveDialog(null);
                });
    }
}

