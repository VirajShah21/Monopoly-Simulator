package Monopoly.LoggerTools;
import java.util.ArrayList;


/**
 * The Logger class allows for the creation and storing of logs, along with displaying logs during runtime and
 * post-runtime.
 */
public class Logger {
    /**
     * An ArrayList of all the logs created during the runtime
     */
    private static ArrayList<String> logs = new ArrayList<>();
    private static ArrayList<LandingLog> landings = new ArrayList<>();

    /**
     * A preference which allows the Logger class to print logs once they are created
     */
    public static boolean printLogsWhenCreated = false;

    /**
     * Create a new log. If Logger.printLogsWhenCreated is true, then the log will also be printed
     *
     * @param message The message to be added to the set of logs
     */
    public static void log(String message) {
        logs.add(message);

        if (printLogsWhenCreated)
            System.out.println(message);
    }

    /**
     * Creates a new log and allows forced printing/print-suppression
     *
     * @param message        The message to be added to the set of logs
     * @param allowedToPrint Whether this method call should print out the message upon creation
     */
    public static void log(String message, boolean allowedToPrint) {
        logs.add(message);

        if (allowedToPrint) System.out.println(message);
    }

    /**
     * Creates a new LandingLog log
     *
     * @param log The log to be added to the log stream
     */
    public static void log(LandingLog log) {
        landings.add(log);
    }

    /**
     * @return An ArrayList with all the logs created during the runtime
     */
    @SuppressWarnings("unused")
    public static ArrayList<String> getLogStream() {
        return logs;
    }

    /**
     * @return The landing log stream
     */
    public static ArrayList<LandingLog> getLandingLogStream() {
        return landings;
    }

    /**
     * Clears out all the logs from all the streams
     */
    public static void clearAll() {
        logs.clear();
        landings.clear();
    }

    public static void clearAllMessageLogs() {
        logs.clear();
    }

    public static void clearAllLandingLogs() { landings.clear(); }
}
