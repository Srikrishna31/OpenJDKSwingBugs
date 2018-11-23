/*
JDK-7124280
 */
import javax.swing.JTree;
import javax.swing.JFrame;
import javax.swing.SwingUtilities;
import javax.swing.tree.DefaultMutableTreeNode;

public class TreeDemo {
    private static JFrame f;

    public static void main(String[] args) throws Exception {
        SwingUtilities.invokeAndWait(() -> {
            f=new JFrame();

            DefaultMutableTreeNode style=new DefaultMutableTreeNode("Style");
            DefaultMutableTreeNode color=new DefaultMutableTreeNode("color");
            DefaultMutableTreeNode font=new DefaultMutableTreeNode("font");

            style.add(color);
            style.add(font);

            DefaultMutableTreeNode red=new DefaultMutableTreeNode("red");
            DefaultMutableTreeNode blue=new DefaultMutableTreeNode("blue");
            DefaultMutableTreeNode black=new DefaultMutableTreeNode("black");
            DefaultMutableTreeNode green=new DefaultMutableTreeNode("green");

            color.add(red); color.add(blue); color.add(black); color.add(green);

            JTree jt=new JTree(style);

            f.add(jt);
            f.setSize(200,200);
            f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            f.setVisible(true);
        });
    }}
