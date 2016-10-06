package org.systemexception.adtrap.logtailer.main;

import org.apache.commons.cli.*;
import org.systemexception.adtrap.logtailer.services.HttpConnector;
import org.systemexception.adtrap.logtailer.services.LogTailer;
import org.systemexception.adtrap.logtailer.services.LogTailerListener;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.concurrent.LinkedBlockingQueue;

public class Main {

	private static final String FILE_OPTION = "f", SLEEP_OPTION = "s";

	public static void main(String[] args) throws FileNotFoundException, InterruptedException, ParseException {

		Options options = options();
		CommandLineParser commandLineParser = new DefaultParser();
		CommandLine commandLine = commandLineParser.parse(options, args);

		if (!commandLine.hasOption(FILE_OPTION) || !commandLine.hasOption(SLEEP_OPTION)) {
			printUsage(options);
			throw new MissingOptionException(options.getRequiredOptions());
		}

		Integer sleepTimer;
		try {
			sleepTimer = Integer.valueOf(commandLine.getOptionValue(SLEEP_OPTION));
		} catch (NumberFormatException e) {
			printUsage(options);
			throw new NumberFormatException();
		}

		String fileName = commandLine.getOptionValue(FILE_OPTION);
		File fileToTail = new File(fileName);
		if (!fileToTail.exists()) {
			throw new FileNotFoundException();
		}

		LinkedBlockingQueue<String> blockingQueue = new LinkedBlockingQueue();
		LogTailerListener logTailerListener = new LogTailerListener(blockingQueue);
		LogTailer logTailer = new LogTailer(fileToTail, logTailerListener, sleepTimer);

		HttpConnector httpConnector = new HttpConnector(blockingQueue);

		logTailer.run();
		httpConnector.run();
	}

	/**
	 * Build the command line options
	 *
	 * @return command line options
	 */
	private static Options options() {
		Options options = new Options();
		Option filenameOption = new Option(FILE_OPTION, true, "filename to tail with path");
		Option pollingOption = new Option(SLEEP_OPTION, true, "sleep timer in milliseconds");
		options.addOption(filenameOption);
		options.addOption(pollingOption);
		return options;
	}

	/**
	 * Print usage help
	 *
	 * @param options
	 */
	private static void printUsage(Options options) {
		HelpFormatter formatter = new HelpFormatter();
		formatter.printHelp("java -jar logtailer.jar", options);
	}

}
