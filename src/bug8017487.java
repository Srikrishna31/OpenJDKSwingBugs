/*
 * Copyright (c) 2015, Oracle and/or its affiliates. All rights reserved.
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

/* @test
   @bug 8017487 8167988
   @summary filechooser in Windows-Libraries folder: columns are mixed up
   @author Semyon Sadetsky
   @modules java.desktop/sun.awt.shell
   @run main bug8017487
  */

//import sun.awt.OSInfo;
import sun.awt.shell.ShellFolder;
import sun.awt.shell.ShellFolderColumnInfo;
import javax.swing.filechooser.FileSystemView;
import java.io.File;
import java.util.*;
import java.util.stream.IntStream;

public class bug8017487
{
    public static void main(String[] p_args) throws Exception {
//        if (OSInfo.getOSType() == OSInfo.OSType.WINDOWS &&
//                OSInfo.getWindowsVersion().compareTo(OSInfo.WINDOWS_VISTA) > 0 ) {
//            test();
//            System.out.println("ok");
//        }
        test();
    }

    private static String getString(ShellFolderColumnInfo[] array) {
        List<ShellFolderColumnInfo> val = Arrays.asList(array);
        return val.stream()
                .collect(() -> new StringBuilder(),
                        (b, i) -> b.append(i.getTitle() + ", "),
                        (a, b) -> a.append(b)).toString();
    }

    private static void test() throws Exception {
        FileSystemView fsv = FileSystemView.getFileSystemView();
        File def = new File(fsv.getDefaultDirectory().getAbsolutePath());
        ShellFolderColumnInfo[] defColumns =
                ShellFolder.getShellFolder(def).getFolderColumns();
        Arrays.asList(defColumns).sort(Comparator.comparing(ShellFolderColumnInfo::getTitle));

        File[] files = fsv.getHomeDirectory().listFiles();
        for (File file : files) {
            if( "Libraries".equals(ShellFolder.getShellFolder( file ).getDisplayName())) {
                File[] libs = file.listFiles();
                for (File lib : libs) {
                    ShellFolder libFolder =
                            ShellFolder.getShellFolder(lib);
                    if( "Library".equals(libFolder.getFolderType()) && !(libFolder.getDisplayName().equalsIgnoreCase("Git")) ) {
                        ShellFolderColumnInfo[] folderColumns =
                                libFolder.getFolderColumns();
                        Arrays.asList(folderColumns).sort(Comparator.comparing(ShellFolderColumnInfo::getTitle));

                        System.out.println("Folder Info length: " + folderColumns.length);
                        IntStream.range(0, folderColumns.length)
                                .filter(i -> !defColumns[i].getTitle().equals(folderColumns[i].getTitle()))
                                .findFirst()
                                .ifPresent(i -> {
                                    throw new RuntimeException("Column " +
                                            folderColumns[i].getTitle() +
                                            " doesn't match " +
                                            defColumns[i].getTitle());
                                });

                        System.out.println(getString(defColumns));
                        System.out.println("--------------------");

                        System.out.println(getString(folderColumns));
                        break;
                    }
                }
            }
        }
    }
}
