import java.io.File;
import java.text.NumberFormat;
import javax.swing.filechooser.FileSystemView;

public class FSVTest {

    public static void main(String[] args) {
        test();
    }

    private static void test() {

        System.getProperties().list(System.out);

        File root = new File("C:\\");
        System.out.println("Root Exists: " + root.exists());
        System.out.println("Root Absolute Path: " + root.getAbsolutePath());
        System.out.println("Root Is Directory?: " + root.isDirectory());

        FileSystemView fileSystemView = FileSystemView.getFileSystemView();
        NumberFormat nf = NumberFormat.getNumberInstance();

//        File[] rts = fileSystemView.getRoots();
//        File p = fileSystemView.getParentDirectory((File)rts[0]);
        File def = fileSystemView.getDefaultDirectory();
        File p = fileSystemView.getParentDirectory(def);
        File p1 = fileSystemView.getParentDirectory(p);
        File p2 = fileSystemView.getParentDirectory(p1);
        assert(fileSystemView.isFileSystemRoot(p2));

        int iMax = 50000;
        long lastPercentFinished = 0L;
        for (int i = 0; i < iMax; i++) {

            long percentFinished = Math.round(((i * 1000d) / (double) iMax));

            if (lastPercentFinished != percentFinished) {
                double pf = ((double) percentFinished) / 10d;
                String pfMessage = String.valueOf(pf) + " % (" + i + "/" + iMax + ")";

                long totalMemory = Runtime.getRuntime().totalMemory() / 1024;
                long freeMemory = Runtime.getRuntime().freeMemory() / 1024;
                long maxMemory = Runtime.getRuntime().maxMemory() / 1024;
                String memMessage = "[Memory Used: " + nf.format(totalMemory) +
                                    " kb Free=" + nf.format(freeMemory) +
                                    " kb Max: " + nf.format(maxMemory) + " kb]";

                System.out.println(pfMessage + " " + memMessage);
                lastPercentFinished = percentFinished;
            }

            boolean floppyDrive = fileSystemView.isFloppyDrive(root);
            boolean computerNode = fileSystemView.isComputerNode(root);

            // "isDrive()" seems to be the painful method...
            boolean drive = fileSystemView.isDrive(root);
            File[] files = fileSystemView.getFiles(root, true);
        }
    }
}

