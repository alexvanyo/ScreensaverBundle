import java.awt.Point;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;

/**
 *
 * Created with Eclipse
 * 
 * This class handles all listeners (user input). As the program is intended to be a screensaver, when any user input is detected the program is exited.
 * 
 * @author Alex
 *
 */
public class Listeners implements MouseListener, MouseMotionListener, MouseWheelListener, KeyListener {
  
	// Private variable that stores the last place the mouse was. If it changes, the program is exited.
	private Point previousPoint = new Point(-1, -1);
	
	@Override
	public void mouseWheelMoved(MouseWheelEvent e) {
		if (!FileHandler.ONLY_EXIT_ON_KEY) {
			Main.close();
		}
	}

	@Override
	public void mouseDragged(MouseEvent e) {
		
	}

	@Override
	public void mouseMoved(MouseEvent e) {
		if (!FileHandler.ONLY_EXIT_ON_KEY) {
			if (previousPoint.x == -1) {
				// If previousPoint has a -1 value as its x value (The first time the function is called), previousPoint is to be where the mouse is.
				previousPoint = e.getPoint();
			} else {
				// Otherwise, the mouse has moved, and the program should quit.
				if (Math.hypot(previousPoint.x - e.getPoint().x, previousPoint.y - e.getPoint().y) > 1) {
					Main.close();
				}
			}
		}
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		if (!FileHandler.ONLY_EXIT_ON_KEY) {
			Main.close();
		}
	}

	@Override
	public void mousePressed(MouseEvent e) {}

	@Override
	public void mouseReleased(MouseEvent e) {}

	@Override
	public void mouseEntered(MouseEvent e) {}

	@Override
	public void mouseExited(MouseEvent e) {}

	@Override
	public void keyTyped(KeyEvent e) {
		if (!FileHandler.ONLY_EXIT_ON_KEY) {
			Main.close();
		}
	}

	@Override
	public void keyPressed(KeyEvent e) {
		if (e.getKeyCode() == FileHandler.EXIT_KEY || !FileHandler.ONLY_EXIT_ON_KEY) {
			Main.close();
		}
	}

	@Override
	public void keyReleased(KeyEvent e) {
		if (!FileHandler.ONLY_EXIT_ON_KEY) {
			Main.close();
		}
	}

}
