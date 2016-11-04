package org.systemexception.adtrap.logtailer.services;

import com.sun.org.apache.xerces.internal.impl.dv.util.Base64;
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
	private final JsonMapper jsonMapper = new JsonMapper();

	public HttpConnector(final LinkedBlockingQueue blockingQueue) {
		this.blockingQueue = blockingQueue;
	}

	@Override
	public void run() {
		try {
			while (true) {
				String logLine = (String) blockingQueue.take();
				if (jsonMapper.jsonFromLogLine(logLine).isPresent()) {
					postLine(jsonMapper.jsonFromLogLine(logLine).get());
					LOGGER.info("Sent " + logLine);
				} else {
					LOGGER.info("Bad line caught, skipped: " + logLine);
				}
			}
		} catch (InterruptedException | ClassCastException e) {
			LogTailerListener.logInterruptedException(e);
		}
	}

	private void postLine(final String logLine) {
		try {
			URL url = new URL("http://localhost:8080/logarchiver/save");
			HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
			urlConnection.setRequestMethod("POST");
			urlConnection.setDoOutput(true);
			urlConnection.setConnectTimeout(1000);
			urlConnection.setRequestProperty("Content-Type", APPLICATION_JSON_PROPERTY);
			urlConnection.setRequestProperty("Accept", APPLICATION_JSON_PROPERTY);
			String encode = Base64.encode("admin:admin_pwd".getBytes());
			urlConnection.setRequestProperty("Authorization", "Basic " + encode);
			OutputStreamWriter osw = new OutputStreamWriter(urlConnection.getOutputStream());
			osw.write(logLine);
			osw.flush();
			osw.close();
			LOGGER.info("Response: " + urlConnection.getResponseCode() + ", data: " + logLine);
		} catch (IOException ex) {
			LOGGER.error("Unreachable", ex);
		}
	}
}
