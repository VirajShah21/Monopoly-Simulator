package Monopoly;

import java.util.ArrayList;

/**
 * The Logger class allows for the creation and storing of logs, along with features for verbose mode.
 */
public class Logger {
    private static final ArrayList<String> logs = new ArrayList<>();
    public static boolean printLogsWhenCreated = false;

    static void log(String message) {
        logs.add(message);

        if (printLogsWhenCreated)
            System.out.println(message);
    }

    @SuppressWarnings("unused")
    static ArrayList<String> getLogs() {
        return logs;
    }
}
