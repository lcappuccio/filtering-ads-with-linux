package org.systemexception.adtrap.logtailer.test;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.systemexception.adtrap.logtailer.services.LogTailer;
import org.systemexception.adtrap.logtailer.services.LogTailerListener;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.List;

import static junit.framework.TestCase.assertTrue;

public class LogTailerListenerTest {

	private final URL testLogFileUrl = ClassLoader.getSystemResource(TEST_LOG_FILE);
	private File testLogFile;
	private LogTailerListener logTailerListener;
	private LogTailer logTailer;
	private final static String TEST_LOG_FILE = "empty.log";
	private final static int SLEEP_TIMER = 100, THREAD_SLEEP = 500;

	@Before
	public void setUp() throws URISyntaxException {
		testLogFile = new File(testLogFileUrl.toURI());
		logTailerListener = new LogTailerListener(SLEEP_TIMER);
		logTailer = new LogTailer(testLogFile, logTailerListener, SLEEP_TIMER);
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
		Thread.sleep(THREAD_SLEEP);
		List<String> listenerLines = logTailerListener.getLines();
		logTailerListener.clearLines();

		assertTrue(listenerLines.contains(outString));
	}

	@Test
	public void should_log_file_not_found() throws InterruptedException, IOException {
		String nonExistingLogFile = "idontexist.log";
		File testLogFile = new File(nonExistingLogFile);
		logTailerListener = new LogTailerListener(SLEEP_TIMER);
		logTailer = new LogTailer(testLogFile, logTailerListener, SLEEP_TIMER);
		Thread thread = new Thread(logTailer);
		thread.start();
		Thread.sleep(THREAD_SLEEP);
		String logFileToString = FileUtils.readFileToString(new File("target/error.log"), Charset.defaultCharset());

		assertTrue("FileNotFoundException not in log", logFileToString.contains(FileNotFoundException.class.getName()));
	}

	@Test
	public void should_log_file_rotate() throws InterruptedException, IOException {
		String outString = "TEST" + System.currentTimeMillis();
		write(testLogFile, outString);
		Thread.sleep(THREAD_SLEEP);
		FileUtils.moveFile(testLogFile, new File(testLogFile.getAbsolutePath() + ".1"));
		outString = "TEST" + System.currentTimeMillis();
		write(testLogFile, outString);
		Thread.sleep(THREAD_SLEEP);
		String logFileToString = FileUtils.readFileToString(new File("target/info.log"), Charset.defaultCharset());

		assertTrue("File rotation not logged", logFileToString.contains("File rotated"));
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