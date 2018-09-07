/*
 * Copyright (c) 2016, 2018 Oracle and/or its affiliates. All rights reserved.
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS FILE HEADER.
 *
 * This code is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License version 2 only, as
 * published by the Free Software Foundation.
 *
 * This code is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License
 * version 2 for more details (a copy is included in the LICENSE file that
 * accompanied this code).
 *
 * You should have received a copy of the GNU General Public License version
 * 2 along with this work; if not, write to the Free Software Foundation,
 * Inc., 51 Franklin St, Fifth Floor, Boston, MA 02110-1301 USA.
 *
 * Please contact Oracle, 500 Oracle Parkway, Redwood Shores, CA 94065 USA
 * or visit www.oracle.com if you need additional information or have any
 * questions.
 */

/*
  @test
  @key headful
  @bug 8150176 8150844
  @author a.stepanov
  @summary Check if correct resolution variant is used
           for JOptionPane dialog / internal frame icons.
  @library ../../../../lib/testlibrary/
  @build ExtendedRobot
  @run main/othervm/timeout=300 -Dsun.java2d.uiScale=2 MultiResolutionJOptionPaneIconTest
  @run main/othervm/timeout=300 -Dsun.java2d.uiScale=1 MultiResolutionJOptionPaneIconTest
*/

import test.ExtendedRobot;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;

//import java.awt.image.BaseMultiResolutionImage;

public class MultiResolutionJOptionPaneIconTest implements ActionListener {

    private final static Color C1X = Color.ORANGE, C2X = Color.CYAN;

    private final boolean isInternal;

    private volatile JFrame test;
    private volatile JDialog dialog;
    private volatile JInternalFrame frame;
    private final JDesktopPane parentPane = new JDesktopPane();
    private final JButton run = new JButton("run");

    private final ExtendedRobot robot = new ExtendedRobot();

    private static BufferedImage getSquare(int sz, Color c) {

        BufferedImage img = new BufferedImage(sz, sz, BufferedImage.TYPE_INT_RGB);
        Graphics g = img.getGraphics();
        g.setColor(c);
        g.fillRect(0, 0, sz, sz);
        return img;
    }

    private static Icon getIcon() {

        return null;
        /*BaseMultiResolutionImage mri = new BaseMultiResolutionImage(
                new BufferedImage[]{getSquare(16, C1X), getSquare(32, C2X)});
        return new ImageIcon(mri);*/
    }

    public MultiResolutionJOptionPaneIconTest(boolean internal,
                                              UIManager.LookAndFeelInfo lf) throws Exception {

        UIManager.setLookAndFeel(lf.getClassName());

        isInternal = internal;
        robot.setAutoDelay(50);
        SwingUtilities.invokeAndWait(this::UI);
        try {
            Thread.sleep(2000);
        } catch (Exception e) {}
    }

    private void UI() {

        test = new JFrame();
        test.setLayout(new BorderLayout());
        test.add(parentPane, BorderLayout.CENTER);
        run.addActionListener(this);
        test.add(run, BorderLayout.SOUTH);
        test.setUndecorated(true);
        test.setSize(400, 300);
        test.setLocation(50, 50);
        test.setOpacity(1.0f);
        test.setVisible(true);
    }

    private void disposeAll() {

        if (dialog != null) { dialog.dispose(); }
        if (frame  != null) {  frame.dispose(); }
        if (test   != null) {   test.dispose(); }
    }

    public void doTest() throws Exception {

        robot.waitForIdle(1000);
        clickButton(robot);
        robot.waitForIdle(2000);

        Component c = isInternal ?
                frame.getContentPane() : dialog.getContentPane();

        System.out.println("\ncheck " + (isInternal ? "internal frame" :
                "dialog") + " icon:");

        Point pt = c.getLocationOnScreen();
        System.out.println(pt);
        System.out.println(c.getWidth() + " " + c.getHeight());
        try {
            checkColors(pt.x, c.getWidth(), pt.y, c.getHeight());
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            robot.waitForIdle();
            SwingUtilities.invokeAndWait(this::disposeAll);
            robot.waitForIdle();
        }

        System.out.println("ok");
    }

    private static boolean compareColors(final Color c, final Color expected) {
        if (c.equals(expected)) {
            return true;
        } else {
            int rc = c.getRed();
            int re = expected.getRed();
            if ((re == 0 && rc < 50) || (rc <= 0.95 * re && rc >= 0.95*re)) {
                int gc = c.getGreen();
                int ge = expected.getGreen();

                if (gc <= 0.95 * ge && gc >= 0.95 * ge) {
                    int bc = c.getBlue();
                    int be = expected.getBlue();

                    return ((be == 0 && bc <= 50) || (bc <= 0.95*be && bc >= 0.95*be));
                }
            }
            return false;
        }
    }
    private void checkColors(int x0, int w, int y0, int h) {

        boolean is2x = "2".equals(System.getProperty("sun.java2d.uiScale"));
        Color expected = is2x ? C2X : C1X,
                unexpected = is2x ? C1X : C2X;

        System.out.println("Expected color: " + expected);
        System.out.println("Unexpected color: " + unexpected);

        for (int y = y0; y < y0 + h; y += 5) {
            for (int x = x0; x < x0 + w; x += 5) {

                Color c = robot.getPixelColor(x, y);
                System.out.println("Actual color: " + c);
                if (c.equals(unexpected)) {
                    throw new RuntimeException(
                            "invalid color was found, test failed");
                } else if (compareColors(c, expected)) { return; }
            }
        }

        // no icon found at all
        throw new RuntimeException("the icon wasn't found");
    }

    private void showDialogOrFrame() {

        JOptionPane pane = new JOptionPane("",
                JOptionPane.DEFAULT_OPTION,
                JOptionPane.INFORMATION_MESSAGE,
                getIcon());
        pane.setOptions(new Object[]{}); // no buttons

        if (isInternal) {
            frame = pane.createInternalFrame(parentPane, "");
            frame.setLocation(0, 0);
            frame.setVisible(true);
        } else {
            dialog = pane.createDialog(parentPane, "");
            dialog.setVisible(true);
        }
    }

    public void clickButton(ExtendedRobot robot) {

        Point pt = run.getLocationOnScreen();
        robot.mouseMove(pt.x + run.getWidth() / 2, pt.y + run.getHeight() / 2);
        robot.waitForIdle();
        robot.click();
        robot.waitForIdle();
        try {
            Thread.sleep(2000);
        } catch (Exception e) {}
    }

    @Override
    public void actionPerformed(ActionEvent event) { showDialogOrFrame(); }


    public static void main(String[] args) throws Exception {

        for (UIManager.LookAndFeelInfo LF: UIManager.getInstalledLookAndFeels()) {
            System.out.println("\nL&F: " + LF.getName());
            (new MultiResolutionJOptionPaneIconTest(false, LF)).doTest();
            (new MultiResolutionJOptionPaneIconTest(true , LF)).doTest();
        }
    }
}
