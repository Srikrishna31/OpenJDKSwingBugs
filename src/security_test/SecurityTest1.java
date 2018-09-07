package security_test;

import javax.swing.*;
import java.io.*;
import javax.swing.filechooser.FileSystemView;

public class SecurityTest1 {
    public static void main(String[] args) throws Exception {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setCurrentDirectory(new File(System.getProperty("user.home") + "/Desktop"));
        fileChooser.showOpenDialog(null);

//        new JFileChooser(new File(System.getProperty("user.home") + "/Desktop")).showOpenDialog(null);
    }
}
