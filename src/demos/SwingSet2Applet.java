package demos;

import javax.swing.*;
import java.awt.*;
import java.net.URL;

/**
 *
 *
 * @author Jeff Dinkins
 */

public class SwingSet2Applet extends JApplet {
    public void init() {
        getContentPane().setLayout(new BorderLayout());
        getContentPane().add(new SwingSet2(this), BorderLayout.CENTER);
    }

    public URL getURL(String filename) {
        URL codeBase = this.getCodeBase();
        URL url = null;

        try {
            url = new URL(codeBase, filename);
            System.out.println(url);
        } catch (java.net.MalformedURLException e) {
            System.out.println("Error: badly specified URL");
            return null;
        }

        return url;
    }


}
