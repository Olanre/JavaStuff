import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.FlowLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.Font;
import javax.swing.JMenuBar;
import javax.swing.JOptionPane;
import javax.swing.event.MenuListener;
import javax.swing.event.MenuEvent;
import java.awt.Dimension;
import javax.swing.JRadioButton;
import javax.swing.ButtonGroup;
import java.awt.BorderLayout;
import java.awt.GridLayout;
import javax.swing.JFrame;
import javax.swing.JButton;
import javax.swing.JTextField;
import javax.swing.JTextArea;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JComponent;
import javax.swing.BoxLayout;
import javax.swing.BorderFactory;
import javax.swing.border.Border;
/* Combines all 3 main classes to run the game frame
 * @author Olanrewaju Okunlola
 */
public class RunApp extends JFrame
    //implements ActionListener
{

public static void runApplication( JPanel app, JPanel app2, JMenuBar app3 ) {
       /* create frame */
       JFrame frame = new JFrame();
       frame.setLayout(new FlowLayout()); 
       frame.setSize( 870, 700 ); 
       frame.setTitle( app.getClass().getName() );
       frame.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
       frame.setJMenuBar(app3);
       frame.add( app );
       frame.add( app2); 
       frame.setVisible( true ); 
       JOptionPane.showMessageDialog(null, 
                  "Please, select amount of games you want!");
    }

    /*
     * Create the frame to show the application
     */
    public static void main( String[] args ) {
        PinBall application = new PinBall(1);
        PlayerForm form = new PlayerForm();
        Menu menu = new Menu(); 
  
        runApplication( application, form, menu.getMenuBar() ); 
    }
}
