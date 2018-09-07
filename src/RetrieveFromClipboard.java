import java.awt.*;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.IOException;

public final class RetrieveFromClipboard {
    public static void main(final String[] args) throws UnsupportedFlavorException, IOException {
        Toolkit.getDefaultToolkit().getSystemClipboard().getContents(null).getTransferData(DataFlavor.javaFileListFlavor);
    }
}
