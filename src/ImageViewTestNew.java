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
 * @bug 8195095 8206238 8208638
 * @summary Tests if Images are scaled correctly in JEditorPane.
 * @run main ImageViewTest
 */

import javax.swing.*;
import javax.swing.text.Element;
import javax.swing.text.View;
import javax.swing.text.ViewFactory;
import javax.swing.text.html.HTMLEditorKit;
import javax.swing.text.html.ImageView;
import java.awt.*;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

public class ImageViewTestNew {
    static class TestEditorKit extends HTMLEditorKit {
        private Data d;
        public TestEditorKit(Data d) {
            super();
            this.d = d;
        }
        private TestViewFactory viewFactory;

        @Override public ViewFactory getViewFactory() {
            if (viewFactory == null) {
                viewFactory = new TestViewFactory();
            }
            return viewFactory;
        }
        class TestViewFactory extends HTMLFactory {
            @Override public View create(Element elem) {
                if (elem.getName().equalsIgnoreCase("img")) {
                    if (d.view == null) {
                        View v = super.create(elem);
                        if (v instanceof ImageView) {
                            System.out.println("ImageView created");
                            d.view = (ImageView)v;
                        }
                    }
                    return d.view;
                }
                else { return super.create(elem); }
            }
        }
    }
    private static JFrame f;
    private static final String ABSOLUTE_FILE_PATH = ImageViewTest.class.getResource("circle.png").getPath();

    private static class Data {
        public String html;
        public String testString;
        public ImageView view;
        public JEditorPane editorPane;
    }
    private static List<Data> infoList = new ArrayList<>();
    private static int threshold = 10;
    private static void test(Robot r, Data data,
                             final int WIDTH, final int HEIGHT )  throws Exception {

        SwingUtilities.invokeAndWait(() -> {
            f = new JFrame();
            data.editorPane.setEditable(false);
            f.add(data.editorPane);
            f.setLocationRelativeTo(null);
            f.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
            data.editorPane.getUI().getPreferredSize(data.editorPane);
            f.setVisible(true);
        });

        r.waitForIdle();
        r.delay(1500);

        SwingUtilities.invokeAndWait(() -> {
            View rootView = data.editorPane.getUI().getRootView(data.editorPane);

            System.out.println("ImageView width height: " + getViewWidthHeight(data));
            System.out.println(data.editorPane.getUI().getPreferredSize(data.editorPane));
            //     Dimension dim = rootView.getContainer().getPreferredSize();
            Dimension dim = data.editorPane.getUI().getPreferredSize(data.editorPane);
            System.out.println("View Width: " + dim.width);
            System.out.println("View Height: " + dim.height);

            final boolean expectedDimensions = Math.abs(dim.width - WIDTH) <= threshold &&
                    Math.abs(dim.height - HEIGHT) <= threshold;
            f.dispose();

            if (!expectedDimensions) {
                throw new RuntimeException("Test failed: Image not scaled correctly");
            }
        });

        r.waitForIdle();
    }

    private static Dimension getViewWidthHeight(Data d) {
        if (d.view != null) {
            Class<? extends Object> cls = d.view.getClass();
            Field[] fields = cls.getDeclaredFields();
            Dimension dim = new Dimension();
            boolean bWidthSeen = false;
            boolean bHeightSeen = false;
            try {
                for (Field field: fields) {
                    if (field.getName().equalsIgnoreCase("width")) {
                        field.setAccessible(true);
                        dim.width = (int)(field.get(d.view));
                        bWidthSeen = true;
                    }

                    if (field.getName().equalsIgnoreCase("height")) {
                        field.setAccessible(true);
                        dim.height = (int)(field.get(d.view));
                        bHeightSeen = true;
                    }

                    if (bWidthSeen && bHeightSeen) {
                        return dim;
                    }
                }
            } catch (IllegalAccessException | IllegalArgumentException e) {
            } finally {
                return dim;
            }
        }

        return null;
    }
    public static void main(String[] args) throws Exception {

        System.out.println(ABSOLUTE_FILE_PATH);

        Robot r = new Robot();
        r.setAutoDelay(500);

        final String[] htmls = new String[] {
                "<img height=\"200\" src=\"file:///" + ABSOLUTE_FILE_PATH + "\"",
                "<img width=\"200\" src=\"file:///" + ABSOLUTE_FILE_PATH + "\"",
                "<img width=\"200\" height=\"200\" src=\"file:///" + ABSOLUTE_FILE_PATH + "\"",
                "<img src=\"file:///" + ABSOLUTE_FILE_PATH + "\"",
                "<img width=\"100\" src =\"file:///" + ABSOLUTE_FILE_PATH + "\"",
                "<img height=\"100\" src =\"file:///" + ABSOLUTE_FILE_PATH + "\"",
                "<img width=\"100\" height=\"100\" src =\"file:///" + ABSOLUTE_FILE_PATH + "\"",
                "<img width=\"50\" src =\"file:///" + ABSOLUTE_FILE_PATH + "\"",
                "<img height=\"50\" src =\"file:///" + ABSOLUTE_FILE_PATH + "\"",
                "<img width=\"300\" src =\"file:///" + ABSOLUTE_FILE_PATH + "\"",
                "<img height=\"300\" src =\"file:///" + ABSOLUTE_FILE_PATH + "\""
        };

        SwingUtilities.invokeAndWait(() -> {
            for (String html: htmls) {
                Data d  = new Data();
                d.html = html;
                d.editorPane = new JEditorPane();
                d.editorPane.setEditorKit(new TestEditorKit(d));
                d.editorPane.setText(html);
                infoList.add(d);
            }
        });

        r.waitForIdle();

        System.out.println("Test with only height set to 200");
        test(r, infoList.get(0), 200, 200);

        System.out.println("Test with only width set to 200");
        test(r, infoList.get(1), 200, 200);

        System.out.println("Test with both of them set");
        test(r, infoList.get(2), 200, 200);

        System.out.println("Test with none of them set to 200");
        test(r, infoList.get(3), 200, 200);

        System.out.println("Test with only width set to 100");
        test(r, infoList.get(4), 100, 100);

        System.out.println("Test with only height set to 100");
        test(r, infoList.get(5), 100, 100);

        System.out.println("Test with both width and height set to 100");
        test(r, infoList.get(6), 100, 100);

        System.out.println("Test with only width set to 50");
        test(r, infoList.get(7), 50, 50);

        System.out.println("Test with only height set to 50");
        test(r, infoList.get(8), 50, 50);

        System.out.println("Test with only width set to 300");
        test(r, infoList.get(9), 300, 300);

        System.out.println("Test with only height set to 300");
        test(r, infoList.get(10), 300, 300);

        System.out.println("Test Passed.");
    }
}
