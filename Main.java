import java.awt.Color;

import javax.swing.*;

/**
 *
 * Created with Eclipse
 *
 * Main Class for Dragon Fractal program. Contains main, the Java starter function, as well as serves as the wrapper for the program.
 * 
 * @author Alex
 *
 */
public class Main extends JFrame {

    /**
	 * Starting method for program. Creates a frame (an instance of Main), initializes it, and adds the Screen to itself.
	 * 
	 * @param args Command line arguments for program
	 */
	public static void main(String[] args) {

        FileHandler fileHandler = new FileHandler();
        fileHandler.loadOptions();

		// Defines a JFrame (Main) and a JPanel (Screen).
        Main frame = new Main();
        Screen screen = new Screen();
		
		// Initializes the JFrame.
		frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
		frame.setUndecorated(true);
		frame.setBackground(Color.black);
		frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		
		frame.requestFocusInWindow();
		
		// Adds the JPanel (Screen) to the JFrame (Main).
		frame.add(screen);
		
		// Displays the JFrame.
		frame.setVisible(true);
	}
	
	/**
	 * Closes the program (In effect simulating the user closing the program)
	 */
	public static void close() {
        System.exit(0);
	}
}
