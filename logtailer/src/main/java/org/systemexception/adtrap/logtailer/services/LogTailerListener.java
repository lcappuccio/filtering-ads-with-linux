package org.systemexception.adtrap.logtailer.services;

import org.apache.commons.io.input.Tailer;
import org.apache.commons.io.input.TailerListener;
import org.systemexception.logger.api.Logger;
import org.systemexception.logger.impl.LoggerImpl;

import java.io.FileNotFoundException;
import java.util.concurrent.LinkedBlockingQueue;

public class LogTailerListener implements TailerListener {

	private static final Logger LOGGER = LoggerImpl.getFor(LogTailerListener.class);
	private final LinkedBlockingQueue<String> blockingQueue;

	public LogTailerListener(LinkedBlockingQueue<String> blockingQueue) {
		this.blockingQueue = blockingQueue;
	}

	@Override
	public void init(Tailer tailer) {
		LOGGER.info("Init LogTailerListener");
	}

	@Override
	public void fileNotFound() {
		LOGGER.error("File not found", new FileNotFoundException());
	}

	@Override
	public void fileRotated() {
		LOGGER.info("File rotated");
	}

	@Override
	public void handle(String line) {
		try {
			blockingQueue.put(line);
		} catch (InterruptedException e) {
			logInterruptedException(e);
		}
	}

	@Override
	public void handle(Exception ex) {
		LOGGER.error("Exception", ex);
	}

	public static void logInterruptedException(Exception e) {
		LOGGER.error(e.getMessage(), e);
	}
}
