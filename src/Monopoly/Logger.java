package Monopoly;

import java.util.ArrayList;

class Logger {
    private static final ArrayList<String> logs = new ArrayList<>();

    static void log(String message) {
        logs.add(message);

        if (Prefs.printLogsWhenCreated)
            System.out.println(message);
    }

    @SuppressWarnings("unused")
    static ArrayList<String> getLogs() {
        return logs;
    }
}
