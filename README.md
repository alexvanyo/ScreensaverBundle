#DragonFractal

A screensaver that displays the dragon curve fractal.

In order to create a screensaver for Windows, the first thing that needs to be done is to package the files
into a executable jar. After doing so, use Launch4j or another packaging tool to turn the jar into an exe.
Then, simply rename the ----.exe to ----.scr. This scr is now a Windows screensaver.

##Classes

#### Main:
Creates overlying JFrame, and serves as a container for the rest of the program. Also holds debug finals.

####Screen:
Most important class, handles painting line segments and the calculating of new line segments.

####LineSegment:
Serves as a wrapper class that contains 5 values: startX, startY, endX, endY, and angle.
An array of these values is used to represent the fractal.

####Listeners:
This class extends all listeners needed so any action will quit the program, functionality for a screensaver.

####FileHandler:
Loads the config.txt file and stores the constants for other classes to access.

##Todo

Add configuration/settings file, so screensaver is fully customizable

Continuously request focus so other applications can't appear in front of it
