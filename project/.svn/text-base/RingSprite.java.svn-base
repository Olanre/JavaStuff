import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Line2D;

/**
 * RingSprite contins three nested circles.
 *
 * @author Olanrewaju Okunlola
 */
public class RingSprite {
    long score = 0; 
    private Ellipse2D ring1;
    private Ellipse2D ring2;
    private Ellipse2D ring3;
    private Ellipse2D ring4;
    private Color[] ringColours = {Color.RED, Color.YELLOW, Color.BLUE};
    private int colourIndex = 0;
    public PlayerForm playerform = new PlayerForm();

    /**
     * RingSprite is a nested set of rings.
     *
     * @param w width of the box containing the rings
     * @param h heigt of the box containing the rings
     */
    public RingSprite( int w, int h ) {
         ring1 = new Ellipse2D.Double( 220, 330, w, h);
         ring2 = new Ellipse2D.Double( 230, 340, w-20, h-20);
         ring3 = new Ellipse2D.Double( 240, 350, w-40, h-40);
         ring4 = new Ellipse2D.Double( 250, 360, w-60, h-60);
    }

    /**
     * nextColor - return the next colour in the array.
     *
     * @return the next colour
     */
    private Color nextColor() {
        colourIndex++;
        if ( colourIndex >= ringColours.length ) {
            colourIndex = 0;
        }
        return ringColours[ colourIndex ];
    }

    /**
     * draw - draws the sprite
     *
     * @param g2d the drawing object
     */
    void draw(Graphics2D g2d ){
        
        // move to the location of the sprite 
        g2d.setColor(nextColor() );
        g2d.draw( ring1 );
        g2d.setColor(nextColor() );
       g2d.draw( ring2 );
       g2d.setColor(nextColor() );
        g2d.draw( ring3 );
        g2d.setColor(nextColor() );
        g2d.draw( ring4 );
         // reset the translate
     
    }

    /**
     * changeConfiguration changes the colours of the rings.
     */
    void changeConfiguration() {
        // skip a colour
        nextColor();
          
    }
    /** Deals with collision detection and changes trajectory of the ball
     *@param ball which is a Ball object
     */ 
    public void hit( Ball ball ) { 
        double bx = ball.getX();
        double by = ball.getY();
        double dx = ball.getDisplacementX();
        double dy = ball.getDisplacementY();
        Line2D.Double dir = new Line2D.Double(bx,by,bx+dx,by+dy);
        if ( ring1.contains( bx, by ) ) {
            ball.setDisplacementY( Ball.nextDisplacement() );
            score += 100;  
            playerform.getScore(); 
        }
    }
    /**Method the return the points which a long*/
    public long points(){
        return score;
   }
   /* Deals with moving the sprites width and height by set amounts*/
   public void move(){
       boolean k = true;
        if(k){
            RingSprite r1 = new RingSprite( 120, 120);
            k =  false;
        }
        else {
            RingSprite r2 = new RingSprite(100, 100);
            k = true; 
        }
    } 
}
