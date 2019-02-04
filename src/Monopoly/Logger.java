package Monopoly;

import java.util.ArrayList;

public class Logger {
    public static final ArrayList<String> logs = new ArrayList<>();

    public static void log(String message) {
        logs.add(message);

        if (Prefs.printLogsWhenCreated)
            System.out.println(message);
    }
}
