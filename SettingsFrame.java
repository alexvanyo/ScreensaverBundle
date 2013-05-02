import javax.swing.*;

/**
 * Created with IntelliJ IDEA.
 * User: Alex
 * Date: 4/29/13
 * Time: 6:05 PM
 */
public class SettingsFrame extends JFrame {

    public SettingsFrame(SettingsScreen screen) {
        this.setIconImage(new ImageIcon(this.getClass().getResource("resources/icon.png")).getImage());
        this.setTitle("DragonFractal Screensaver");
        this.requestFocusInWindow();
        this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        this.setResizable(false);

        this.requestFocusInWindow();

        this.add(screen);
        this.pack();

        this.setLocationRelativeTo(this.getRootPane());
    }
}
