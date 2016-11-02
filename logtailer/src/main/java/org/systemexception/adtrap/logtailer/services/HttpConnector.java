package org.systemexception.adtrap.logtailer.services;

import org.systemexception.logger.api.Logger;
import org.systemexception.logger.impl.LoggerImpl;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * @author leo
 */
public class HttpConnector implements Runnable {

	private static final String APPLICATION_JSON_PROPERTY = "application/json";
	private static final Logger LOGGER = LoggerImpl.getFor(HttpConnector.class);
	private final LinkedBlockingQueue blockingQueue;

	public HttpConnector(final LinkedBlockingQueue blockingQueue) {
		this.blockingQueue = blockingQueue;
	}

	@Override
	public void run() {
		try {
			while (true) {
				String logLine = (String) blockingQueue.take();
				postLine(logLine);
				LOGGER.info("Sent " + logLine);
			}
		} catch (InterruptedException | ClassCastException e) {
			LogTailerListener.logInterruptedException(e);
		}
	}

	private void postLine(final String logLine) throws InterruptedException {
		try {
			URL url = new URL("http://localhost:8080/statsviewer/add");
			HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
			urlConnection.setRequestMethod("POST");
			urlConnection.setDoOutput(true);
			urlConnection.setConnectTimeout(1000);
			urlConnection.setRequestProperty("Content-Type", APPLICATION_JSON_PROPERTY);
			urlConnection.setRequestProperty("Accept", APPLICATION_JSON_PROPERTY);
			OutputStreamWriter osw = new OutputStreamWriter(urlConnection.getOutputStream());
			osw.write(JsonMapper.jsonFromLogLine(logLine));
			osw.flush();
			osw.close();
		} catch (IOException ex) {
			LOGGER.error("Unreachable", ex);
		}
	}
}
