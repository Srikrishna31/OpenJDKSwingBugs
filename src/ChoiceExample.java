import java.awt.Choice;
import java.awt.Frame;
import java.awt.EventQueue;

public class ChoiceExample
{
    private static Choice c;
    private static Frame f;
    ChoiceExample(){
        f= new Frame();
        c = new Choice();
        c.setBounds(100,100, 75,75);
        c.add("Item 1");
        c.add("Item 2");
        c.add("Item 3");
        c.add("Item 4");
        c.add("Item 5");
        f.add(c);
        f.setSize(400,400);
        f.setLayout(null);
        f.setVisible(true);
    }

    private static  void sleep(int ms) {
        try {
            Thread.sleep(ms);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static void main(String args[]) throws Exception
    {
        EventQueue.invokeAndWait(() -> {
            ChoiceExample ce = new ChoiceExample();
            System.out.println(c.getSelectedIndex());
            sleep(10000);
            c.remove("Item 1");
            System.out.println(c.getSelectedIndex());

            sleep(10000);

            //c.select(c.getItemCount() - 1);
            c.select("Item 5");
            System.out.println(c.getSelectedIndex());
            f.repaint();
            sleep(5);
            c.remove("Item 4");
            System.out.println(c.getSelectedIndex());
            f.repaint();
        });

        sleep(5000);

        EventQueue.invokeAndWait(() -> {
            f.dispose();
        });
    }
}
