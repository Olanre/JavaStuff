import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.Ellipse2D;
import java.awt.Stroke;
import java.awt.BasicStroke;
import java.awt.Dimension; 
import java.awt.geom.Rectangle2D;
import java.awt.geom.Line2D;
import java.awt.geom.RoundRectangle2D;
import java.awt.EventQueue;
import java.awt.geom.Path2D; 
import javax.swing.JPanel;
import javax.swing.JFrame;

/** Class to create several sprites on the screen
 * which the ball will bounce off so the user recieves points
 *@author Olanrewaju Okunlola
 */
public class Sprites implements Animate, Drawable{
    
    public double y = 0.0;
    public double x = 0.0; 
    long score = 0;  
    public PlayerForm playerform = new PlayerForm(); 
    Rectangle2D.Double recttop = new Rectangle2D.Double(); 
    Rectangle2D.Double rectright = new Rectangle2D.Double(); 
    Rectangle2D.Double rectleft = new Rectangle2D.Double(); 
    Ellipse2D.Double circle = new Ellipse2D.Double();
    Ellipse2D.Double circle2 = new Ellipse2D.Double();
    Path2D bumper1 = new Path2D.Double();
    Path2D bumper2 = new Path2D.Double();
    Line2D.Double sideshock1 = new Line2D.Double();
    Line2D.Double sideshock2 = new Line2D.Double();  
    Line2D.Double extend1 = new Line2D.Double();
    Line2D.Double extend2 = new Line2D.Double();    
    Ellipse2D.Double bigcircle = new Ellipse2D.Double();
    private Color Color1 = Color.red;
    private Color Color2 = Color.blue;
    private Color Color3 = Color.pink;
    private Color Color4 = Color.yellow;
    private Color Color5 = Color.green;
    private Color Color6 = Color.cyan;
    

    /** Method to chage object movement i.e. 
     *where the object shall go
     *@param x and y which are the coordinate variables of the current position
     */
    public void move( double x, double y){
        this.x = x;
        this.y = y; 
    }
    
    /** Method to chage object congiguration i.e. 
     *how the object looks by alternating the colors
     */
    public void changeConfiguration(){
        
        if(Color1 == Color.pink){
            Color1 = Color.blue;
        }
        else {
            Color1 = Color.pink;
        }
        
        if(Color2 == Color.blue){
            Color2 = Color.pink;
        }
        else {
            Color2 = Color.blue;
        }
        
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
        Rectangle2D bound = bumper1.getBounds2D();
        Rectangle2D bound2 = bumper2.getBounds2D(); 
        if ( dir.intersectsLine( extend2 ) ) {
            ball.setDisplacementY( -Ball.nextDisplacement() );
            playerform.getScore();
        }
        else if ( dir.intersectsLine( extend1 ) ) {
            ball.setDisplacementY( -Ball.nextDisplacement() ); 
            playerform.getScore();
        }
        else if ( dir.intersectsLine( sideshock1 ) ) {
            ball.setDisplacementX( Ball.nextDisplacement() ); 
            playerform.getScore();
        }
        else if ( dir.intersectsLine( sideshock2 ) ) {
            ball.setDisplacementX( -Ball.nextDisplacement() );
            playerform.getScore();
        }
        else if ( bound.intersectsLine( dir) ) {
            ball.setDisplacementX( -Ball.nextDisplacement() );
            score += 200; 
            playerform.getScore(); 
        }
        else if ( bound2.intersectsLine( dir) ) {
            ball.setDisplacementX( Ball.nextDisplacement() );
            score += 200;  
            playerform.getScore();
        }
        else if ( recttop.intersectsLine( dir) ) {
            ball.setDisplacementY( -Ball.nextDisplacement() );
            score += 100; 
            playerform.getScore(); 
        }
        else if ( rectleft.intersectsLine( dir) ) {
            ball.setDisplacementX( -Ball.nextDisplacement() );
            score += 100;  
            playerform.getScore();
        }
        else if ( rectright.intersectsLine( dir) ) {
            ball.setDisplacementX( -Ball.nextDisplacement() );
            score += 100;  
            playerform.getScore();
        }
        else if ( circle.contains(bx, by ) ) {
            ball.setDisplacementX( -Ball.nextDisplacement() );
            ball.setDisplacementY( Ball.nextDisplacement() );
            score += 100; 
            playerform.getScore(); 
        }
        else if ( circle2.contains(bx, by ) ) {
            ball.setDisplacementX( Ball.nextDisplacement() );
            ball.setDisplacementY( -Ball.nextDisplacement() );
            score += 100; 
            playerform.getScore(); 
        }
         
        
    }
    /**Method the return the points which a long*/
    public long points(){
        return score;
   }
    
    /* Draws shapes and fills them with color
     *@ param g2d which is a Graphics Object
     * draws based on x and y instance variable coordinates
     */
    public void draw(Graphics2D g2d){
        g2d.setColor( Color3 );
        
	     
        recttop.x = x + 200.0;
        recttop.y = y + 50.0;
        recttop.height = 10.0;
        recttop.width = 120.0; 
        g2d.setColor( Color1 );        
        g2d.draw ( recttop);
        
        rectleft.x = x + 150.0;
        rectleft.y = y + 100.0;
        rectleft.height = 100.0;
        rectleft.width = 10.0; 
        g2d.setColor( Color1 );        
        g2d.draw( rectleft);
        
        rectright.x = x + 350.0;
        rectright.y = y + 100.0;
        rectright.height = 100.0;
        rectright.width = 10.0; 
        g2d.setColor( Color1 );        
        g2d.draw ( rectright);
        
       
       circle.x = x + 80.0;
       circle.y = y + 240.0;
       circle.width = 80.0;
       circle.height = 80.0;
       g2d.setColor( Color2);
       g2d.draw ( circle);
        double cx2 = circle2.width * 0.5;
       double cy2 = circle2.height * 0.5;
       for( int i = 0 ; i < 8; i++ ) {
                double theta = (Math.PI*2.0) * i/6;
                double kx = cx2 + (circle2.width*Math.cos(theta));
                double ky = cy2 + (-circle2.width*Math.sin(theta));
                Ellipse2D e2 = new Ellipse2D.Double( kx + 400, ky, 20.0, 20.0); 
                g2d.setColor( Color6);
                g2d.draw( e2 );
       }
       circle2.x = x + 350.0;
       circle2.y = y + 240.0;
       circle2.width = 80.0;
       circle2.height = 80.0;
       g2d.setColor( Color2);
       g2d.draw ( circle2);
       double cx = circle.width * 0.5;
       double cy = circle.height * 0.5;
       for( int i = 0 ; i < 8; i++ ) {
                double theta = (Math.PI*2.0) * i/8;
                double kx = cx + (circle.width*Math.cos(theta));
                double ky = cy + (-circle.width*Math.sin(theta));
                Ellipse2D e = new Ellipse2D.Double( kx, ky, 10.0, 10.0); 
                g2d.setColor( Color5);
                g2d.draw( e );
       } 
       
       bumper1.reset();
       bumper1.moveTo(x+90, y+440 );
       bumper1.lineTo(x+90.0, y+500 );
       bumper1.lineTo(x+140, y+510 );
       bumper1.closePath();
       g2d.setColor( Color2 );        
       g2d.draw ( bumper1);
       
       bumper2.reset();
       bumper2.moveTo(x+460, y+440 );
       bumper2.lineTo(x+460.0, y+500 );
       bumper2.lineTo(x+410, y+520 );
       bumper2.closePath();
       g2d.setColor( Color2 );        
       g2d.draw( bumper2);
       
       g2d.setColor( Color4 );
       sideshock1 = new Line2D.Double(x+60, y+440, x+60, y + 510 );
       g2d.draw ( sideshock1);  
       sideshock2 = new Line2D.Double(x+510, y+510, x+510, y + 440);
       g2d.draw ( sideshock2);  
       extend1 = new Line2D.Double(x+60, y+510, x+180, y + 550 );
       g2d.draw ( extend1);  
       extend2 = new Line2D.Double(x+510, y+510, x+390, y + 550);
       g2d.draw ( extend2);
                            
        
    }
}