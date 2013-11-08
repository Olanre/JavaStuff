import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.FlowLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.Font;
import java.awt.Dimension;
import javax.swing.JRadioButton;
import javax.swing.ButtonGroup;
import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.lang.String; 
import javax.swing.JOptionPane; 
import java.lang.Long; 
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
import java.io.IOException;
import java.io.File;
import java.util.Scanner;
import java.util.ArrayList;

/* THe maain Form class to allow the user to interact with game features
 *such as pausing and viewing his/her high score
 * @author Olanrewaju Okunlola
 */
public class PlayerForm extends JPanel
    implements ActionListener
{
    long currentscr = 0; 
    int games; 
     ArrayList<Long> scores = new ArrayList<Long>();
    // instance and static variables
    private JButton start;
    private JButton pause;
    private JButton resume;
    private JTextArea currentscore;
    private JTextArea highscore; 
    private static final int INTERVAL = 1000;
    private static String[] numbers = {
       "1", "2", "3", "4", "5", "6"
    };
    // pinball is a class variable 
    private static PinBall pinball; 
    private JRadioButton[] numberButtons;
    //creates a timer 
    private javax.swing.Timer timer;
    
    /**Main constructor for the entire PlayerForm which creates 
     *the playerform  including its ActionListeners and Events */
    public PlayerForm() {
        setPreferredSize(
            new Dimension( 250, 650 ) );
        setBackground( Color.BLACK); 
        setLayout( new BorderLayout() );
        Border blackLine = BorderFactory.createLineBorder(Color.black);

        JPanel rows = new JPanel();
        rows.setLayout( new BoxLayout(rows, BoxLayout.Y_AXIS) );
        JPanel p;
        timer = new javax.swing.Timer(INTERVAL, new Changescore());
        // row 1
        p = new JPanel(new FlowLayout(FlowLayout.RIGHT,4,4) );
        p.setBackground( new Color(255, 255, 100 ) );
        p.add( new JLabel("Number of Games: ") );
        numberButtons = new JRadioButton[ numbers.length ];
        ButtonGroup group = new ButtonGroup();
        for( int i = 0 ; i < numbers.length; i++ ) {
           numberButtons[i] = new JRadioButton( numbers[i] );
           numberButtons[i].addActionListener( this );
           numberButtons[i].setForeground( Color.black);
           group.add( numberButtons[i] );
           p.add( numberButtons[i] );
       }
       add( p, BorderLayout.NORTH );
       
        rows.add( p );
 
        p = new JPanel(new FlowLayout(FlowLayout.LEFT,4,4) );
        p.setBackground( new Color( 100, 200, 150) );
        p.add( new JLabel("CurrentScore: ") );
        currentscore = new JTextArea();
        p.add( currentscore );
        rows.add( p );

        // row 2
        p = new JPanel(new FlowLayout(FlowLayout.LEFT,4,4) );
        p.setBackground( new Color( 100, 205, 55) );
        p.add( new JLabel("HighScore: ") );
        highscore = new JTextArea();
        p.add( highscore);
        rows.add( p );
        
        
        p = new JPanel( new GridLayout(1,1) );
        start = new JButton("START");
        start.setFont(new Font("monospaced", Font.BOLD, 10));
        p.add( start );
        start.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                    pinball = new PinBall(numofGame()); 
                    pinball.runApplication( pinball );
                    long topscore = 0; 
                    timer.start(); 
                    pinball.activatekeylisteners();
               
               try{
                    Scanner input = new Scanner(new File("highscores.data"));
                     while( input.hasNext() ) { 
                           String s = input.next();
                            
                           long v = Long.parseLong(s);
                           scores.add( new Long(v));
                           
                     }
                     input.close();
                     long[] longs = new long[scores.size()];
                     for (int i = 0; i < longs.length-1; i++) {
                         longs[i] = scores.get(i);
                     }
                     
                     topscore = getMax(longs);
                          
                   }
                   catch( Exception ex ) {
                        System.out.println(ex); 
                       System.exit( 1 );
                   }     
                highscore.setText( String.valueOf(topscore) );
                getScore();  
                
                
            }
        });
        rows.add( p );
       
        p = new JPanel( new GridLayout(1,1) );
        pause = new JButton("PAUSE");
        pause.setFont(new Font("monospaced", Font.BOLD, 10));
        p.add( pause );
        pause.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                        pinball.stop();
                         
                        JOptionPane.showMessageDialog(null,  
                  "Game has been paused. Hit resume to continue or save game and quit");
                    
           }
        });
        rows.add( p );
        
        p = new JPanel( new GridLayout(1,1) );
        resume = new JButton("RESUME");
        resume.setFont(new Font("monospaced", Font.BOLD, 10));
        p.add( resume );
        resume.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                        pinball.start();             
           }
        });
        rows.add( p );
        add( rows, BorderLayout.CENTER);
     }
        
     /** event listener method
     *@param e which the ActionEvent
     */
     public void actionPerformed( ActionEvent e ) {
        Object o = e.getSource();
        JRadioButton b = (JRadioButton)o;
        games = Integer.parseInt( b.getText()); 
        System.out.println(games);
        
    }
    /** Sorts through an array to arrive at the maximum value of that array
     *then returns said max value
     *param []lst which is an array of longs
     */
    public long getMax(long []lst)
{
    long max = lst[0];
    System.out.println(max);
    for(int i=0;i<lst.length;i++)
    {
        if(max<lst[i])
        max=lst[i];
         
    }
    return max;
}

    /** class Changescore deals with all relevent action events to get the score of the 
      *the game at the relevant time
      */
    class Changescore implements ActionListener {
    
       public void actionPerformed(ActionEvent e) {
           getScore(); 
       }
    }
    /** get the number of games requested by the user and returns it
     * it is an integer
     */
    public int numofGame(){
         return games;
    }
    /** to return the currentscore JTextArea for further use in calculating the adding to the score
     *at each hit event
     */
    public JTextArea getcurrent(){
        return currentscore;
    }
    /** simple getter method to return the score of the pinball and add it to the JTextArea currentscore
    */ 
    public void getScore(){      
       currentscr = pinball.setScore();  
       getcurrent().setText( String.valueOf(currentscr)); 
       
    }  
    /** Run application deals with creating and intializing the 
     *frame the player form is in
     */   
    public static void runApplication( JPanel app ) {
       /* create frame */
       JFrame frame = new JFrame();
 
       frame.setSize( app.getPreferredSize() ); 
       frame.setTitle( app.getClass().getName() );
       frame.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
       frame.add( app );
       frame.setVisible( true ); 
    }
    
    public static void main( String[] args ) {
    
      PlayerForm form = new PlayerForm();
      runApplication( form );
    }
}
