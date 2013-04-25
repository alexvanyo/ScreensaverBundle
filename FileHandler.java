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

    public static boolean NO_WAIT_BETWEEN_SEGMENTS = false;
    public static boolean HIDE_PREVIOUS_ITERATION = false;
    public static int MAX_NUMBER_OF_ITERATIONS = 20;
    public static long WAIT_BETWEEN_ITERATIONS = 500;
    public static long WAIT_BETWEEN_SEGMENTS = 25;
    public static boolean ONLY_EXIT_ON_KEY = false;
    public static int EXIT_KEY = KeyEvent.VK_ESCAPE;

    private ArrayList<String> optionsList;

    public void loadOptions() {
        optionsList = new ArrayList<String>();

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
            optionI.setValue(get(optionI));
        }
    }

    public String get(Options option) {
        String value = "";

        for (String lineI : optionsList) {
            if (lineI.startsWith(option.getString())) {
                value = lineI.substring(option.getString().length() + 1);
            }
        }

        return value;
    }

    public enum Options {
        NO_WAIT_BETWEEN_SEGMENTS_OPTIONS("no-wait-between-segments", OptionTypes.BOOLEAN),
        HIDE_PREVIOUS_ITERATION_OPTIONS("hide-previous-iteration", OptionTypes.BOOLEAN),
        MAX_NUMBER_OF_ITERATIONS_OPTIONS("max-number-of-iterations", OptionTypes.INT),
        WAIT_BETWEEN_ITERATIONS_OPTIONS("wait-between-iterations", OptionTypes.LONG),
        WAIT_BETWEEN_SEGMENTS_OPTIONS("wait-between-segments", OptionTypes.LONG),
        ONLY_EXIT_ON_KEY_OPTIONS("only-exit-on-key", OptionTypes.BOOLEAN),
        EXIT_KEY_OPTIONS("exit-key", OptionTypes.INT);

        private String name;
        private OptionTypes type;

        private Options(String name, OptionTypes type) {
            this.name = name;
            this.type = type;
        }

        public String getString() {
            return name;
        }

        public void setValue(String value) {
            if (value.isEmpty()) {
                return;
            }
            if (name.equals(NO_WAIT_BETWEEN_SEGMENTS_OPTIONS.getString())) {
                NO_WAIT_BETWEEN_SEGMENTS = Boolean.parseBoolean(value);
            } else if (name.equals(HIDE_PREVIOUS_ITERATION_OPTIONS.getString())) {
                HIDE_PREVIOUS_ITERATION = Boolean.parseBoolean(value);
            } else if (name.equals(MAX_NUMBER_OF_ITERATIONS_OPTIONS.getString())) {
                MAX_NUMBER_OF_ITERATIONS = Integer.parseInt(value);
            } else if (name.equals(WAIT_BETWEEN_ITERATIONS_OPTIONS.getString())) {
                WAIT_BETWEEN_ITERATIONS = Long.parseLong(value);
            } else if (name.equals(WAIT_BETWEEN_SEGMENTS_OPTIONS.getString())) {
                WAIT_BETWEEN_SEGMENTS = Long.parseLong(value);
            } else if (name.equals(ONLY_EXIT_ON_KEY_OPTIONS.getString())) {
                ONLY_EXIT_ON_KEY = Boolean.parseBoolean(value);
            } else if (name.equals(EXIT_KEY_OPTIONS.getString())) {
                EXIT_KEY = Integer.parseInt(value);
            }
        }

        private enum OptionTypes {
            BOOLEAN,
            INT,
            LONG;
        }
    }
}
