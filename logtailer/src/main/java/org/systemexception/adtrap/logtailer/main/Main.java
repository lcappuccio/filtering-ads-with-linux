package org.systemexception.adtrap.logtailer.main;

import org.apache.commons.cli.*;
import org.systemexception.adtrap.logtailer.services.LogTailer;
import org.systemexception.adtrap.logtailer.services.LogTailerListener;

import java.io.File;
import java.io.FileNotFoundException;

class Main {

	private static final String FILE_OPTION = "f", SLEEP_OPTION = "s";

	public static void main(String[] args) throws FileNotFoundException, InterruptedException, ParseException {

		Options options = options();
		CommandLineParser commandLineParser = new DefaultParser();
		CommandLine commandLine = commandLineParser.parse(options, args);

		if (!commandLine.hasOption(FILE_OPTION) || !commandLine.hasOption(SLEEP_OPTION)) {
			HelpFormatter formatter = new HelpFormatter();
			formatter.printHelp("java -jar logtailer.jar", options);
			System.exit(1);
		}

		String fileName = commandLine.getOptionValue(FILE_OPTION);
		File fileToTail = new File(fileName);

		Integer sleepTimer = Integer.valueOf(commandLine.getOptionValue(SLEEP_OPTION));
		LogTailerListener logTailerListener = new LogTailerListener(sleepTimer);
		LogTailer logTailer = new LogTailer(fileToTail, logTailerListener, sleepTimer);

		logTailer.run();
	}

	/**
	 * Build the command line options
	 *
	 * @return command line options
	 */
	private static Options options() {
		Options options = new Options();
		Option filenameOption = new Option(FILE_OPTION, true, "filename to tail with path");
		Option pollingOption = new Option(SLEEP_OPTION, true, "sleep timer");
		options.addOption(filenameOption);
		options.addOption(pollingOption);
		return options;
	}

}
