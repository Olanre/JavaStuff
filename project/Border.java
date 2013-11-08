
import java.awt.Color;
import java.awt.geom.Rectangle2D;
import java.awt.geom.Line2D;
import java.awt.Graphics2D;
import java.awt.geom.Path2D;

/* THe class to create the border of the painball game
 * which the boundary the ball cannot leave. 
 * @author Olanrewaju Okunlola
 */
public class Border {

    /* color of border */
    private Color color;

    /* rectangle used to draw the border */
    private Line2D discleft;
    private Line2D discright;
    private Line2D  botleft;
    private Line2D botright;
    private Line2D top;
    private Line2D bottom;
    private Line2D left;
    private Line2D bar1;
    private Line2D right;
    private Line2D finisher;
    Path2D ramp = new Path2D.Double();
    Path2D ramp2 = new Path2D.Double();
    private Color Color2 = Color.green;
 
    /** Contrcutor to draw the border using graphic objects such as a line and path
    */ 
    public Border(
         Color color )
    {
        this.color = color;
        top = new Line2D.Double(60, 20, 540, 20);
        discleft = new Line2D.Double(60, 20, 20, 60);
        discright = new Line2D.Double(540, 20, 580, 60);
        
        bottom = new Line2D.Double(200, 600, 360, 600);
        left = new Line2D.Double( 20, 60, 20 , 250);
        right = new Line2D.Double(580, 60, 580 ,500);
        bar1 = new Line2D.Double(555, 100, 555, 600);
        
        botright = new Line2D.Double(555, 520, 360, 600);
        botleft = new Line2D.Double(20, 520, 200, 600);
        finisher = new Line2D.Double( 20, 430, 20, 520);
        
        ramp.reset();
        ramp.moveTo(20, 250 );
        ramp.lineTo(60.0, 320 );
        ramp.lineTo(90, 340 );
        ramp.lineTo(90, 370 );
        ramp.lineTo(50, 400 );
        ramp.lineTo(20, 430 );
        
        ramp2.reset();
        ramp2.moveTo(555, 250 );
        ramp2.lineTo(515, 320 );
        ramp2.lineTo(485, 340 );
        ramp2.lineTo(485, 370 );
        ramp2.lineTo(525, 400 );
        ramp2.lineTo(555, 430 );        
      
          
    }
    public boolean bottomhit( Ball ball ) {
        double bx = ball.getX();
        double by = ball.getY();
        double dx = ball.getDisplacementX();
        double dy = ball.getDisplacementY();
        Line2D.Double dir = new Line2D.Double(bx,by,bx+dx,by+dy);
        Line2D.Double bot = new Line2D.Double(10, 570 ,580, 570);
        if ( dir.intersectsLine( bottom ) ) {
            return true; 
        }
        return false; 
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
        Rectangle2D bound = ramp.getBounds2D();
        Rectangle2D bound2 = ramp2.getBounds2D();
        if ( dir.intersectsLine( top ) ) {
            ball.setDisplacementY( Ball.nextDisplacement() );
        }
        else if ( dir.intersectsLine( left ) ) {
            ball.setDisplacementX( Ball.nextDisplacement() );
        }
        else if ( dir.intersectsLine( right ) ) {
            ball.setDisplacementX( -Ball.nextDisplacement() );
        }
        
        else if ( dir.intersectsLine( botleft ) ) {
            ball.setDisplacementY( -Ball.nextDisplacement() );
        }
        else if ( dir.intersectsLine( botright ) ) {
            ball.setDisplacementY( -Ball.nextDisplacement() );
        }
        
        else if ( dir.intersectsLine( discleft ) ) {
            ball.setDisplacementX( Ball.nextDisplacement() );
        }
        else if ( dir.intersectsLine( discright ) ) {
            ball.setDisplacementX( -Ball.nextDisplacement() );
        }
        else if ( dir.intersectsLine( bar1 ) ) {
            ball.setDisplacementX( -Ball.nextDisplacement() );
        }
        else if ( bound.intersectsLine( dir) ) {
            ball.setDisplacementX( Ball.nextDisplacement() );  
        }
        else if ( bound2.intersectsLine( dir) ) {
            ball.setDisplacementX( -Ball.nextDisplacement() );  
        }
    }
 
    /** Paint Component to draw the actual boundaryon screen
     *@param  g which is a 2D graphics object
     */
    public void paint( Graphics2D g ) {
        Color c = g.getColor();
        g.setColor( color );
        g.draw( top );
        g.draw( bottom );
        g.draw( left );
        g.draw( right );
        g.draw( discright );
        g.draw( discleft );
        g.draw( botleft );
        g.draw( botright );
        g.draw(finisher);
        g.draw( bar1);
        g.setColor( Color2 );        
        g.draw( ramp);
        g.draw( ramp2);
        g.setColor( c );
        
       
    }
}
