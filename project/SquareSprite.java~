import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.RoundRectangle2D;
import java.awt.Graphics;
import java.awt.geom.Line2D;


/**
 * SquaresSprite contains four rounded rectangles in a grid.
 * @author Olanrewaju Okunlola
 */
public class SquareSprite{

    private RoundRectangle2D rect1;
    private RoundRectangle2D rect2;
    private RoundRectangle2D rect3;
    private RoundRectangle2D rect4;
    public PlayerForm playerform = new PlayerForm(); 
    long score = 0; 

    private Color[] rectColours = {Color.BLUE, Color.RED, Color.YELLOW};
    private int colourIndex = 0;

    /**
     * SquaresSprite - build the sprite
     *
     * @param w the width of the sprite
     * @param h the width of the sprite
     */
    public SquareSprite( int w, int h ) {
         
         
         rect1 = new RoundRectangle2D.Double( 210.0, 100.0, w, h, 5.0, 5.0 );
         rect2 = new RoundRectangle2D.Double( 220.0, 110, w-20, h-20, 5.0, 5.0 );
         rect3 = new RoundRectangle2D.Double( 230, 120.0, w-40, h-40, 5.0, 5.0 );
         rect4 = new RoundRectangle2D.Double( 240.0, 130.0, w-60, h-60, 5.0, 5.0 );
    }

    /**
     * nextColor - return the next colour in the array.
     *
     * @return the next colour
     */
    private Color nextColor() {
        colourIndex++;
        if ( colourIndex >= rectColours.length ) {
            colourIndex = 0;
        }
        return rectColours[ colourIndex ];
    }

    /**
     * draw - draws the sprite
     *
     * @param g2d the drawing object
     */
    void draw(Graphics2D g2d){
        
        // move to the location of the sprite
        
       g2d.setColor(nextColor() );
        g2d.draw( rect1 );
        g2d.setColor(nextColor() );
       g2d.draw( rect2 );
       g2d.setColor(nextColor() );
        g2d.draw( rect3 );
        g2d.setColor(nextColor() );
        g2d.draw( rect4 );
        // reset the translate
       // g2d.translate( -tx, -ty );
    }

    /**
     * changeConfiguration requests that drawing configuration of
     * the sprite should change.
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
        if ( rect1.contains( bx, by) ) {
            ball.setDisplacementY( -Ball.nextDisplacement() );
            score += 100;  
            playerform.getScore(); 
        }
    }
    /**Method the return the points which a long*/
    public long points(){
        return score;
   }
}
