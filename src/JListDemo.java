
// java Program to create a simple JList 
import java.awt.event.*;
import java.awt.*;
import javax.swing.*;
class JListDemo extends JFrame
{
    //frame 
    private static JFrame f;
    //lists 
    private static JList b;


    //main class 
    public static void main(String[] args) throws Exception
    {
        SwingUtilities.invokeAndWait(() -> {
            f = new JFrame("frame");

            JListDemo s=new JListDemo();

            JPanel p =new JPanel();

            JLabel l= new JLabel("select the day of the week");

            String week[]= { "Monday","Tuesday","Wednesday",
                    "Thursday","Friday","Saturday","Sunday"};

            b= new JList(week);

            b.setSelectedIndex(2);

            //add list to panel
            p.add(b);

            f.add(p);

            //set the size of frame
            f.setSize(400,400);
            f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

            f.setVisible(true);
        });
    }


} 
