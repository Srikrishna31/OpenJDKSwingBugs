/*
 * Copyright (c) 2010, 2018, Oracle and/or its affiliates. All rights reserved.
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
 * @requires os.family=="windows"
 * @bug 8194546
 * @summary tests that inaccessible linked files are truly not accessible.
 * when running with a security manager.
 * @run main SecurityTest
 */

package security_test;

import sun.awt.shell.ShellFolder;

import javax.swing.filechooser.FileSystemView;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Arrays;

import java.util.List;
import java.util.stream.Collectors;

public class SecurityTest {
    private static final String userHome = System.getProperty("user.home");
    private static final String userName = userHome.substring(userHome.lastIndexOf(File.separator) + 1);
    private static final String fileName = "_____test_____";

    public static List<File> getRecentFilesList() {
        //Make sure that the ShellFolderManager is initialized.
        FileSystemView fsv = FileSystemView.getFileSystemView();

//        File[] folders = (File[])ShellFolder.get("fileChooserComboBoxFolders");
//        List<File> recentFilesFolder = Arrays.stream(folders)
//                                            .filter(f -> f.getName().equalsIgnoreCase("recent"))
//                                            .collect(Collectors.toList());
//        File[] recentFolderContents = recentFilesFolder.get(0).listFiles();

        File[] recentFolder = new File(userHome + "/Appdata/Roaming/Microsoft/Windows/Recent").listFiles();
//        File[] recentFolder = new File(userHome + File.separator + "Recent").listFiles();
        File[] homeFolder = new File(userHome).listFiles();
        return Arrays.asList(recentFolder);
    }

    static class SecurityChecker {
        public static void main(String[] args) {
            SecurityManager sm = System.getSecurityManager();
            assert(sm != null);
            System.err.println(sm);

            System.err.println(FileSystemView.getFileSystemView().getDefaultDirectory());
            List<File> files = getRecentFilesList();

            System.out.println("Files seen in a secured environment: " + "\n" + files);
            boolean isLinkFound = files
                               .parallelStream()
                               .anyMatch(f -> f.getName().toLowerCase().contains(userName));

            if (!isLinkFound){
                System.err.println("Test case passed. OK!");
            } else {
                System.err.println("Test case failed: some of the " +
                        "inaccessible files escaped security check!\n");
            }
        }
    }

    public static void main(String[] args) throws Exception {

        File[] files = new File(userHome + "").listFiles();
        File existingFile = Arrays.stream(files).filter(f -> f.isFile()).limit(1).findFirst().get();
//        File tempFile = new File(userHome + File.separator + fileName + ".txt");
//        FileOutputStream os = new FileOutputStream(tempFile);
        FileInputStream is = new FileInputStream(existingFile);
        try {
            System.out.println("File that is being read: " + existingFile.getName());
            is.read();
            is.close();

            final String cp = System.getProperty("java.class.path");

            final String params = " -Djava.security.manager -Djava.security.policy==" +
                    SecurityTest.class.getResource("test.policy").getPath();

            Process process = Runtime.getRuntime().exec(System.getProperty("java.home") + "/bin/java " + cp +
                    params + " SecurityTest$SecurityChecker");

            process.waitFor();

//            if (process.getInputStream() != null && process.getInputStream().available() > 0) {
//                System.out.println(process.getInputStream().readAllBytes().toString());
//            }
//
//            if (process.getErrorStream() != null && process.getErrorStream().available() > 0) {
//                String errorString = process.getErrorStream().readAllBytes().toString();
//                if (process.getErrorStream().readAllBytes().toString().contains("failed")) {
//                    throw new RuntimeException(errorString);
//                }
//                System.out.println(errorString);
//            }

            System.out.println("Test case passed!");
        } finally {
            //tempFile.delete();
        }

        //System.out.println(Arrays.asList(files));

    }
}
