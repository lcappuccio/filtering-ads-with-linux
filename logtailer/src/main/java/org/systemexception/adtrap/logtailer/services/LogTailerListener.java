package org.systemexception.adtrap.logtailer.services;

import org.apache.commons.io.input.Tailer;
import org.apache.commons.io.input.TailerListener;
import org.systemexception.logger.api.Logger;
import org.systemexception.logger.impl.LoggerImpl;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

public class LogTailerListener implements TailerListener {

	private static final Logger LOGGER = LoggerImpl.getFor(LogTailerListener.class);
	private List<String> lines = new ArrayList<>();
	private final int threadSleep;

	public LogTailerListener(final int threadSleep) {
		this.threadSleep = threadSleep;
	}

	@Override
	public void init(Tailer tailer) {
		lines = new ArrayList<>();
	}

	@Override
	public void fileNotFound() {
		LOGGER.error("File not found", new FileNotFoundException());
		try {
			Thread.sleep(threadSleep);
		} catch (InterruptedException e) {
			logInterruptedException();
		}
	}

	@Override
	public void fileRotated() {
		LOGGER.info("File rotated");
		try {
			Thread.sleep(threadSleep);
		} catch (InterruptedException e) {
			logInterruptedException();
		}
	}

	@Override
	public void handle(String line) {
		HttpConnector httpConnector = new HttpConnector(line);
		httpConnector.run();
	}

	@Override
	public void handle(Exception ex) {
		LOGGER.error("Exception", ex);
	}

	/**
	 * Get the lines in buffer
	 *
	 * @return the lines in buffer
	 */
	public List<String> getLines() {
		LOGGER.info("Get " + lines.size() + " lines");
		return lines;
	}

	/**
	 * Clear the lines in the buffer, to be called after fetching them
	 */
	public void clearLines() {
		LOGGER.info("Clear lines");
		lines = new ArrayList<>();
	}

	private void logInterruptedException() {
		LOGGER.error("Thread sleep error", new InterruptedException());
	}
}
