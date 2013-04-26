import java.awt.event.KeyEvent;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;

/**
 * Created with IntelliJ IDEA.
 * User: Alex
 * Date: 4/23/13
 * Time: 8:46 PM
 */

public class FileHandler {

    public void loadOptions() {
        ArrayList<String> optionsList = new ArrayList<String>();

        URL file = this.getClass().getResource("config.txt");

        try {
            BufferedReader br = new BufferedReader(new FileReader(file.getFile()));

            String line = br.readLine();
            while (line != null) {
                optionsList.add(line);

                line = br.readLine();
            }

            br.close();
        } catch (FileNotFoundException e) {
            System.err.print(e);
        } catch (IOException e) {
            System.err.print(e);
        }

        for (Options optionI : Options.values()) {
            for (String lineI : optionsList) {
                if (lineI.startsWith(optionI.getName())) {
                    optionI.setValue(lineI.substring(optionI.getName().length() + 1));
                }
            }
        }
    }

    public static enum Options {
        NO_WAIT_BETWEEN_SEGMENTS("no-wait-between-segments", "false", OptionTypes.BOOLEAN),
        HIDE_PREVIOUS_ITERATION("hide-previous-iteration", "false", OptionTypes.BOOLEAN),
        MAX_NUMBER_OF_ITERATIONS("max-number-of-iterations", "14", OptionTypes.INT),
        WAIT_BETWEEN_ITERATIONS("wait-between-iterations", "500", OptionTypes.LONG),
        WAIT_BETWEEN_SEGMENTS("wait-between-segments", "25", OptionTypes.LONG),
        ONLY_EXIT_ON_KEY("only-exit-on-key", "false", OptionTypes.BOOLEAN),
        EXIT_KEY("exit-key", "27", OptionTypes.INT),
        CURRENT_ITERATION_SEGMENT_WIDTH("current-iteration-segment-width", "3", OptionTypes.INT),
        PREVIOUS_ITERATION_SEGMENT_WIDTH("previous-iteration-segment-width", "2", OptionTypes.INT),
        PREVIOUS_ITERATION_TRANSPARENCY("previous-iteration-transparency", "0.2", OptionTypes.FLOAT);

        private final OptionTypes type;
        private String value;
        private final String name;

        private Options(String name, String defaultValue, OptionTypes type) {
            this.name = name;
            this.value = defaultValue;
            this.type = type;
        }

        public String getName() {
            return name;
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

    private enum OptionTypes {
        BOOLEAN,
        INT,
        LONG,
        FLOAT
    }
}
