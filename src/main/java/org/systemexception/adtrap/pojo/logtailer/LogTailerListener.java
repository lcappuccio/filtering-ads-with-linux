package org.systemexception.adtrap.pojo.logtailer;

import org.apache.commons.io.input.Tailer;
import org.apache.commons.io.input.TailerListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.systemexception.adtrap.pojo.LogQueue;

import java.io.FileNotFoundException;

@Service
public class LogTailerListener implements TailerListener {

	private static final Logger LOGGER = LoggerFactory.getLogger(LogTailerListener.class);
	private static final int MAX_QUEUE_SIZE = 1000;
	private final LogQueue blockingQueue;

	@Autowired
	public LogTailerListener(LogQueue blockingQueue) {
		this.blockingQueue = blockingQueue;
	}

	@Override
	public void init(Tailer tailer) {
		LOGGER.info("Init LogTailerListener");
	}

	@Override
	public void fileNotFound() {
		LOGGER.error("File not found", new FileNotFoundException());
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			logInterruptedException(e);
		}
	}

	@Override
	public void fileRotated() {
		LOGGER.info("File rotated");
	}

	@Override
	public void handle(String line) {
		try {
			if (blockingQueue.size() <= MAX_QUEUE_SIZE) {
				blockingQueue.put(line);
			} else {
				LOGGER.error("Queue blocked, waiting " + MAX_QUEUE_SIZE + "ms");
				Thread.sleep(50);
			}
		} catch (InterruptedException e) {
			logInterruptedException(e);
		}
	}

	@Override
	public void handle(Exception ex) {
		LOGGER.error("Exception", ex);
	}

	private static void logInterruptedException(Exception e) {
		LOGGER.error(e.getMessage(), e);
	}
}
