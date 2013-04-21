import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;

import javax.swing.JPanel;

/**
 * 
 * This class is the main class in the program. It handles the loop that calculates the segments, and draws them to the screen.
 * 
 * @author Alex
 *
 */
public class Screen extends JPanel {
  
	// Private variables that manage the two threads: a repaint thread (repaintThread) and a thread the calculates the segments (calcThread)
	private Thread calcThread;
	private Thread repaintThread;
	
	// Private variables that manage the calculation of the segments.
	private volatile LineSegment[] lineSegments;
	private volatile LineSegment[] oldLineSegments;
	private int iterations = 0;
	private double lineSegmentLength;
	
	// Private variables that manage the screen size and layout.
	//There are no hard-coded values, so the screensaver will work correctly, as long as the width in pixels is at least 1.5 times the sizes of the height
	private static final int screenX = (int) Toolkit.getDefaultToolkit().getScreenSize().getWidth();
	private static final int screenY = (int) Toolkit.getDefaultToolkit().getScreenSize().getHeight();
	private static final int lineSegmentEquivalent = screenY;
	private static final int startDrawX = (int) ((screenX - (lineSegmentEquivalent * 1.5)) / 2);
	private static final int startDrawY = 0;
	private static final int startX = (int) (startDrawX + (lineSegmentEquivalent / 3));
	private static final int startY = (int) (startDrawY + ((lineSegmentEquivalent * 2) / 3));
	private static final int endX = startX + lineSegmentEquivalent;
	private static final int endY = startY;
	
	/**
	 * Constructor for Screen. This initializes itself, and starts the threads running that run the screensaver.
	 */
	public Screen() {
		
		// Initializes the JPanel
		this.setPreferredSize(new Dimension(screenX, screenY));
		this.setFocusable(true);
		this.requestFocusInWindow();
		
		BufferedImage cursorImg = new BufferedImage(16, 16, BufferedImage.TYPE_INT_ARGB);
		Cursor blankCursor = Toolkit.getDefaultToolkit().createCustomCursor(cursorImg, new Point(0, 0), "blank cursor");
		
		this.setCursor(blankCursor);
		
		// Defines the listeners
		Listeners listeners = new Listeners();
		
		// Creates the thread that calculates the segments.
		calcThread = new Thread(new Runnable() {
			
			@Override
			public void run() {
				// Forever, (while the program is running) calculate new line segment.
				while (true) {
					
					// If the number of iterations is 0 (first time through the loop), or the number of iterations exceeds the max allowed, 
					// then line segments and old line segments are reset.
					if (iterations == 0 || iterations > Main.MAX_NUMBER_OF_ITERATIONS) {
						iterations = 0;
						lineSegmentLength = lineSegmentEquivalent;
						lineSegments = new LineSegment[1];
						lineSegments[0] = new LineSegment(startX, startY, endX, endY, 0);
						
						oldLineSegments = new LineSegment[1];
						oldLineSegments[0] = new LineSegment(startX, startY, endX, endY, 0);
					}
					
					// Between each iteration, the thread waits
					try {
						Thread.sleep(Main.WAIT_BETWEEN_ITERATIONS);
					} catch (Exception e) {}
					
					// This calculates the new length of each line segment, and sets oldLineSegments to the previous iteration's line segments,
					// and resets lineSegments, the ones that will be calculated.
					lineSegmentLength = Math.sqrt(Math.pow(lineSegmentLength, 2) / 2);
					oldLineSegments = lineSegments.clone();
					lineSegments = new LineSegment[oldLineSegments.length * 2];
					
					// This loop loops through all of the old line segments, and creates the two new ones based on the rules of the Dragon Fractal
					// NOTE: the ternary operator (boolean ? a : b) is shorthand for 
					// if (boolean) {
					//     a;
					// } else {
					//     b;
					// }
					// This makes it useful for situations that are going to decide between two values based on a boolean expression.
					for (int i = 0; i < oldLineSegments.length; i++) {
						
						// If the number of the old line segment is even, than the turn is a left, otherwise, it is a right
						// This means that the original angle of the segment should be increased, or decreased, if the turn is right, or a left, respectively.
						int angle1 = oldLineSegments[i].getAngle() + ((i % 2 == 0) ? -45 : 45);
						// If the angle is greater than or equal to 360 degrees, or less than 0 degrees, the angle value is decreased by -360 degrees, 
						//or increased by 360, respectively. This keeps the angle between 0 (inclusive) and 360 (exclusive).
						angle1 += (angle1 >= 360) ? -360 : (angle1 < 0) ? 360 : 0;
						// In Java, the top left corner of the screen is 0, 0, and both numbers increase the further to the right and down you go. Because of this,
						// angles are defined as follows:
						//        270
						//   225   |   315
						//      \  |  /
						// 180 - - X - - 0
						//      /  |  \
						//   135   |   45
						//        90
						// This allows cosine and sine to be used effectively with addition.
						
						// This sets the start point of the new segment, which is the same as the start point of the old line segment
						double startX1 = oldLineSegments[i].getStartX();
						double startY1 = oldLineSegments[i].getStartY();
						// This sets the end point of the new line segment. The new line segment's end point is the length of a line segment away from the start.
						// However due to the varying angle, cosine needs to be used to calculate the difference in the X coordinate,
						// and sine needs to be used to calculate the difference in the the Y coordinate
						double endX1 = startX1 + (Math.cos(Math.toRadians(angle1)) * lineSegmentLength);
						double endY1 = startY1 + (Math.sin(Math.toRadians(angle1)) * lineSegmentLength);
						
						// A new line segment at the array index of i * 2 (because there are twice as many more line segments in each iteration as the previous)
						// is formed from these calculated values.
						lineSegments[i * 2] = new LineSegment(startX1, startY1, endX1, endY1, angle1);
						
						// If debug option NO_WAIT_BETWEEN_SEGMENTS is not enabled:
						// This makes the thread wait in between calculating (and therefore displaying) each segment.
						if (!Main.NO_WAIT_BETWEEN_SEGMENTS) {
							try {
								Thread.sleep(Main.WAIT_BETWEEN_SEGMENTS);
							} catch (Exception e) {}
						}
						
						// If the number of the old line segment is even, than the turn is a left, otherwise, it is a right
						// This means that the angle of the first new segment of the segment should be decreased by 90 degrees, or increased by 90 degrees, 
						//if the turn is right, or a left, respectively.
						int angle2 = angle1 + ((i % 2 == 0) ? 90 : -90);
						// If the angle is greater than or equal to 360 degrees, or less than 0 degrees, the angle value is decreased by -360 degrees, 
						//or increased by 360, respectively. This keeps the angle between 0 (inclusive) and 360 (exclusive).
						angle2 += (angle2 >= 360) ? -360 : (angle2 < 0) ? 360 : 0;
						
						// This sets the start point of the new segment, which is the same as the end point of the previously calculated line segment.
						// We could recalculate it, but if we already have, why do it again?
						double startX2 = endX1;
						double startY2 = endY1;
						// This sets the end point of the new line segment. This is going to be the end point of the original line segment.
						double endX2 = oldLineSegments[i].getEndX();
						double endY2 = oldLineSegments[i].getEndY();
						
						// A new line segment at the array index of (i * 2) + 1 (because there are twice as many more line segments in each iteration as the 
						// previous) is formed from these calculated values.
						lineSegments[(i * 2) + 1] = new LineSegment(startX2, startY2, endX2, endY2, angle2);
						
						// If debug option NO_WAIT_BETWEEN_SEGMENTS is not enabled:
						// This makes the thread wait in between calculating (and therefore displaying) each segment.
						if (!Main.NO_WAIT_BETWEEN_SEGMENTS) {
							try {
								Thread.sleep(Main.WAIT_BETWEEN_SEGMENTS);
							} catch (Exception e) {}
						}
					}
					
					// Increases the iteration count
					iterations++;
				}
			}
		});
		
		// Creates the thread that repaints the JPanel, reflecting changes in the segments.
		repaintThread = new Thread(new Runnable() {
			
			@Override
			public void run() {
				// Forever, (while the program is running) repaint the screen.
				while (true) {
					repaint();
				}
			}
		});
		
		// Starts both threads.
		repaintThread.start();
		calcThread.start();
		
		// Adds the listeners to itself.
		this.addMouseListener(listeners);
		this.addMouseMotionListener(listeners);
		this.addMouseWheelListener(listeners);
		this.addKeyListener(listeners);
	}
	
	/*
	 * This function paints the screen, and is called whenever repaint(); is called.
	 */
	@Override
	public void paintComponent(Graphics gOrig) {
		// Creates a Graphics2D object from the passed Graphics object. This allows more complex drawing functions to be used.
		Graphics2D g = (Graphics2D) gOrig;
		
		// Resets the screen by drawing a black rectangle that covers the entire screen.
		g.setColor(Color.black);
		g.fillRect(0, 0, screenX, screenY);

		// If debug option HIDE_PREVIOUS_ITERATION is not enabled:
		// Draws the faded line that depicts the previous iteration of lines.
		if (!Main.HIDE_PREVIOUS_ITERATION) {
			// Sets the width of the lines to be 1 pixel, and sets the color to be yellow (RGB 255,255,0), with a high transparency (40/255)
			g.setStroke(new BasicStroke(2));
			
			// For all of the old line segments, draw a line from their start position to their end position.
			for (int i = 0; i < oldLineSegments.length; i++) {
				Color tempColor = Color.getHSBColor((((float) 360 / oldLineSegments.length) * i) / 360, 1, 1);
				g.setColor(new Color(tempColor.getRed(), tempColor.getGreen(), tempColor.getBlue(), 50));
				
				if (oldLineSegments[i] != null) {
					g.drawLine((int) oldLineSegments[i].getStartX(), (int) oldLineSegments[i].getStartY(), (int) oldLineSegments[i].getEndX(), (int) oldLineSegments[i].getEndY());
				} else {
					break;
				}
			}
		}
		
		// Sets the width of the lines to be 3 pixels, and sets the color to be yellow (RGB 255,255,0), with a transparency of 0 (255/255)
		g.setStroke(new BasicStroke(3));
		
		// For all line segments that are currently calculated, draw a line from their start position to their end position.
		for (int i = 0; i < lineSegments.length; i++) {
			
			g.setColor(Color.getHSBColor((((float) 360 / lineSegments.length) * i) / 360, 1, 1));
			if (lineSegments[i] != null) {
				g.drawLine((int) lineSegments[i].getStartX(), (int) lineSegments[i].getStartY(), (int) lineSegments[i].getEndX(), (int) lineSegments[i].getEndY());
			} else {
				break;
			}
		}
	}	
}
