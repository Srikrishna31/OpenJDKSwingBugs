/*
JDK-8201421
 */
import java.awt.Dimension;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.Locale;

import javax.swing.JDesktopPane;
import javax.swing.JFrame;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.plaf.synth.SynthLookAndFeel;

public class TurkishParserExceptionTest extends JFrame{
    private static String synthXml = "<synth>" +
            "<style id=\"default\">" +
            "<state>" +
            "<font id=\"DefaultFont\" name=\"Tahoma\" size=\"11\"/>" +
            "<color type=\"FOREGROUND\" value=\"#000000\" />" +
            "</state>" +
            "<state value=\"DISABLED\">" +
            "<color type=\"TEXT_FOREGROUND\" value=\"#A0A0A0\" />" +
            "</state>" +
            "</style>" +
            "<bind style=\"default\" type=\"region\" key=\".*\" />" +
            "</synth>";

    public static void main(String[] args){
        SwingUtilities.invokeLater(new Runnable(){
            @Override
            public void run(){
                try{
                    Locale.setDefault(new Locale("tr"));
                    SynthLookAndFeel laf = new SynthLookAndFeel();
                    Class<?> clazz = TurkishParserExceptionTest.class;
                    InputStream ins = new ByteArrayInputStream(synthXml.getBytes("UTF8"));
                    laf.load(ins, clazz);
                    //laf.load(clazz.getResourceAsStream("synth.xml"), clazz);
                    UIManager.setLookAndFeel(laf);
                    new TurkishParserExceptionTest();
                }
                catch (Exception e){
                    e.printStackTrace();
                }
            }
        });
    }

    public TurkishParserExceptionTest(){
        JDesktopPane desktop = new JDesktopPane();
        getContentPane().add(desktop);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(new Dimension(400, 300));
        setLocationRelativeTo(null);
        setVisible(true);
    }
} 
