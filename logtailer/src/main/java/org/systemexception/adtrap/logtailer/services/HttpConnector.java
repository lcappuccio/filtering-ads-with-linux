package org.systemexception.adtrap.logtailer.services;

import org.systemexception.logger.api.Logger;
import org.systemexception.logger.impl.LoggerImpl;

import java.util.concurrent.LinkedBlockingQueue;

/**
 * @author leo
 */
public class HttpConnector implements Runnable {

	private static final Logger LOGGER = LoggerImpl.getFor(HttpConnector.class);
	private final LinkedBlockingQueue blockingQueue;

	public HttpConnector(final LinkedBlockingQueue blockingQueue) {
		this.blockingQueue = blockingQueue;
	}

	@Override
	public void run() {
		try {
			while (true) {
				String take = (String) blockingQueue.take();
				LOGGER.info("Sent " + take);
			}
		} catch (InterruptedException | ClassCastException e) {
			LogTailerListener.logInterruptedException(e);
		}
	}
}
