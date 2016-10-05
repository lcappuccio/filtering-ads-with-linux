package org.systemexception.adtrap.logtailer.test;

import org.apache.commons.io.IOUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.systemexception.adtrap.logtailer.services.LogTailer;
import org.systemexception.adtrap.logtailer.services.LogTailerListener;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.List;

import static junit.framework.TestCase.assertTrue;

public class LogTailerListenerTest {

	private final static String TEST_LOG_FILE = "empty.log";
	private final URL testLogFileUrl = ClassLoader.getSystemResource(TEST_LOG_FILE);
	private File testLogFile;
	private LogTailerListener logTailerListener;
	private LogTailer logTailer;

	@Before
	public void setUp() throws URISyntaxException {
		testLogFile = new File(testLogFileUrl.toURI());
		logTailerListener = new LogTailerListener(100);
		logTailer = new LogTailer(testLogFile, logTailerListener, 100);
		Thread thread = new Thread(logTailer);
		thread.start();
	}

	@After
	public void tearDown() {
		logTailer.stop();
	}

	@Test
	public void should_listen_new_lines() throws InterruptedException, IOException {
		String outString = "TEST" + System.currentTimeMillis();
		write(testLogFile, outString);
		Thread.sleep(500);
		List<String> listenerLines = logTailerListener.getLines();
		logTailerListener.clearLines();

		assertTrue(listenerLines.contains(outString));
	}

	/**
	 * Append some lines to a file
	 */
	private void write(File file, String... lines) throws IOException {
		FileWriter writer = null;
		try {
			writer = new FileWriter(file, true);
			for (String line : lines) {
				writer.write(line + System.getProperty("line.separator"));
			}
		} finally {
			IOUtils.closeQuietly(writer);
		}
	}

}