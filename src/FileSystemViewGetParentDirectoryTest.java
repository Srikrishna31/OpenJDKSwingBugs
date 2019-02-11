/*
 * Copyright (c) 2019, Oracle and/or its affiliates. All rights reserved.
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
 * @bug 8193761
 * @summary Checks that FileSystemView::getParentDirectory doesn't throw
 * SecurityException when there are not enough privileges to read the directory.
 * @run main/manual/othervm/policy=getparentdirectory.policy FileSsytemViewGetParentDirectoryTest
 */
import javax.swing.filechooser.FileSystemView;
import java.io.File;

public class FileSystemViewGetParentDirectoryTest {
    public static void main(String[] args) {
        FileSystemView fsv = FileSystemView.getFileSystemView();
        String homeDirPath = System.getProperty("user.home");
        File userHome = fsv.getParentDirectory(new File(homeDirPath));
        System.out.println(userHome);
        try {
            File parent = fsv.getParentDirectory(userHome);
            System.out.println("Test passed: SecurityException not received when navigating to parent folder");
        } catch (SecurityException e) {
            throw new RuntimeException("Test failed: Received SecurityException when navigating to parent folder");
        }

    }
}
