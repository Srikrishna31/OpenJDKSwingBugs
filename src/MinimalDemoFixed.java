/**
 * Bug: JDK-8187936
 * References:
 * http://docs.oracle.com/javase/tutorial/displayCode.html?code=http://docs.oracle.com/javase/tutorial/uiswing/examples/components/DynamicTreeDemoProject/src/components/DynamicTree.java
 * http://docs.oracle.com/javase/tutorial/uiswing/events/treemodellistener.html
 */

import javax.swing.*;
import javax.swing.event.TreeModelEvent;
import javax.swing.event.TreeModelListener;
import javax.swing.plaf.BorderUIResource;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreePath;

public class MinimalDemoFixed {
    public static void main(final String[] args) throws Exception {
        final DefaultMutableTreeNode root = new DefaultMutableTreeNode("Root");
        final DefaultTreeModel model = new DefaultTreeModel(root);
        final JTree tree = new JTree(model);

        final DefaultMutableTreeNode node1 = new DefaultMutableTreeNode("Node 1");
        root.add(node1);
        root.add(new DefaultMutableTreeNode("Node 2"));
        root.add(new DefaultMutableTreeNode("Node 3"));

        model.addTreeModelListener(new TreeModelListener() {
            @Override
            public void treeNodesChanged(final TreeModelEvent event) {
                //Do nothing.
            }

            @Override
            public void treeNodesInserted(final TreeModelEvent event) {
                SwingUtilities.invokeLater(() -> {
                    final TreePath pathToLastInsertedChild =
                            event.getTreePath().pathByAddingChild(event.getChildren()[event.getChildren().length - 1]);
                    tree.setSelectionPath(pathToLastInsertedChild);
                });

//                final TreePath pathToLastInsertedChild =
//                        event.getTreePath().pathByAddingChild(event.getChildren()[event.getChildren().length - 1]);
//                tree.setSelectionPath(pathToLastInsertedChild);
            }

            @Override
            public void treeNodesRemoved(final TreeModelEvent event) {
                SwingUtilities.invokeLater(() -> tree.setSelectionPath(event.getTreePath()));

//                tree.setSelectionPath(event.getTreePath());
            }

            @Override
            public void treeStructureChanged(final TreeModelEvent event) {
                // Do nothing.
            }
        });

        // Automated addition/removal of a child node. 
        final DefaultMutableTreeNode childNode = new DefaultMutableTreeNode("child");
        final Timer insertTimer = new Timer(5000, event -> model.insertNodeInto(childNode, node1, 0));
        insertTimer.setRepeats(false);
        insertTimer.start();
        final Timer removeTimer = new Timer(10000, event -> model.removeNodeFromParent(childNode));
        removeTimer.setRepeats(false);
        removeTimer.start();

        final JFrame mainFrame = new JFrame();
        mainFrame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
//        UIManager.setLookAndFeel("javax.swing.plaf.metal.MetalLookAndFeel");
        UIManager.setLookAndFeel("javax.swing.plaf.nimbus.NimbusLookAndFeel");
//          UIManager.setLookAndFeel("javax.swing.plaf.multi.MultiLookAndFeel");
//        UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
        mainFrame.setContentPane(tree);
        mainFrame.setSize(640, 480);
        mainFrame.setLocationRelativeTo(null);
//        mainFrame.getRootPane().setBorder(BorderUIResource.getEtchedBorderUIResource());
        mainFrame.setVisible(true);

        SwingUtilities.updateComponentTreeUI(mainFrame);

        final Timer skinChangeTimer = new Timer(10000, event -> {
            try {
                  UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
//                UIManager.setLookAndFeel("javax.swing.plaf.nimbus.NimbusLookAndFeel");
            } catch (Exception e) {
                throw new RuntimeException(e.getCause());
            }
            SwingUtilities.updateComponentTreeUI(mainFrame);
        });
        skinChangeTimer.setRepeats(false);
        skinChangeTimer.start();

    }
}
