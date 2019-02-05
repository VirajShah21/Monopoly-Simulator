package Monopoly;

import java.util.ArrayList;

/**
 * The Logger class allows for the creation and storing of logs, along with displaying logs during runtime and
 * post-runtime.
 */
public class Logger {
    /**
     * An ArrayList of all the logs created during the runtime
     */
    private static final ArrayList<String> logs = new ArrayList<>();

    /**
     * A preference which allows the Logger class to print logs once they are created
     */
    public static boolean printLogsWhenCreated = false;

    /**
     * Create a new log. If Logger.printLogsWhenCreated is true, then the log will also be printed
     *
     * @param message The message to be added to the set of logs
     */
    static void log(String message) {
        logs.add(message);

        if (printLogsWhenCreated)
            System.out.println(message);
    }

    static void log(String message, boolean forcePrinting) {
        logs.add(message);

        if (forcePrinting) System.out.println(message);
    }

    /**
     * @return An ArrayList with all the logs created during the runtime
     */
    @SuppressWarnings("unused")
    static ArrayList<String> getLogs() {
        return logs;
    }
}
