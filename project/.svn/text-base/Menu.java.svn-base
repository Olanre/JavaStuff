/* THe maain menu class to show the Jmenu at the top of the frame
 * @author Olanrewaju Okunlola
 */
import java.awt.EventQueue;
import javax.swing.JFrame;
import java.io.BufferedWriter; 
import java.io.FileWriter;
import javax.swing.JMenuBar;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane; 
import javax.swing.JFileChooser;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.io.FileNotFoundException;
import javax.swing.JPanel;
import javax.swing.event.MenuListener;
import javax.swing.event.MenuEvent;
import javax.swing.JOptionPane;
import java.io.IOException;
import java.io.File;
import java.lang.Integer; 
import java.util.Scanner;
import java.awt.Color;
import java.lang.Long;

public class Menu { 

    private JMenuBar menuBar;
    public static PlayerForm playerform = new PlayerForm(); 
    public static PinBall pinball = new PinBall(playerform.numofGame());
    private static final double BALL_RADIUS = 15;
    private static final Color BALL_COLOR = Color.GRAY;
    /**
     * Create the application.
     */
    public Menu() {
        initialize();
    }

    /**
     * Initialize the contents of the JMenuBar.
     */
    private void initialize() {
        menuBar = new JMenuBar();
        JMenu mnFile = new JMenu("File");
        menuBar.add(mnFile);
        
        JMenuItem mntmQuit = new JMenuItem("Quit");
        mntmQuit.setMnemonic(KeyEvent.VK_Q);
        mntmQuit.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try{
                    savescore();
                }
                catch( IOException ex ) {
                    System.out.println("Cannot find file");
                    System.exit( 1 );
                }
                System.out.println("Q item");
                System.exit( 0 );
            }
        });        
        JMenuItem mntmSave = new JMenuItem("Save");
        mntmSave.setMnemonic(KeyEvent.VK_S);
        mntmSave.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                 /** get the high score in the file
                 */    
                 try{              
                     savescore(); }
                 catch( IOException ex){ 
                     System.out.println(ex);
                     System.exit(1);
                 }                       
            }
            
        });
        mnFile.add(mntmSave);
        mnFile.add(mntmQuit);
        JMenuItem mntmNewGame = new JMenuItem("New Game");
        mntmNewGame.setMnemonic(KeyEvent.VK_S);   
        mntmNewGame.addActionListener(new ActionListener() {
            /**listener to create a new game console 
            *@param e which is an ActionEvent
             */
            public void actionPerformed(ActionEvent e) {
                pinball = new PinBall(playerform.numofGame());
                pinball.runApplication( pinball );
                playerform = new PlayerForm();  
                pinball.activatekeylisteners();  
            }
        });     
        mnFile.add(mntmNewGame);
        JMenu mnOption = new JMenu("Option");
        menuBar.add(mnOption);
        JMenuItem mntmScores = new JMenuItem("Scores");
        mntmScores.setMnemonic(KeyEvent.VK_Q);
        mntmScores.addActionListener(new ActionListener() {
            /**listener to print scores all scores in the file 
            *@param e which is an ActionEvent
             */
            public void actionPerformed(ActionEvent e) {
                try{
                    StringBuffer s = new StringBuffer();
                    s.append( "All Found Scores\n\n");
                    Scanner scanner = new Scanner( new File("highscores.data"));
                    while ( scanner.hasNext() ) { 
                            s.append(scanner.next() + "\n");
                            
                    }   
                    String displaystring = s.toString();
                    JOptionPane.showMessageDialog(null, displaystring);
                            
                }
                catch( Exception ex ) {
                        System.out.println(ex); 
                       System.exit( 1 );
                }                  
            }
        });
        mnOption.add(mntmScores);
                
        
        JMenu mnHelp = new JMenu("Help?");
        menuBar.add(mnHelp);
        JMenuItem mntmInstructions = new JMenuItem("Instructions");
        mntmInstructions.setMnemonic(KeyEvent.VK_Q);
        mntmInstructions.addActionListener(new ActionListener() {
            /** listener to print out the instructions in the file
             *@param e which is an ActionEvent
             */
            public void actionPerformed(ActionEvent e) {
                  StringBuffer s = new StringBuffer();
                  s.append( "Game Instructions\n\n");
                  s.append( "z for left bumper" + "\n");
                  s.append("/ for right backspace to launch ball." + "\n");
                  s.append("Launch the ball after pressing the start button\n");
                  s.append("The real game always opens in a new console \n");
                  s.append("Enjoy!!"); 
                  String displaystring = s.toString();
                    JOptionPane.showMessageDialog(null, displaystring);
            }
        });
        mnHelp.add(mntmInstructions);
        
    }
    /** savescore sends all the scores reported in a file in a new line
     *and throws and catched relevant exceptions
     */ 
    public static void savescore()throws IOException{
        try{
            File f = new File("highscores.data");
            boolean exists = f.exists();
            if(exists){
                FileWriter fstream = new FileWriter(f, true); 
                BufferedWriter out = new BufferedWriter(fstream);
                fstream.write(String.valueOf(pinball.setScore()));
                fstream.write("\n"); 
                System.out.println(pinball.setScore());
                fstream.close(); 
            }
            else{
                f.createNewFile();
            }
        }
        catch( IOException ex ) {
            System.out.println("Cannot find file");
            System.exit( 1 );
        }
     }
    /* to return the JMenuBar from initialize
    */
    public JMenuBar getMenuBar() {
        return menuBar;
    }
}
