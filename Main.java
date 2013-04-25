import java.awt.Color;
import java.awt.event.KeyEvent;
import java.awt.event.WindowEvent;

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
  
	// Private variables for the JFrame (frame) and the JPanel (screen).
	private static Main frame;
	private static Screen screen;
    private static FileHandler fileHandler;
	
	/**
	 * Starting method for program. Creates a frame (an instance of Main), initializes it, and adds the Screen to itself.
	 * 
	 * @param args Command line arguments for program
	 */
	public static void main(String[] args) {

        fileHandler = new FileHandler();
        fileHandler.loadOptions();

		// Defines a JFrame (Main) and a JPanel (Screen).
		frame = new Main();
		screen = new Screen();
		
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
		// Creates a 'Window Event', and sends it to the system. This simulates closing the program by hand.
		WindowEvent wEvent = new WindowEvent(frame, WindowEvent.WINDOW_CLOSING);
		java.awt.Toolkit.getDefaultToolkit().getSystemEventQueue().postEvent(wEvent);
	}
}
