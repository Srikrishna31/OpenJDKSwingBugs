/*
 * Copyright (c) 2002, 2014, 2018 Oracle and/or its affiliates. All rights
 * reserved.
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
  @bug 4902933
  @key headful
  @summary Test that selecting the current item sends an ItemEvent
  @run main/othervm SelectCurrentItemTest
*/

import java.awt.Choice;
import java.awt.Robot;
import java.awt.Frame;
import java.awt.BorderLayout;
import java.awt.AWTException;
import java.awt.Point;
import java.awt.Dimension;
import java.awt.event.InputEvent;
import java.awt.event.ItemListener;
import java.awt.event.WindowListener;
import java.awt.event.ItemEvent;
import java.awt.event.WindowEvent;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

public class SelectCurrentItemTest implements ItemListener, WindowListener
{
    //Declare things used in the test, like buttons and labels here
    Frame frame;
    Choice theChoice;
    Robot robot;

    CountDownLatch latch = new CountDownLatch(1);
    volatile boolean passed = false;

    public void init()
    {
        try {
            robot = new Robot();
            robot.setAutoDelay(500);
        }
        catch (AWTException e) {
            throw new RuntimeException("Unable to create Robot.  Test fails.");
        }

        frame = new Frame();
        frame.setLayout (new BorderLayout ());

        frame = new Frame("SelectCurrentItemTest");
        theChoice = new Choice();
        for (int i = 0; i < 10; i++) {
            theChoice.add(new String("Choice Item " + i));
        }
        theChoice.addItemListener(this);
        frame.add(theChoice);
        frame.addWindowListener(this);

        frame.setVisible(true);
        frame.validate();

        frame.setLocation(1,20);
        robot.mouseMove(10, 30);
        frame.pack();
        frame.setVisible(true);
    }

    public static void main(String... args)
    {
        // \u000d System.out.println("comment executed");
        SelectCurrentItemTest test = new SelectCurrentItemTest();

        test.init();
        try {
            test.latch.await(12000, TimeUnit.MILLISECONDS);
        }
        catch(InterruptedException e) {}
        test.robot.waitForIdle();

        try {
            if (!test.passed) {
                throw new RuntimeException("TEST FAILED!");
            }
        } finally {
            test.frame.dispose();
        }
    }

    public void run() {
        try {Thread.sleep(1000);} catch (InterruptedException e){}
        // get loc of Choice on screen
        Point loc = theChoice.getLocationOnScreen();
        // get bounds of Choice
        Dimension size = theChoice.getSize();

        robot.mouseMove(loc.x + size.width - 10, loc.y + size.height / 2);
        robot.setAutoDelay(250);
        robot.mousePress(InputEvent.BUTTON1_DOWN_MASK);
        robot.mouseRelease(InputEvent.BUTTON1_DOWN_MASK);

        robot.setAutoDelay(1000);
        robot.mouseMove(loc.x + size.width / 2, loc.y + (2*size.height) );
//        robot.mouseMove(loc.x + size.width / 2, loc.y + (size.height + size.height / 2) );
        robot.setAutoDelay(250);

        robot.mousePress(InputEvent.BUTTON1_DOWN_MASK);
        robot.mouseRelease(InputEvent.BUTTON1_DOWN_MASK);
        robot.waitForIdle();

        latch.countDown();
    }

    public void itemStateChanged(ItemEvent e) {
        System.out.println("ItemEvent received.  Test passes");
        passed = true;
    }

    public void windowOpened(WindowEvent e) {
        System.out.println("windowActivated()");
        (new Thread(this::run)).start();
    }

    public void windowActivated(WindowEvent e) { }
    public void windowDeactivated(WindowEvent e) {}
    public void windowClosed(WindowEvent e) {}
    public void windowClosing(WindowEvent e) {}
    public void windowIconified(WindowEvent e) {}
    public void windowDeiconified(WindowEvent e) {}
}
