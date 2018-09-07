package jdialogex;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.*;

    /**
     *
     * @author blokesh
     */
    public class JDialogEx {
        private static String osVersion;
        private static String javaVersion;
        private static Logger logger = Logger.getLogger(JDialogEx.class.getName());

        /**
         * @param args the command line arguments
         */
        public static void main(String[] args) {
            try {
                osVersion = System.getProperty("os.name");
            } catch (Exception ignored) {
            }
            try {
                javaVersion = System.getProperty("java.vendor") + " / " + System.getProperty("java.version");
            } catch (Exception ignored) {
            }


            System.out.println("[STARTUP] OS Version : " + osVersion);
            System.out.println("[STARTUP] Java Version : " + javaVersion);
            JFrame f = new JFrame();
            final JPanel panel1 = new JPanel();
            final JDialog dialog = new JDialog(f, "Information", true);
            JLabel lbl = new JLabel("Menu is created. Please continue...");
            panel1.add(lbl);
            JButton btn = new JButton("Ok");
            btn.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    dialog.setVisible(false);
                    dialog.dispose();
                }
            });
            panel1.add(btn);
            panel1.setAlignmentY(Float.MAX_VALUE);
            dialog.add(panel1);
            dialog.setBounds(100, 500, 500, 100);
            Timer timer = new Timer(1000, new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    dialog.setVisible(false);
                    dialog.dispose();
                }
            });
            timer.setRepeats(false);
            timer.start();

            dialog.setVisible(true); // if modal, application will pause here
            final JPanel panel = new JPanel();

        }

    }
