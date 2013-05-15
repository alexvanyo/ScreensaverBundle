#DragonFractal

A screensaver that displays the dragon curve fractal.

In order to create a screensaver for Windows, the first thing that needs to be done is to package the files
into a executable jar. After doing so, use Launch4j or another packaging tool to turn the jar into an exe.
Then, simply rename the ----.exe to ----.scr. This scr is now a Windows screensaver.

##Classes

####Main:
Initializes the screensaver, or frame, given the passed arguments, after loading the options with a FileHandler.

####ScreensaverScreen:
Handles painting and calculating the screensaver.

####ScreensaverFrame:
Creates a frame that is fullscreen with no borders.

####SettingsScreen:
Handles drawing of the seperate OptionSelectors.

####SettingsFrame:
Creates a frame with the desired size and options.

####LineSegment:
Serves as a wrapper class that contains 5 values: startX, startY, endX, endY, and angle.
An array of these values is used to represent the fractal.

####Listeners:
This class extends all listeners needed so any action will quit the program, functionality for a screensaver.

####FileHandler:
Loads the config.txt file and stores the constants for other classes to access.

####OptionSelector
All of the options are contained inside an OptionSelector.
It contains the text area / dropdown, default values, and labels, as well as the functionality of those.
