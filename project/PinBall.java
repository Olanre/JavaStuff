import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Dimension;
import java.io.IOException;
import java.io.File;
import java.util.Scanner;
import javax.swing.JOptionPane; 
import java.awt.event.KeyListener;
import java.awt.event.KeyEvent;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JPanel;
import javax.swing.JFrame;
/* THe maain game class to animate the features of the game
 * @author Olanrewaju Okunlola
 */
public class PinBall extends JPanel implements ActionListener, KeyListener 
              
    //implements ActionListener, KeyListener, MouseListener
{
   private static final int WINDOW_WIDTH = 600;
   private static final int WINDOW_HEIGHT = 650;
   private static final double BALL_RADIUS = 15;
   private static final int INTERVAL = 100;
   private static final int INTERVAL2 = 500;
   /* class variables and creates and intialized here*/
    private static final Color BALL_COLOR = Color.GRAY;
    private static Paddles paddles = new Paddles();
    private static Launcher launcher = new Launcher();  
    private static RingSprite ringsprite = new RingSprite(100, 100);
    private static SquareSprite squaresprite = new SquareSprite(100, 100); 
    private static Sprites sprites = new Sprites();
    private static PlayerForm playerform = new PlayerForm();
    private static Menu menu = new Menu(); 
    private static final Color BG_COLOR = Color.BLACK;
    int count; 
    private static JFrame frame = new JFrame(); 
    /* ball */
    private Ball ball;    
    /*border*/ 
    private Border border;
    long score = 0; 
    /* the timers*/ 
    private javax.swing.Timer timer;
    private javax.swing.Timer timer2;
       /* keeps current window size */
    private int width = -1;
    private int height = -1;
   

    /** Pinball constructor to create a instance of the pinball object
     *@param count which represents the number of games*/
    public PinBall(int count) { 
        setPreferredSize(
            new Dimension( WINDOW_WIDTH, WINDOW_HEIGHT ) );
        setBackground( BG_COLOR );
        border = new Border( Color.YELLOW);  
        timer = new javax.swing.Timer(INTERVAL, this);
        timer2 = new javax.swing.Timer(INTERVAL2, new Configureme() );
        ball = new Ball( 570, 495, 
                       BALL_RADIUS, BALL_COLOR );
        this.count = count; 
        // 1000ms timer, and Inner class handling 
        
   
        addKeyListener( this );
        setFocusable( true );
        // create timer to update the bouncing
        // remove if not required
        //timer = new javax.swing.Timer(INTERVAL, this);
        //timer.start();

    }
    /** Class configurement contains the relevant action listener
     *to change the color of the sprites
     */
    class Configureme implements ActionListener {
    
       public void actionPerformed(ActionEvent e) {
           sprites.changeConfiguration();    
           repaint();
       }
   }
   /** Single getter method to return the count of the pinball
    *which is an integer*/
   public int getCount(){
       return count;
   }
   /** Method to start the action event timers*/
    public void start() {
        timer.start();
        timer2.start();
    } 
    /* Method to activate any keyListeners*/
    public void activatekeylisteners(){
        addKeyListener( new Paddlechange());
        setFocusable( true );
    }
    /*Method to stop all action event timers*/
    public void stop() {
        timer.stop(); 
        timer2.stop();
    }
    /** class paddlechange contains relevant change to move the paddles
     *of the pinball game up using a keylistener
     */
    class Paddlechange implements KeyListener{
       /** keyPressed moves paddles depending on whether
        *a key is pressed
        *@param e which the KeyEvent
        */
       public void keyPressed(KeyEvent e) {
           switch( e.getKeyChar() ) {
               case 'z':
                   paddles.move1(-30.0);
                   break; 
               case '/':
                   paddles.move2(-30.0);
                   break;
            }
            repaint();
        }
        /** keyReleased moves paddles depending on whether
        *a key is released
        *@param e which the KeyEvent
        */
        public void keyReleased(KeyEvent e) {
           switch( e.getKeyChar() ) {
               case 'z':
                   paddles.move1(30.0);
                   break;
               case '/':
                   paddles.move2(30.0);
                   break;
            }
            repaint();
        }
        /* In case a key is typed, empty*/ 
        public void keyTyped(KeyEvent e) { }
        
   }
   /** keyPressed moves launcher depending on whether
    *a key is pressed
    *@param e which the KeyEvent
    */
   public void keyPressed(KeyEvent e) {
           switch( e.getKeyChar() ) {
               case '\b':
                   launcher.move(20.0);
                   break;
           }
           repaint(); 
   }
   /** keyReleased moves launcher depending on whether
    *a key is released
    *@param e which the KeyEvent
    */
   public void keyReleased(KeyEvent e) {
        switch( e.getKeyChar() ) {
               case '\b':
                   launcher.move(-20.0);
                   for(int i = 0; i<13; i++){ 
                       ball.launchball(20.0); 
                       start(); 
                       repaint(); 
                   }
                   ball.updatePosition( getBounds() );
                   break;
        }
        repaint(); 
    }
    /* In case a key is typed, empty*/ 
    public void keyTyped(KeyEvent e) { }
    
    /* Draws shapes and fills them with color
     *@ param g2d which is a Graphics Object
     * also deals with window resizing
     */          
    protected void paintComponent( Graphics g ) {
        super.paintComponent( g );
        Graphics2D g2d = (Graphics2D)g;
        int w = getWidth(); 
        int h = getHeight();

        if ( w != width || h != height ) {
            width = w; 
            height = h;
        }
        paddles.draw( g2d );
        ball.paint( g2d );
        launcher.draw(g2d);
        sprites.draw(g2d);
        paddles.draw(g2d);
        ringsprite.draw(g2d);
        squaresprite.draw(g2d);
        border.paint(g2d);
    }
     /**
        * code to handle animation.
        *@param evt which is the ActionEvent
        */
     public void actionPerformed( ActionEvent evt ) {        
        
         /* 
         to check if the bottomhit is true then it tells user the round is over
         */
         if( border.bottomhit(ball) == true){
              JOptionPane.showMessageDialog(null, 
                  "Round finished");
              mkGame();
         }
         border.hit(ball); 
         sprites.hit(ball); 
         squaresprite.hit(ball);
         ringsprite.hit(ball); 
         paddles.hit(ball); 
         ringsprite.move(); 
         playerform.getScore();  
         ball.updatePosition( getBounds() );  
         repaint();
    }
    /** setgame ensures the count decrements by one each
     *time the method is called
     *also returns the count
     */
    public int setgame(){
        
        int count2 = count-1;  
        System.out.println(count2);  
        return count2;
   } 
   /** setScore ensures the score increases with the hits
     *also returns the score
     */
   public long setScore(){
        score = sprites.points() + squaresprite.points() + ringsprite.points(); 
        return score;
   } 
   /** mkGame creates a new game if the ball has reached a low positioning in th   e game area
   * also deals with exceptions and saves the score before cancelling the game
   */ 
   public void mkGame(){
       if(setgame()< 1){
           try{
               menu.savescore();
               frame.dispose();
           }
           catch( IOException ex ) {
            System.out.println("Cannot find file");
            System.exit( 1 );
        }
       System.exit(1);
       }
       stop(); 
       PinBall pinball = new PinBall(setgame());
       runApplication(pinball);
       pinball.start(); 
        
       
   }
       
    /** runApplication creates the frame which the game is in and gives it a nam    e and default
     *close operation
     *@param app which is the JPanel object
     */   
    public static void runApplication( JPanel app ) {
       /* create frame */
       
 
       frame.setSize( app.getPreferredSize() ); 
       frame.setTitle( app.getClass().getName() );
       frame.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
       frame.add( app );
       frame.setVisible( true ); 
    }

    /*
     * Create the frame to show the application
     */
    public static void main( ) {
        PinBall application = new PinBall( playerform.numofGame()); 
        runApplication( application ); 
    }
}
