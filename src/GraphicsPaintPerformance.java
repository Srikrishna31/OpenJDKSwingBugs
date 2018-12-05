/**
 * JDK-8214537
 */

import java.awt.*;
import java.awt.event.*;

class GraphicsPaintPerformance extends Frame // javax.swing.JFrame
{
    int [] x = null;
    int [] y = null;

    public static void main( String [] args )
    {
        new GraphicsPaintPerformance();
    }

    GraphicsPaintPerformance()
    {
        int N = 20000;
        x = new int[N];
        y = new int[N];

        for( int i = 0; i < N; i++ )
        {
            x[i] = (int)( 500 * Math.random() + 50 );
            y[i] = (int)( 500 * Math.random() + 80 );
        }

        setSize( 600, 630 );
        setVisible( true );

        addWindowListener( new WindowAdapter()
        {
            public void windowClosing( WindowEvent we )
            {
                System.exit(0);
            }
        });
    }

    public void paint( Graphics g )
    {
        long start = System.currentTimeMillis();

        int n = x.length;
        for( int i = 1; i < n; i++ )
        {
            g.drawLine( x[i-1], y[i-1], x[i], y[i] );
        }

        g.setColor( Color.WHITE );
        g.fillRect( 250, 280, 100, 100 );


        g.setColor( Color.BLACK );
        g.drawString( 0.001 * ( System.currentTimeMillis() - start ) + " s", 280, 335 );
        System.out.println(0.001 * (System.currentTimeMillis() - start) + " s");
    }
}
