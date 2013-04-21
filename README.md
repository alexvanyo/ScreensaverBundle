# DragonFractal

A screensaver that displays the dragon curve fractal.

## Classes
#### Main:
Creates overlying JFrame, and serves as a container for the rest of the program. Also holds debug finals.

#### Screen:
Most important class, handles painting line segments and the calculating of new line segments.

#### LineSegment:
Serves as a wrapper class that contains 5 values: startX, startY, endX, endY, and angle.
An array of these values is used to represent the fractal.

#### Listeners:
This class extends all listeners needed so any action will quit the program, functionality for a screensaver.
