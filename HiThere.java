import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Dimension;

import java.awt.EventQueue;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

import javax.swing.JPanel;
import javax.swing.JFrame;

public class HiThere extends JPanel implements ActionListener {
    /*
     * configuration constants
     */
    private static final int INTERVAL = 500; // 500 ms
    private static final int WINDOW_WIDTH = 400;
    private static final int WINDOW_HEIGHT = 400;
    private static final Color BG_COLOR = Color.WHITE;

    /*
     * handles updating the animation, remove if not required
     */
    private javax.swing.Timer timer;

    /*
     * instance variables
     */
    private Color currentColor = Color.red;
    private Font currentFont = new Font("monospaced", Font.BOLD, 30);
    
    /* keeps current window size */
    private int width = -1;
    private int height = -1;

    public HiThere() {
        setPreferredSize(
            new Dimension( WINDOW_WIDTH, WINDOW_HEIGHT ) );
        setBackground( BG_COLOR );

        // create timer to update the bouncing
        // remove if not required
        timer = new javax.swing.Timer(INTERVAL, this);
        timer.start();
    }

    protected void paintComponent( Graphics g ) {
        super.paintComponent( g );
        Graphics2D g2d = (Graphics2D)g;
        int w = getWidth();
        int h = getHeight();

        if ( w != width || h != height ) {
            width = w;
            height = h;
        }
        /* drawing code */
        g2d.setColor( currentColor );
        g2d.setFont( currentFont );
        g2d.drawString( "HiThere", 10, height/3 );
    }

    /**
     * Invoked every animation step.
     */
    public void actionPerformed( ActionEvent evt ) {
        /*
         * code to handle animation.
         */
        if ( currentColor == Color.red ) {
            currentColor = Color.blue;
        }
        else {
            currentColor = Color.red;
        }
        repaint();
    }

    public static void runApplication( final JPanel app ) {
        EventQueue.invokeLater(
            new Runnable() {
                public void run() {
                    /* create frame */
                    JFrame frame = new JFrame();

                    frame.setSize( app.getPreferredSize() );
                    frame.setTitle( app.getClass().getName() );
                    frame.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
                    frame.add( app );
                    frame.setVisible( true );
                }
            } );
    }

    /*
     * Create the frame to show application
     */
    public static void main( String[] args ) {
        HiThere application = new HiThere();
        runApplication( application );
    }
}
