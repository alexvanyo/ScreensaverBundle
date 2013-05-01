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

    public static ScreensaverFrame frame;
    public static ScreensaverScreen screen;

    private static boolean running;

    public static boolean isRunning() {
        return running;
    }

    /**
     * Closes the program (In effect simulating the user closing the program)
     */
    public static void close() {
        running = false;
        System.exit(0);
    }

    /**
     * Starting method for program. Creates a frame (an instance of Main), initializes it, and adds the Screen to itself.
     *
     * @param args Command line arguments for program
     */

	public static void main(String[] args) {

        arguments = args;

        running = true;

        boolean isSettings = false;

        if (arguments.length > 0) {
            if (arguments[0].startsWith("/p")) {
                close();
            } else if (arguments[0].startsWith("/c")) {
                isSettings = true;
            }
        }

        try {

            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());

        } catch (ClassNotFoundException e) {
            System.out.println(e);
        } catch (UnsupportedLookAndFeelException e) {
            System.out.println(e);
        } catch (InstantiationException e) {
            System.out.println(e);
        } catch (IllegalAccessException e) {
            System.out.println(e);
        }

        FileHandler.loadOptions();

        if (isSettings) {
            SettingsScreen screen = new SettingsScreen();
            SettingsFrame frame = new SettingsFrame(screen);

            frame.setVisible(true);
        } else {
            ScreensaverScreen screen = new ScreensaverScreen();
            ScreensaverFrame frame = new ScreensaverFrame(screen);

            frame.setVisible(true);
        }
	}
}
