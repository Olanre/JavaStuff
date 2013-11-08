import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.Ellipse2D;
import java.awt.Stroke;
import java.awt.BasicStroke;
import java.awt.Dimension; 
import java.awt.geom.Rectangle2D;
import java.awt.geom.RoundRectangle2D;
import java.awt.EventQueue;
import java.awt.geom.Line2D;
import java.awt.geom.Path2D; 
import javax.swing.JPanel;
import javax.swing.JFrame;

/** Class to create an object which are the flippers in the game
 *@author Olanrewaju Okunlola
 */
public class Paddles{
    
    public double y = 0.0;
    public double y2 = 0.0;
    public double y3 = 0.0;
    public double x = 0.0; 
    public PlayerForm playerform = new PlayerForm();
    Line2D.Double paddle1 = new Line2D.Double();
    Line2D.Double paddle2 = new Line2D.Double();
    private Color paddlecolor = Color.white;

    /** Method to chage object movement i.e. 
     *where the object shall go
     *@param y which are the coordinate variables of the current position
     */
    public void move1( double y){
        this.y2 = this.y2 + y;
    }
    /** Method to chage object movement i.e. 
     *where the object shall go
     *@param y which are the coordinate variables of the current position
     */
    public void move2( double y){
        this.y3 = this.y3 + y;
    }
    
    /* Draws shapes and fills them with color
     *@ param g2d which is a Graphics Object
     * draws based on x and y instance variable coordinates
     */
    public void draw(Graphics2D g2d){
        g2d.setColor( paddlecolor );
        paddle1 = new Line2D.Double(x+180, y+550, x+250, y2 + 570 );
        g2d.draw ( paddle1);  
        paddle2 = new Line2D.Double(x+390, y+550, x+320, y3 + 570);
        g2d.draw ( paddle2); 
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
        if( paddle1.y2<560 || paddle2.y2 < 560){
            if ( paddle1.intersectsLine( dir) ) {
                ball.setDisplacementY( -Ball.nextDisplacement() ); 
                playerform.getScore(); 
            }
            else if ( paddle2.intersectsLine( dir) ) {
                ball.setDisplacementY( -Ball.nextDisplacement() ); 
                playerform.getScore(); 
            }
        }
    }
}