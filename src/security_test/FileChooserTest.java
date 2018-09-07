package security_test;

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
 * @key closed-security
 * @bug 8196546
 * @requires (os.family == "Windows")
 * @summary Checks that shortcuts pointing to inaccessible files
 *          filtered out of the folder being listed.
 * @run main/manual/othervm/policy=filechooser.policy security_test.FileChooserTest
 */
import javax.swing.filechooser.FileSystemView;
import java.io.File;
import java.util.ArrayList;

public final class FileChooserTest {
    private static ArrayList<String> list = new ArrayList<String>(500);

    public static void main(String[] args) {
        FileSystemView fsv = FileSystemView.getFileSystemView();
        File[] roots = fsv.getRoots();
        test(fsv, roots, 0);
    }

    private static void test(FileSystemView fsv, File[] dirs, int step) {
        if (step++ > 2) {
            return;
        }
        for (final File dir : dirs) {
            if (dir.isFile() || list.contains(dir.getPath())) {
                continue;
            }
            list.add(dir.getPath());
            test(fsv, fsv.getFiles(dir, true), step);
        }
    }
}
