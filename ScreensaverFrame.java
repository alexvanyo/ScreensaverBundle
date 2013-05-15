import javax.swing.*;
import java.awt.*;

/**
 * Created with IntelliJ IDEA.
 * User: Alex
 * Date: 4/29/13
 * Time: 5:54 PM
 */
public class ScreensaverFrame extends JFrame {

    public ScreensaverFrame(ScreensaverScreen screen) {

        // Initializes the JFrame.
        this.setExtendedState(JFrame.MAXIMIZED_BOTH);
        this.setUndecorated(true);
        this.setBackground(Color.black);
        this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        this.setIconImage(new ImageIcon(this.getClass().getResource("resources/icon.png")).getImage());
        this.setTitle("ScreensaverBundle");

        this.requestFocusInWindow();

        // Adds the JPanel (Screen) to the JFrame (Main).
        this.add(screen);
        this.pack();

    }
}
