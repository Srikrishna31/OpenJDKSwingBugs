import java.awt.*;
import java.awt.event.*;
import java.awt.font.*;
import java.util.*;

public class FontChangeTest extends Frame
        implements ActionListener, ItemListener {
    Panel p;
    java.util.List list = Arrays.asList(
            "1","2","3","4","5","6","7","8","9");
    TextField[] textfs = new TextField[list.size()];;
    Choice fontName, fontStyle;
    TextField fontSize;
    final static String[] fontStyles = new String[]{
            "Plain","Bold","Italic","ItalicBold"};
    final static int[] fontStylesId = new int[]{
            Font.PLAIN,Font.BOLD,Font.ITALIC,Font.BOLD+Font.ITALIC};
    Button btn;
    final int fSize = 12;
    void shuffle() {
        Collections.shuffle(list);
        for(int i=0; i<list.size(); i++) {
            textfs[i].setText(list.get(i).toString());
        }
    }
    void init() {
        setLayout(new BorderLayout());
        Panel p0 = new Panel();
        p0.setLayout(new FlowLayout());
        add(p0, BorderLayout.NORTH);
        btn = new Button("Shuffle");
        btn.addActionListener(this);
        p0.add(btn);
        Panel p1 = new Panel();
        p1.setLayout(new FlowLayout());
        add(p1, BorderLayout.SOUTH);
        fontName = new Choice();
        fontName.addItemListener(this);
        for(String s : Toolkit.getDefaultToolkit().getFontList()) {
            fontName.add(s);
        }
        p1.add(fontName);
        fontStyle = new Choice();
        fontStyle.addItemListener(this);
        for(String s : fontStyles) {
            fontStyle.add(s);
        }
        p1.add(fontStyle);
        fontSize = new TextField(String.valueOf(fSize),2);
        fontSize.addActionListener(this);
        p1.add(fontSize);
        p = new Panel();
        add(p, BorderLayout.CENTER);
        p.setLayout(new GridLayout(0,3,3,3));
        for(int i=0; i<list.size(); i++) {
            textfs[i] = new TextField(1);
            textfs[i].setFont(new Font(fontName.getSelectedItem(),
                    fontStylesId[fontStyle.getSelectedIndex()],
                    fSize));
            p.add(textfs[i]);
        }
        shuffle();
    }
    public void changeFont() {
        int size;
        try {
            size = Integer.parseInt(fontSize.getText());
            for(int i=0; i<textfs.length; i++) {
                textfs[i].setFont(new Font(fontName.getSelectedItem(),
                        fontStylesId[fontStyle.getSelectedIndex()],
                        size));
            }
        } catch (java.lang.NumberFormatException nfe) {
        }
    }
    FontChangeTest() {
        super("FontChangeTest");
        init();
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent we) { System.exit(0); }
        });
        pack();
        setVisible(true);
    }
    public void actionPerformed(ActionEvent ae) {
        if (ae.getSource().equals(btn)) {
            p.setVisible(false);
            shuffle();
            p.setVisible(true);
        } else if (ae.getSource().equals(fontSize)) {
            changeFont();
            pack();
        }
    }
    public void itemStateChanged(ItemEvent ie) {
        changeFont();
        pack();
    }
    public static void main(String[] args) {
        new FontChangeTest();
    }
}
