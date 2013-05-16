import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.ArrayList;

/**
 * Created with Eclipse
 *
 * This class is the main class in the program. It handles the loop that calculates the segments, and draws them to the screen.
 *
 * @author Alex
 */
public class ScreensaverScreen extends JPanel {

    // Private variables that manage the calculation of the segments.
    private volatile LineSegment[] lineSegments;
    private volatile LineSegment[] oldLineSegments;
    private int iterations = 0;
    private double lineSegmentLength;

    private volatile ArrayList<String> file;
    private volatile ArrayList<String> fileColors;
    private int stringIndex;
    private int lineIndex;

    // Private variables that manage the screen size and layout.
    // There are no hard-coded values, so the screensaver will work correctly, as long as the width in pixels is at least 1.5 times the sizes of the height
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
    public ScreensaverScreen() {

        // Initializes the JPanel
        this.setPreferredSize(new Dimension(screenX, screenY));
        this.setFocusable(true);
        this.requestFocusInWindow();

        // hides cursor
        BufferedImage cursorImg = new BufferedImage(16, 16, BufferedImage.TYPE_INT_ARGB);
        Cursor blankCursor = Toolkit.getDefaultToolkit().createCustomCursor(cursorImg, new Point(0, 0), "blank cursor");

        this.setCursor(blankCursor);

        // Defines the listeners
        Listeners listeners = new Listeners();

        // Creates the thread that calculates the segments.
        Thread calcThread = new Thread(new Runnable() {

            @Override
            public void run() {

                // Forever, (while the program is running) calculate new line segment.
                while (Main.isRunning()) {
                    if (FileHandler.Options.SCREENSAVER_TYPE.getInt() == FileHandler.ScreensaverTypes.DRAGON_CURVE.getID()) {
                        calcDragonCurve();
                    } else if (FileHandler.Options.SCREENSAVER_TYPE.getInt() == FileHandler.ScreensaverTypes.TYPING.getID()) {
                        calcTyping();
                    }
                }
            }
        });

        // Creates the thread that repaints the JPanel, reflecting changes in the segments.
        Thread repaintThread = new Thread(new Runnable() {

            @Override
            public void run() {
                // Forever, (while the program is running) repaint the screen.
                while (Main.isRunning()) {
                    repaint();
                    requestFocusInWindow();
                }
            }
        });

        if (FileHandler.Options.SCREENSAVER_END.getFloat() > 0) {

            ActionListener autoEndProgram = new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    Main.close();
                }
            };

            new Timer((int) FileHandler.Options.SCREENSAVER_END.getFloat() * 1000, autoEndProgram).start();

        }

        // Starts both threads.
        repaintThread.start();
        calcThread.start();

        // Adds the listeners to itself.
        this.addMouseListener(listeners);
        this.addMouseMotionListener(listeners);
        this.addMouseWheelListener(listeners);
        this.addKeyListener(listeners);
    }

    private void calcDragonCurve() {

        // If the number of iterations is 0 (first time through the loop), or the number of iterations exceeds the max allowed,
        // then line segments and old line segments are reset.
        if (iterations == 0 || iterations > FileHandler.Options.MAX_NUMBER_OF_ITERATIONS.getInt()) {
            iterations = 0;
            lineSegmentLength = lineSegmentEquivalent;
            lineSegments = new LineSegment[1];
            lineSegments[0] = new LineSegment(startX, startY, endX, endY, 0);

            oldLineSegments = new LineSegment[1];
            oldLineSegments[0] = new LineSegment(startX, startY, endX, endY, 0);
        }

        // Between each iteration, the thread waits
        try {
            Thread.sleep(FileHandler.Options.WAIT_BETWEEN_ITERATIONS.getLong());
        } catch (Exception e) {
            System.err.println(e);
        }

        // This calculates the new length of each line segment, and sets oldLineSegments to the previous iteration's line segments,
        // and resets lineSegments, the ones that will be calculated.
        lineSegmentLength = Math.sqrt(Math.pow(lineSegmentLength, 2) / 2);
        oldLineSegments = lineSegments.clone();
        lineSegments = new LineSegment[oldLineSegments.length * 2];

        // Increases the iteration count
        iterations++;

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
            if (!FileHandler.Options.NO_WAIT_BETWEEN_SEGMENTS.getBoolean()) {
                try {
                    Thread.sleep(FileHandler.Options.WAIT_BETWEEN_SEGMENTS.getLong());
                } catch (Exception e) {
                    System.err.print(e);
                }
            }

            // If the number of the old line segment is even, than the turn is a left, otherwise, it is a right
            // This means that the angle of the first new segment of the segment should be decreased by 90 degrees, or increased by 90 degrees,
            //if the turn is right, or a left, respectively.
            int angle2 = angle1 + ((i % 2 == 0) ? 90 : -90);
            // If the angle is greater than or equal to 360 degrees, or less than 0 degrees, the angle value is decreased by -360 degrees,
            //or increased by 360, respectively. This keeps the angle between 0 (inclusive) and 360 (exclusive).
            angle2 += (angle2 >= 360) ? -360 : (angle2 < 0) ? 360 : 0;

            // This sets the end point of the new line segment. This is going to be the end point of the original line segment.
            double endX2 = oldLineSegments[i].getEndX();
            double endY2 = oldLineSegments[i].getEndY();

            // A new line segment at the array index of (i * 2) + 1 (because there are twice as many more line segments in each iteration as the
            // previous) is formed from these calculated values.
            lineSegments[(i * 2) + 1] = new LineSegment(endX1, endY1, endX2, endY2, angle2);

            // If debug option NO_WAIT_BETWEEN_SEGMENTS is not enabled:
            // This makes the thread wait in between calculating (and therefore displaying) each segment.
            if (!FileHandler.Options.NO_WAIT_BETWEEN_SEGMENTS.getBoolean()) {
                try {
                    Thread.sleep(FileHandler.Options.WAIT_BETWEEN_SEGMENTS.getLong());
                } catch (Exception e) {
                    System.err.print(e);
                }
            }
        }
    }

    private void calcTyping() {

        try {

            BufferedReader reader = new BufferedReader(new InputStreamReader(this.getClass().getResourceAsStream("resources/Output.txt")));

            file = new ArrayList<String>();
            fileColors = new ArrayList<String>();

            String line;
            boolean multiLineComment = false;

            while ((line = reader.readLine()) != null) {

                file.add(line);

                String colorLine = "";

                for (int i = 0; i < line.length(); i++) {
                    colorLine += "0";
                }

                // Searches for Java keywords
                for (String keyword : javaKeywords) {
                    int previousIndex = 0;

                    while (line.indexOf(keyword, previousIndex) >= 0) {
                        if (isSingleWord(line, keyword, previousIndex)) {
                            String tempChangeColorLine = "";

                            for (int i = 0; i < keyword.length(); i++) {
                                tempChangeColorLine += "1";
                            }

                            String beforeChange = colorLine.substring(0, line.indexOf(keyword, previousIndex));
                            String afterChange = colorLine.substring(line.indexOf(keyword, previousIndex) + tempChangeColorLine.length());

                            colorLine = beforeChange + tempChangeColorLine + afterChange;
                        }

                        previousIndex = line.indexOf(keyword, previousIndex) + keyword.length();
                    }
                }

                for (int i = 0; i < line.length(); i++) {
                    String remainderOfLine = line.substring(i);

                    if (remainderOfLine.startsWith("\"") && remainderOfLine.indexOf("\"", 1) > 0) {
                        // Searches for strings

                        String tempChangeColorLine = "";

                        for (int j = 0; j < remainderOfLine.indexOf("\"", 1) + "\"".length(); j++) {
                            tempChangeColorLine += "3";
                        }

                        String beforeChange = (i > 0) ? colorLine.substring(0, i) : "";
                        String afterChange = colorLine.substring(i + remainderOfLine.indexOf("\"", 1) + "\"".length());

                        colorLine = beforeChange + tempChangeColorLine + afterChange;

                        i += remainderOfLine.indexOf("\"", 1);

                    } else if (remainderOfLine.startsWith("/**")) {
                        // Searches for the start of multi-line comments

                        String tempChangeColorLine = "";

                        for (int j = 0; j < remainderOfLine.length(); j++) {
                            tempChangeColorLine += "2";
                        }

                        String beforeChange = (i > 0) ? colorLine.substring(0, i) : "";

                        colorLine = beforeChange + tempChangeColorLine;

                        multiLineComment = true;
                    } else if (remainderOfLine.startsWith("*/")) {
                        // Searches for the end of mulit-line comments

                        String tempChangeColorLine = "";

                        for (int j = 0; j < i + "*/".length(); j++) {
                            tempChangeColorLine += "2";
                        }

                        String afterChange = colorLine.substring(i + "*/".length());

                        colorLine = tempChangeColorLine + afterChange;

                        multiLineComment = false;
                    } else if (remainderOfLine.startsWith("//")) {
                        // Searches for one-line comments

                        String tempChangeColorLine = "";

                        for (int j = 0; j < line.length() - line.indexOf("/**"); j++) {
                            tempChangeColorLine += "2";
                        }

                        String beforeChange = colorLine.substring(0, line.indexOf("//"));

                        colorLine = beforeChange + tempChangeColorLine;
                    }

                }

                if (multiLineComment && !line.contains("/**") && !line.contains("*/")) {
                    String tempChangeColorLine = "";

                    for (int i = 0; i < line.length(); i++) {
                        tempChangeColorLine += "2";
                    }

                    colorLine = tempChangeColorLine;
                }

                fileColors.add(colorLine);
            }

            for (lineIndex = 0; lineIndex < file.size(); lineIndex++) {
                for (stringIndex = 0; stringIndex < file.get(lineIndex).length() - 1; stringIndex++) {
                    Thread.sleep(10);
                }

                Thread.sleep(50);
            }

            Thread.sleep(1000);

        } catch (InterruptedException e) {
            System.err.println(e);
        } catch (IOException e) {
            System.err.println(e);
        }

    }

    private boolean isSingleWord(String line, String keyword, int previousIndex) {

        char leadingCharacter = (line.indexOf(keyword, previousIndex) == 0) ? ' ' : line.charAt(line.indexOf(keyword, previousIndex) - 1);
        char trailingCharacter = (line.indexOf(keyword, previousIndex) + keyword.length() - 1 == line.length() - 1) ? ' ' : line.charAt(line.indexOf(keyword, previousIndex) + keyword.length());

        String[] nonWordCharacters = new String[] {
                "  ",
                " :",
                "()"
        };

        for (String characters : nonWordCharacters) {
            if (leadingCharacter == characters.charAt(0) && trailingCharacter == characters.charAt(1)) {
                return true;
            }
        }

        return false;

    }

    /*
     * This function paints the screen, and is called whenever repaint() is called.
     */
    @Override
    public void paintComponent(Graphics gOrig) {
        // Creates a Graphics2D object from the passed Graphics object. This allows more complex drawing functions to be used.
        Graphics2D g = (Graphics2D) gOrig;

        if (FileHandler.Options.SCREENSAVER_TYPE.getInt() == FileHandler.ScreensaverTypes.DRAGON_CURVE.getID()) {
            paintDragonCurve(g);
        } else if (FileHandler.Options.SCREENSAVER_TYPE.getInt() == FileHandler.ScreensaverTypes.TYPING.getID()) {
            paintTyping(g);
        }
    }

    private void paintDragonCurve(Graphics2D g) {
        // Resets the screen by drawing a black rectangle that covers the entire screen.
        g.setColor(Color.black);
        g.fillRect(0, 0, screenX, screenY);

        // If debug option HIDE_PREVIOUS_ITERATION is not enabled:
        // Draws the faded line that depicts the previous iteration of lines.
        if (!FileHandler.Options.HIDE_PREVIOUS_ITERATION.getBoolean()) {
            // Sets the width of the lines to be 1 pixel, and sets the color to be yellow (RGB 255,255,0), with a high transparency (40/255)
            g.setStroke(new BasicStroke(FileHandler.Options.PREVIOUS_ITERATION_SEGMENT_WIDTH.getInt()));

            // For all of the old line segments, draw a line from their start position to their end position.
            for (int i = 0; i < oldLineSegments.length; i++) {
                Color tempColor = Color.getHSBColor((((float) 360 / oldLineSegments.length) * i) / 360, 1, 1);
                g.setColor(new Color(tempColor.getRed(), tempColor.getGreen(), tempColor.getBlue(), Math.round(FileHandler.Options.PREVIOUS_ITERATION_TRANSPARENCY.getFloat() * 255)));

                if (oldLineSegments[i] != null) {
                    g.drawLine((int) oldLineSegments[i].getStartX(), (int) oldLineSegments[i].getStartY(), (int) oldLineSegments[i].getEndX(), (int) oldLineSegments[i].getEndY());
                } else {
                    break;
                }
            }
        }

        // Sets the width of the lines to be 3 pixels
        g.setStroke(new BasicStroke(FileHandler.Options.CURRENT_ITERATION_SEGMENT_WIDTH.getInt()));

        // For all line segments that are currently calculated, draw a line from their start position to their end position.
        for (int i = 0; i < lineSegments.length; i++) {

            g.setColor(Color.getHSBColor((((float) 360 / lineSegments.length) * i) / 360, 1, 1));
            if (lineSegments[i] != null) {
                g.drawLine((int) lineSegments[i].getStartX(), (int) lineSegments[i].getStartY(), (int) lineSegments[i].getEndX(), (int) lineSegments[i].getEndY());
            } else {
                break;
            }
        }

        if (FileHandler.Options.SHOW_ARGUMENTS.getBoolean()) {
            g.setColor(Color.WHITE);

            for (int i = 0; i < Main.arguments.length; i++) {
                g.drawString(Main.arguments[i], 3, 12 * (i + 1));
            }
        }
    }

    private void paintTyping(Graphics2D g) {
        g.setColor(Color.black);
        g.fillRect(0, 0, screenX, screenY);

        g.setColor(Color.white);
        g.setFont(new Font("Courier New", Font.PLAIN, 14));

        if (file.size() > 0) {

            int maxLines = (int) Math.floor(screenY / g.getFontMetrics().getHeight()) - 1;

            int trueLineIndex = (maxLines < lineIndex) ? maxLines : lineIndex;

            for (int i = 0; i < trueLineIndex; i++) {

                String line = file.get(lineIndex - trueLineIndex + i);
                String lineColor = fileColors.get(lineIndex - trueLineIndex + i);

                if (!line.isEmpty()) {

                    int previousOffset = 5;

                    for (int j = 0; j < line.length(); j++) {
                        g.setColor(getColor(lineColor.charAt(j)));
                        g.drawString(String.valueOf(line.charAt(j)), previousOffset, (i + 1) * g.getFontMetrics().getHeight());
                        previousOffset += g.getFontMetrics().charWidth(line.charAt(j));
                    }
                }
            }

            String line = file.get(lineIndex).substring(0, stringIndex + 1);
            String lineColor = fileColors.get(lineIndex).substring(0, stringIndex + 1);

            int previousOffset = 5;

            for (int j = 0; j < line.length(); j++) {
                g.setColor(getColor(lineColor.charAt(j)));
                g.drawString(String.valueOf(line.charAt(j)), previousOffset, (trueLineIndex + 1) * g.getFontMetrics().getHeight());
                previousOffset += g.getFontMetrics().charWidth(line.charAt(j));
            }
        }
    }

    private Color getColor(char character) {
        switch (character) {
            case '1':
                return new Color(50, 100, 255);

            case '2':
                return new Color(129, 129, 129);

            case '3':
                return new Color(50, 255, 50);

            default:
                return new Color(255, 255, 255);
        }
    }

    private static final String[] javaKeywords = new String[]{
            "abstract",
            "assert",
            "boolean",
            "break",
            "byte",
            "case",
            "catch",
            "char",
            "class",
            "const",
            "continue",
            "default",
            "do",
            "double",
            "else",
            "enum",
            "extends",
            "false",
            "final",
            "finally",
            "float",
            "for",
            "goto",
            "if",
            "implements",
            "import",
            "instanceof",
            "int",
            "interface",
            "long",
            "native",
            "new",
            "null",
            "package",
            "private",
            "protected",
            "public",
            "return",
            "short",
            "static",
            "strictfp",
            "super",
            "switch",
            "synchronized",
            "this",
            "throw",
            "throws",
            "transient",
            "true",
            "try",
            "void",
            "volatile",
            "while"
    };


}