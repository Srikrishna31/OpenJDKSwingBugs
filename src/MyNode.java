/**
 * BUG: JDK-8186648
 */

import java.util.Enumeration;
import java.util.Vector;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreeNode;

class MyNode implements TreeNode {
    public boolean isLeaf() { return false; }
    public TreeNode getParent() { return null; }
    public int getIndex(TreeNode node) { return 0; }
    public int getChildCount() { return 0; }
    public TreeNode getChildAt(int childIndex) { return null; }
    public boolean getAllowsChildren() { return false; }
    public Enumeration<MyNode> children() { return null; }
}

class A extends DefaultMutableTreeNode {
    A() {
//        children = new Vector<MyNode>();
    }
} 