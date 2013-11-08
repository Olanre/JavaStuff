import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Dimension;
import java.awt.geom.Ellipse2D;

import java.awt.EventQueue;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

import javax.swing.JPanel;
import javax.swing.JFrame;

import java.util.Random;

/**
 * Draw a bouncing ball.
 */
public class Bouncing extends JPanel implements ActionListener {

    private static final double BALL_W = 20.0;
    private static final double BALL_H = 20.0;

    /* handles updating the animation */
    private javax.swing.Timer timer;

    private Ellipse2D.Double ball =
        new Ellipse2D.Double( 10.0, 10.0, BALL_W, BALL_H );
    private Color ballColor = Color.GREEN;
    private double dx = 5.0; // direction for x move
    private double dy = 5.0; // direction for y move
    private int width = -1;
    private int height = -1;
    private Random random = new Random();

    public Bouncing() {
        setPreferredSize( new Dimension( 300, 300 ) );
        setBackground( Color.black );

        // create timer to update the bouncing
        timer = new javax.swing.Timer(100, this);
        start();
    }

    public void start() {
        timer.start();
    }

    public void stop() {
        timer.stop();
    }

    public void setBallColor( Color c ) {
        ballColor = c;
        repaint();
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
        g2d.setColor ( ballColor );
        g2d.fill ( ball );
    }

    /**
     * Invoked every animation step.
     */
    public void actionPerformed( ActionEvent evt ) {
        double left = 0.0;
        double right = width - BALL_W;
        double top = 0.0;
        double bottom = height - BALL_H;

        if ( (dx + ball.x) > right ) {
            dx = (random.nextDouble() * -9.0) - 1.0;
        }
        else if ( (dx + ball.x) < left ) {
            dx = (random.nextDouble() * 9.0) + 1.0;
        }

        if ( (dy + ball.y) > bottom ) {
            dy = (random.nextDouble() * -9.0) - 1.0;
        }
        else if ( (dy + ball.y) < top ) {
            dy = (random.nextDouble() * 9.0) + 1.0;
        }

        ball.x += dx;
        ball.y += dy;
        repaint();
    }

    /*
     * Create the frame for the bouncing ball
     */
    public static void main( String[] args ) {

        EventQueue.invokeLater(new Runnable() {
            public void run() {
               /* create frame */
               JFrame frame = new JFrame();

               Bouncing bouncing = new Bouncing();
               frame.setSize( bouncing.getPreferredSize() );
               frame.setTitle( bouncing.getClass().getName() );
               frame.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
               frame.add( bouncing );
               frame.setVisible( true );
            }
        });
    }
}
