package org.systemexception.adtrap.logtailer.services;

/**
 * @author leo
 * @date 05/10/2016 22:57
 */
public class HttpConnector implements Runnable {

	private final String logLine;

	public HttpConnector(final String logLine) {
		this.logLine = logLine;
	}

	@Override
	public void run() {
		try {
			Thread.sleep(5000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		System.out.println(logLine);
	}
}
