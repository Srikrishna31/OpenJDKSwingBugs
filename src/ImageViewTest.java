/*
 * Copyright (c) 2018, Oracle and/or its affiliates. All rights reserved.
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

/**
 * @test
 * @key headful
 * @bug 8195095
 * @summary Tests if Images are scaled correctly in JEditorPane.
 * @run main ImageViewTest
 */

import javax.swing.*;
import javax.swing.text.EditorKit;
import javax.swing.text.View;
import javax.swing.text.html.HTMLEditorKit;
import java.awt.*;

public class ImageViewTest {

    private static final int WIDTH = 200;
    private static final int HEIGHT = 200;
    private static JFrame f;

    private static JEditorPane editorPane1;
    private static JEditorPane editorPane2;
    private static JEditorPane editorPane3;
    private static JEditorPane editorPane4;

    private static void test(Robot r, JEditorPane editorPane)  throws Exception {

        SwingUtilities.invokeAndWait(() -> {
            f = new JFrame();
            editorPane.setEditable(false);
            f.add(editorPane);
//            f.setSize(220,240);
            f.setLocationRelativeTo(null);

            editorPane.getUI().getPreferredSize(editorPane);
            f.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
            f.setVisible(true);
        });

        r.waitForIdle();
        r.delay(500);

        SwingUtilities.invokeAndWait(() -> {
            Insets insets = editorPane.getInsets();
            Point loc = editorPane.getLocationOnScreen();


            //editorPane.setEditorKit();
            final Color black = Color.BLACK;
            final int offset = 10;

            Color center = r.getPixelColor(loc.x + insets.left + WIDTH / 2,
                                            loc.y + insets.top + HEIGHT / 2);
            Color left = r.getPixelColor(loc.x + insets.left + offset,
                                            loc.y + insets.top + HEIGHT / 2);
            Color right = r.getPixelColor(loc.x + insets.left + WIDTH - offset,
                                            loc.y + insets.top + HEIGHT / 2);
            Color bottom = r.getPixelColor(loc.x + insets.left + WIDTH / 2,
                                            loc.y + insets.top + HEIGHT - offset);
            Color top = r.getPixelColor(loc.x + insets.left + WIDTH / 2,
                                            loc.y + insets.top + offset);

            View rootView = editorPane.getUI().getRootView(editorPane);
            System.out.println(editorPane.getUI().getPreferredSize(editorPane));
            System.out.println(rootView.getPreferredSpan(View.X_AXIS));
            System.out.println(rootView.getPreferredSpan(View.Y_AXIS));

            rootView.getContainer();
            Dimension dim = new Dimension();

            System.out.println(rootView.getContainer().getPreferredSize());
            for (int i = 0; i <rootView.getViewCount(); i++) {
                System.out.println(rootView.getView(i).getPreferredSpan(View.Y_AXIS));

                View v = rootView.getView(i);
                System.out.println(v.getContainer().getPreferredSize());
                System.out.println(v.getElement().getName());
                v.getElement();
            }
//            System.out.println(elem.getName());
            f.dispose();

            System.out.println("center color: " + center);
            System.out.println("left color: " + left);
            System.out.println("right color: " + right);
            System.out.println("bottom color: " + bottom);
            System.out.println("top color: " + top);

            if (!(black.equals(center) || black.equals(left) || black.equals(right) ||
                    black.equals(top) || black.equals(bottom))) {
                throw new RuntimeException("Test failed: Image not scaled correctly");
            }
        });

        r.waitForIdle();
    }

    public static void main(String[] args) throws Exception {

        final String ABSOLUTE_FILE_PATH = ImageViewTest.class.getResource("anotherCircle.png").getPath();

        System.out.println(ABSOLUTE_FILE_PATH);

        Robot r = new Robot();

        SwingUtilities.invokeAndWait(() -> {
            editorPane1 = new JEditorPane("text/html",
                    "<img height=\"200\" src=\"file:///" + ABSOLUTE_FILE_PATH + "\"");

            HTMLEditorKit editorKit = (HTMLEditorKit)editorPane1.getEditorKit();

            EditorKit kit = editorPane1.getEditorKitForContentType(editorKit.getContentType());

            View root = editorPane1.getUI().getRootView(editorPane1);
            System.out.println(editorPane1.getUI().getRootView(editorPane1).getElement().getElement(0));
//            editorPane2 = new JEditorPane("text/html",
//                    "<img width=\"100\" src=\"file:///" + ABSOLUTE_FILE_PATH + "\"");
//
//
//
//            editorPane3 = new JEditorPane("text/html",
//                    "<img src=\"file:///" + ABSOLUTE_FILE_PATH + "\"");
//
//            editorPane4 = new JEditorPane("text/html",
//                    "<img width=\"200\" height=\"400\" src=\"file:///" + ABSOLUTE_FILE_PATH + "\"");
        });

        r.waitForIdle();

        System.out.println("Test with only height set to 200");
        test(r, editorPane1);

        sleep (1000);
        System.out.println("Test with only width set to 200");
        test(r, editorPane2);

        sleep (1000);
        System.out.println("Test with none of them set");
        test(r, editorPane3);
        sleep (1000);
        System.out.println("Test with both of them set to 200");
        test(r, editorPane4);
        sleep (1000);
        System.out.println("Test Passed.");
    }

    static void sleep (int time) {
        try {
            Thread.sleep(time);
        } catch (InterruptedException e) {

        }
    }
}
