package org.systemexception.adtrap.logtailer.main;

import org.systemexception.adtrap.logtailer.services.LogTailer;
import org.systemexception.adtrap.logtailer.services.LogTailerListener;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.List;

public class Main {

	private static File fileToTail;
	private static LogTailerListener logTailerListener;
	private static LogTailer logTailer;

	public static void main(String[] args) throws FileNotFoundException, InterruptedException {

		final String fileName = args[0];

		fileToTail = new File(fileName);

		// if (!fileToTail.exists()) {
		// 	throw new FileNotFoundException();
		// }

		logTailerListener = new LogTailerListener();
		logTailer = new LogTailer(fileToTail, logTailerListener, 100);

		System.out.println("Starting listener");
		Thread thread = new Thread(logTailer);
		thread.start();

		while (true) {
			System.out.println("Listening");
			Thread.sleep(500 * 2);
			List<String> listenerLines = logTailerListener.getLines();
			System.out.println(listenerLines);
			logTailerListener.clearLines();
		}
	}

}
