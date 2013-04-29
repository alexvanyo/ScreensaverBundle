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

    public static String[] arguments;

    public static Main frame;
    public static Screen screen;

    private boolean running;

    public Main() {
        this.running = true;
    }

    public boolean isRunning() {
        return running;
    }

    /**
     * Closes the program (In effect simulating the user closing the program)
     */
    public void close() {
        this.running = false;
        while (screen.repaintThread.isAlive() || screen.calcThread.isAlive()) {
            continue;
        }
        System.exit(0);
    }

    /**
     * Starting method for program. Creates a frame (an instance of Main), initializes it, and adds the Screen to itself.
     *
     * @param args Command line arguments for program
     */

	public static void main(String[] args) {

        arguments = args;

		// Defines a JFrame (Main), FileHandler, and a JPanel (Screen).
        frame = new Main();
        FileHandler fileHandler = new FileHandler();
        fileHandler.loadOptions();
        screen = new Screen();

        if (arguments.length > 0) {
            if (arguments[0].startsWith("/p")) {
                frame.close();
            } else if (arguments[0].startsWith("/c")) {
                // future settings configuration
            }
        }

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
}
