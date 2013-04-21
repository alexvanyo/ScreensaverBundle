/**
 * 
 * This class serves as a wrapper for the line segments. It defines the position of the line (start and end of it), as well as the angle at which the line is pointing.
 * 
 * @author Alex
 *
 */
public class LineSegment {
  
	// Private variables defining the start of the line (startX and startY), the end of the line (endX and endY), and the angle (angle)
	private double startX;
	private double startY;
	private double endX;
	private double endY;
	private int angle;
	
	/**
	 * Constructor for a line segment. It stores the passed arguments into their respective class variables.
	 * 
	 * @param startX
	 * @param startY
	 * @param endX
	 * @param endY
	 * @param angle
	 */
	public LineSegment(double startX, double startY, double endX, double endY, int angle) {
		this.startX = startX;
		this.startY = startY;
		this.endX = endX;
		this.endY = endY;
		this.angle = angle;
	}
	
	// The following are getters for the five private variables
	/**
	 * Returns startX.
	 * 
	 * @return startX
	 */
	public double getStartX() {
		return startX;
	}
	
	/**
	 * Returns startY.
	 * 
	 * @return startY
	 */
	public double getStartY() {
		return startY;
	}
	
	/**
	 * Returns endX.
	 * 
	 * @return startY
	 */
	public double getEndX() {
		return endX;
	}
	
	/**
	 * Returns endY.
	 * 
	 * @return startY
	 */
	public double getEndY() {
		return endY;
	}
	
	/**
	 * Returns angle.
	 * 
	 * @return angle
	 */
	public int getAngle() {
		return angle;
	}
}
