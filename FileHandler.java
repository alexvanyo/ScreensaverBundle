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

        for (FileOptions optionI : FileOptions.values()) {
            optionI.setValue(get(optionI));
        }
    }

    public String get(FileOptions option) {
        String value = "";

        for (String lineI : optionsList) {
            if (lineI.startsWith(option.getName())) {
                value = lineI.substring(option.getName().length() + 1);
            }
        }

        return value;
    }

    public static enum BooleanOptions {
        NO_WAIT_BETWEEN_SEGMENTS(false),
        HIDE_PREVIOUS_ITERATION(false),
        ONLY_EXIT_ON_KEY(false);

        private boolean value;

        private BooleanOptions(boolean value) {
            this.value = value;
        }

        public boolean getValue() {
            return value;
        }

        public void setValue(boolean value) {
            this.value = value;
        }
    }

    public static enum IntOptions {
        EXIT_KEY(KeyEvent.VK_ESCAPE),
        MAX_NUMBER_OF_ITERATIONS(20);

        private int value;

        private IntOptions(int value) {
            this.value = value;
        }

        public int getValue() {
            return value;
        }

        public void setValue(int value) {
            this.value = value;
        }
    }

    public static enum LongOptions {
        WAIT_BETWEEN_ITERATIONS(500),
        WAIT_BETWEEN_SEGMENTS(25);

        private long value;

        private LongOptions(long value) {
            this.value = value;
        }

        public long getValue() {
            return value;
        }

        public void setValue(long value) {
            this.value = value;
        }
    }

    public static enum FileOptions {
        NO_WAIT_BETWEEN_SEGMENTS_OPTIONS("no-wait-between-segments", OptionTypes.BOOLEAN, BooleanOptions.NO_WAIT_BETWEEN_SEGMENTS, null, null),
        HIDE_PREVIOUS_ITERATION_OPTIONS("hide-previous-iteration", OptionTypes.BOOLEAN, BooleanOptions.HIDE_PREVIOUS_ITERATION, null, null),
        MAX_NUMBER_OF_ITERATIONS_OPTIONS("max-number-of-iterations", OptionTypes.INT, null, IntOptions.MAX_NUMBER_OF_ITERATIONS, null),
        WAIT_BETWEEN_ITERATIONS_OPTIONS("wait-between-iterations", OptionTypes.LONG, null, null, LongOptions.WAIT_BETWEEN_ITERATIONS),
        WAIT_BETWEEN_SEGMENTS_OPTIONS("wait-between-segments", OptionTypes.LONG, null, null, LongOptions.WAIT_BETWEEN_SEGMENTS),
        ONLY_EXIT_ON_KEY_OPTIONS("only-exit-on-key", OptionTypes.BOOLEAN, BooleanOptions.ONLY_EXIT_ON_KEY, null, null),
        EXIT_KEY_OPTIONS("exit-key", OptionTypes.INT, null, IntOptions.EXIT_KEY, null);

        private String name;
        private OptionTypes type;

        private BooleanOptions booleanOption;
        private IntOptions intOption;
        private LongOptions longOption;

        private FileOptions(String name, OptionTypes type, BooleanOptions booleanOption, IntOptions intOption, LongOptions longOption) {
            this.name = name;
            this.type = type;

            if (booleanOption != null) {
                this.booleanOption = booleanOption;
            } else if (intOption != null) {
                this.intOption = intOption;
            } else if (longOption != null) {
                this.longOption = longOption;
            }
        }

        public String getName() {
            return name;
        }

        public OptionTypes getType() {
            return type;
        }

        public void setValue(String value) {
            if (value.isEmpty()) {
                return;
            }

            boolean booleanReturnValue = false;
            boolean boolRetValInit = false;

            int intReturnValue = 0;
            boolean intRetValInit = false;

            long longReturnValue = 0l;
            boolean longRetValInit = false;

            for (FileOptions optionI : FileOptions.values()) {
                if (name.equals(optionI.getName())) {
                    if (optionI.getType() == OptionTypes.BOOLEAN) {
                        booleanReturnValue = Boolean.parseBoolean(value);
                        boolRetValInit = true;
                    } else if (optionI.getType() == OptionTypes.INT) {
                        intReturnValue = Integer.parseInt(value);
                        intRetValInit = true;
                    } else if (optionI.getType() == OptionTypes.LONG) {
                        longReturnValue = Long.parseLong(value);
                        longRetValInit = true;
                    }
                }
            }

            if (boolRetValInit) {
                booleanOption.setValue(booleanReturnValue);
            } else if (intRetValInit) {
                intOption.setValue(intReturnValue);
            } else if (longRetValInit) {
                longOption.setValue(longReturnValue);
            }
        }

        private enum OptionTypes {
            BOOLEAN,
            INT,
            LONG;
        }
    }
}
