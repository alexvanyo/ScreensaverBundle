import java.io.*;
import java.net.URI;
import java.util.ArrayList;

/**
 * Created with IntelliJ IDEA.
 * User: Alex
 * Date: 4/23/13
 * Time: 8:46 PM
 */

public class FileHandler {

    private static final String localConfigFile = "config.txt";
    private static final String configFile = System.getenv("APPDATA") + "\\ScreensaverBundle\\config.txt";

    public static void loadOptions() {
        ArrayList<String> optionsList = getOptionsAsStrings();

        // cycle through the options and optionsList and check to see if any values need to be updated
        for (String lineI : optionsList) {
            for (Options optionI : Options.values()) {
                if (lineI.startsWith(optionI.getName())) {
                    optionI.setValue(lineI.substring(optionI.getName().length() + 1));
                    break;
                }
            }
        }
    }

    public static void saveOptions() {
        ArrayList<String> previousOptionsList = getOptionsAsStrings();

        try {
            File file = new File(URI.create("file:/" + configFile.replace("\\", "/")));
            file.getParentFile().mkdirs();
            file.createNewFile();

            FileOutputStream outputStream = new FileOutputStream(configFile);

            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(outputStream));

            for (String configLineI : previousOptionsList) {
                if (configLineI.startsWith("#")) {
                    bw.write(configLineI);
                    bw.newLine();
                } else {
                    bw.newLine();
                    break;
                }
            }

            for (Options optionI : Options.values()) {
                if (!optionI.getValue().equals(optionI.getDefaultValue())) {
                    bw.write(optionI.getName() + "=" + optionI.getValue());
                    bw.newLine();
                }
            }

            bw.close();
        } catch (FileNotFoundException e) {
            System.err.println(e);
        } catch (IOException e) {
            System.out.println(e);
        }
    }

    private static ArrayList<String> getOptionsAsStrings() {

        ArrayList<String> optionsList = new ArrayList<String>();

        try {
            InputStream file;
            if (new File(configFile).exists()) {
                file = new FileInputStream(configFile);
            } else {
                file = FileHandler.class.getResourceAsStream(localConfigFile);
            }

            // read the config.txt file and store all lines in optionsList
            BufferedReader br = new BufferedReader(new InputStreamReader(file));

            String line = br.readLine();
            while (line != null) {
                optionsList.add(line);

                line = br.readLine();
            }

            br.close();
        } catch (FileNotFoundException e) {
            System.err.println(e);
        } catch (IOException e) {
            System.err.println(e);
        }

        return optionsList;
    }

    public static enum Options {
        // initialize all configurable variables with their config.txt label, default value, and type of variable
        NO_WAIT_BETWEEN_SEGMENTS("no-wait-between-segments", "false", OptionTypes.BOOLEAN),
        HIDE_PREVIOUS_ITERATION("hide-previous-iteration", "false", OptionTypes.BOOLEAN),
        MAX_NUMBER_OF_ITERATIONS("max-number-of-iterations", "14", OptionTypes.INT),
        WAIT_BETWEEN_ITERATIONS("wait-between-iterations", "500", OptionTypes.LONG),
        WAIT_BETWEEN_SEGMENTS("wait-between-segments", "25", OptionTypes.LONG),
        ONLY_EXIT_ON_KEY("only-exit-on-key", "false", OptionTypes.BOOLEAN),
        EXIT_KEY("exit-key", "27", OptionTypes.INT),
        CURRENT_ITERATION_SEGMENT_WIDTH("current-iteration-segment-width", "3", OptionTypes.INT),
        PREVIOUS_ITERATION_SEGMENT_WIDTH("previous-iteration-segment-width", "2", OptionTypes.INT),
        PREVIOUS_ITERATION_TRANSPARENCY("previous-iteration-transparency", "0.2", OptionTypes.FLOAT),
        SHOW_ARGUMENTS("show-arguments", "false", OptionTypes.BOOLEAN),
        SCREENSAVER_END("screensaver-end", "-1", OptionTypes.FLOAT);

        private final OptionTypes type;
        private String value;
        private String defaultValue;
        private final String name;

        private Options(String name, String defaultValue, OptionTypes type) {
            this.name = name;
            this.value = defaultValue;
            this.defaultValue = defaultValue;
            this.type = type;
        }

        public String getName() {
            return name;
        }

        public OptionTypes getType() {
            return type;
        }

        public String getDefaultValue() {
            return defaultValue;
        }

        public String getValue() {
            return value;
        }

        public void setValue(String value) {
            this.value = value;
        }

        public boolean getBoolean() {
            return this.type.equals(OptionTypes.BOOLEAN) && Boolean.parseBoolean(value);

        }

        public int getInt() {
            if (this.type.equals(OptionTypes.INT)) {
                return Integer.parseInt(value);
            }

            return 0;
        }

        public long getLong() {
            if (this.type.equals(OptionTypes.LONG)) {
                return Long.parseLong(value);
            }

            return 0l;
        }

        public float getFloat() {
            if (this.type.equals(OptionTypes.FLOAT)) {
                return Float.parseFloat(value);
            }

            return 0f;
        }
    }

    public static enum OptionTypes {
        BOOLEAN,
        INT,
        LONG,
        FLOAT
    }
}
