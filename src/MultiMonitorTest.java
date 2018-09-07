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

/*
 * @test
 * @key headful
 * @requires os.family=="windows"
 * @bug 8176359
 * @summary tests that Windows are maximized as per the monitors' dimensions,
 * on which it is going to be displayed.
 * @run main/othervm MultiMonitorTest
 * @run main/othervm -Dsun.java2d.uiScale=1.25 MultiMonitorTest
 * @run main/othervm -Dsun.java2d.uiScale=1.5 MultiMonitorTest
 * @run main/othervm -Dsun.java2d.uiScale=1.75 MultiMonitorTest
 */
import java.awt.GraphicsEnvironment;
import java.awt.GraphicsConfiguration;
import java.awt.GraphicsDevice;
import java.awt.Rectangle;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;
import java.util.stream.IntStream;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;


public class MultiMonitorTest {
    private static CyclicBarrier barrier = new CyclicBarrier(2);

    private static void validateBounds(JFrame f, GraphicsDevice screen) {
        final Rectangle screenBounds = screen.getDefaultConfiguration().getBounds();
        final Rectangle frameBounds = f.getBounds();
        final Rectangle contentBounds = f.getContentPane().getBounds();
        final double scaleX = screen.getDefaultConfiguration().getDefaultTransform().getScaleX();
        final double scaleY = screen.getDefaultConfiguration().getDefaultTransform().getScaleY();
        System.out.println("Testing for Screen: " + screen.getIDstring());
        System.out.println("Device bounds: " + screenBounds);
        System.out.println("Content bounds: " + contentBounds);
        System.out.println("Frame bounds: " + frameBounds);
        System.out.println("Frame insets: " + f.getInsets() + "\n");
        System.out.println("X - Scale: " + scaleX);
        System.out.println("Y - Scale: " + scaleY);
        try {
            if (frameBounds.x != screenBounds.x || frameBounds.y != screenBounds.y ||
                    Math.abs(frameBounds.width - screenBounds.width) > 1.0 ||
                    Math.abs(frameBounds.height - screenBounds.height) > 1.0) {
                System.out.println(Math.abs(frameBounds.width * scaleX - screenBounds.width));
                System.out.println(Math.abs(frameBounds.height * scaleY - screenBounds.height));
                throw new RuntimeException("Test case failed. Frame bounds (" +
                        frameBounds + ") are not equal to Screen " + "bounds (" + screenBounds + ") for Screen" + screen
                        .getIDstring());
            }
        } finally {
            awaitBarrier();
        }
    }

    private static void awaitBarrier() {
        try {
            barrier.await();
        } catch (InterruptedException | BrokenBarrierException e) {
            throw new RuntimeException(e);
        }
    }

    public static void main(String[] args) {
        GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
        final GraphicsDevice[] gs = ge.getScreenDevices();
        JFrame fs[] = new JFrame[1];

        IntStream.range(0, gs.length).forEach(i -> {
            try {
                SwingUtilities.invokeAndWait(() -> {
                    JFrame f = new JFrame(gs[i].getDefaultConfiguration());
                    fs[0] = f;
                    GraphicsConfiguration gc = f.getGraphicsConfiguration();
                    Rectangle maxBounds = gc.getBounds();

                    gs[i].getIDstring();
                    f.setMaximizedBounds(maxBounds);
                    f.setTitle("Screen " + i + " " + maxBounds);
                    f.setExtendedState(JFrame.MAXIMIZED_BOTH);
                    f.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                    f.setUndecorated(true);
                    WindowListener l = new WindowAdapter() {
                        @Override
                        public void windowOpened(WindowEvent e) {
                            validateBounds(f, gs[i]);
                        }
                    };
                    f.addWindowListener(l);
                    f.setVisible(true);
                    try {
                        Thread.sleep(5000);
                    }catch (InterruptedException e) {

                    }
                });
            } catch (Exception e) {
                throw new RuntimeException(e);
            } finally {
                awaitBarrier();
                if (fs[0] != null) {
                    fs[0].setVisible(false);
                    fs[0].dispose();
                    fs[0] = null;
                }
                barrier.reset();
            }
        });
    }
}
