package org.virajshah.monopoly.logs;

import java.util.ArrayList;

/**
 * 
 * @author Viraj Shah
 *
 */
public class Logger {
	/**
	 * ANSI reset color escape code
	 */
	public static final String ANSI_RESET = "\u001B[0m";

	/**
	 * ANSI black color escape code
	 */
	public static final String ANSI_BLACK = "\u001B[30m";

	/**
	 * ANSI red color escape code
	 */
	public static final String ANSI_RED = "\u001B[31m";

	/**
	 * ANSI green color escape code
	 */
	public static final String ANSI_GREEN = "\u001B[32m";

	/**
	 * ANSI yellow color escape code
	 */
	public static final String ANSI_YELLOW = "\u001B[33m";

	/**
	 * ANSI blue color escape code
	 */
	public static final String ANSI_BLUE = "\u001B[34m";

	/**
	 * ANSI purple color escape code
	 */
	public static final String ANSI_PURPLE = "\u001B[35m";

	/**
	 * ANSI cyan color escape code
	 */
	public static final String ANSI_CYAN = "\u001B[36m";

	/**
	 * Logs will be printed if true
	 */
	private boolean printingEnabled;

	/**
	 * The list of logs
	 */
	private ArrayList<String> logs;

	/**
	 * Default constructor (printingEnabled=true)
	 */
	public Logger() {
		this.printingEnabled = true;
		this.logs = new ArrayList<>();
	}

	/**
	 * Constructs a logger object
	 * 
	 * @param printingEnabled Whether printing should be enabled
	 */
	public Logger(boolean printingEnabled) {
		this.printingEnabled = printingEnabled;
		this.logs = new ArrayList<>();
	}

	/**
	 * Logs info to the logger
	 * 
	 * @param message The message to be logged
	 */
	public void info(String message) {
		logs.add(message);
		printLastLog();
	}

	/**
	 * Logs info to the logger in green
	 * 
	 * @param message The message to be logged
	 */
	public void infoGreen(String message) {
		logs.add(ANSI_GREEN + message + ANSI_RESET);
	}

	/**
	 * Logs info to the logger in blue
	 * 
	 * @param message The message to be logged
	 */
	public void infoBlue(String message) {
		logs.add(ANSI_BLUE + message + ANSI_RESET);
	}

	/**
	 * Logs info to the logger in purple
	 * 
	 * @param message The message to be logged
	 */
	public void infoPurple(String message) {
		logs.add(ANSI_PURPLE + message + ANSI_RESET);
	}

	/**
	 * Logs info to the logger in cyan
	 * 
	 * @param message The message to be logged
	 */
	public void infoCyan(String message) {
		logs.add(ANSI_CYAN + message + ANSI_RESET);
	}

	/**
	 * Logs a warning to the logger (in yellow)
	 * 
	 * @param message The message to be logged
	 */
	public void warn(String message) {
		logs.add(ANSI_YELLOW + message + ANSI_RESET);
		printLastLog();
	}

	/**
	 * Logs an error to the logger (in red)
	 * 
	 * @param message The message to be logged
	 */
	public void error(String message) {
		logs.add(ANSI_RED + message + ANSI_RESET);
		printLastLog();
	}

	/**
	 * Prints the last log from the list of logs
	 */
	private void printLastLog() {
		if (printingEnabled) {
			System.out.println(logs.get(logs.size() - 1));
		}
	}

	/**
	 * @return the printingEnabled
	 */
	public boolean isPrintingEnabled() {
		return printingEnabled;
	}

	/**
	 * @param printingEnabled the printingEnabled to set
	 */
	public void setPrintingEnabled(boolean printingEnabled) {
		this.printingEnabled = printingEnabled;
	}
}