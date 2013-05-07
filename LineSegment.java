/**
 * Created with Eclipse
 * <p/>
 * This class serves as a wrapper for the line segments. It defines the position of the line (start and end of it), as well as the angle at which the line is pointing.
 *
 * @author Alex
 */
public class LineSegment {

    // Private variables defining the start of the line (startX and startY), the end of the line (endX and endY), and the angle (angle)
    private double startX;
    private double startY;
    private double endX;
    private double endY;
    private int angle;

    public LineSegment(double startX, double startY, double endX, double endY, int angle) {
        this.startX = startX;
        this.startY = startY;
        this.endX = endX;
        this.endY = endY;
        this.angle = angle;
    }

    public double getStartX() {
        return startX;
    }

    public double getStartY() {
        return startY;
    }

    public double getEndX() {
        return endX;
    }

    public double getEndY() {
        return endY;
    }

    public int getAngle() {
        return angle;
    }
}
