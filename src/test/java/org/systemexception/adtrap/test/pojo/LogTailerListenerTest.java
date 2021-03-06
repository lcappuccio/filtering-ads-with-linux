package org.systemexception.adtrap.test.pojo;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.systemexception.adtrap.Application;
import org.systemexception.adtrap.pojo.LogQueue;
import org.systemexception.adtrap.pojo.logtailer.LogTailer;
import org.systemexception.adtrap.pojo.logtailer.LogTailerListener;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.charset.Charset;

import static junit.framework.TestCase.assertFalse;
import static junit.framework.TestCase.assertTrue;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = {Application.class})
@WebAppConfiguration
@TestPropertySource(locations = "classpath:application.properties")
public class LogTailerListenerTest {

	private static File testLogFile, testLogFileRotate;
	private LogTailerListener logTailerListener;
	private LogTailer logTailer;
	private final static String TEST_LOG_FILE = "empty.log";
	private final static int SLEEP_TIMER = 100;

	public final static File INFO_LOG_FILE = new File("target/adtrap-test.log");
	public final static int THREAD_SLEEP = 2000;

	@Autowired
	private LogQueue logQueue;

	@BeforeClass
	public static void setLogTailerListenerTest() throws URISyntaxException, IOException {
		URL testLogFileUrl = ClassLoader.getSystemResource(TEST_LOG_FILE);
		testLogFile = new File(testLogFileUrl.toURI());
		if (testLogFile.exists()) {
			FileUtils.deleteQuietly(testLogFile);
		}
		String outString = "STARTING";
		write(testLogFile, outString);
		testLogFileRotate = new File(testLogFile.getAbsolutePath() + ".1");
		if (testLogFileRotate.exists()) {
			FileUtils.deleteQuietly(testLogFileRotate);
		}
	}

	@Before
	public void setUp() throws URISyntaxException {
		logTailerListener = new LogTailerListener(logQueue);
		logTailer = new LogTailer(testLogFile, logTailerListener, SLEEP_TIMER);

		Thread threadLogTailer = new Thread(logTailer);
		threadLogTailer.start();
	}

	@After
	public void tearDown() {
		logTailer.stop();
	}

	@Test
	public void should_listen_new_lines() throws InterruptedException, IOException {
		write(testLogFile, StringUtilsTest.LOG_LINE);
		Thread.sleep(THREAD_SLEEP);
		String logFileToString = FileUtils.readFileToString(INFO_LOG_FILE, Charset.defaultCharset());

		assertTrue("Not logged " + StringUtilsTest.LOG_LINE, logFileToString.contains("e4478.a.akamaiedge.net"));
	}

	@Test
	public void should_log_file_not_found() throws InterruptedException, IOException {
		String nonExistingLogFile = "idontexist.log";
		File testLogFile = new File(nonExistingLogFile);
		logTailerListener = new LogTailerListener(logQueue);
		logTailer = new LogTailer(testLogFile, logTailerListener, SLEEP_TIMER);
		Thread thread = new Thread(logTailer);
		thread.start();
		Thread.sleep(THREAD_SLEEP);
		String logFileToString = FileUtils.readFileToString(INFO_LOG_FILE, Charset.defaultCharset());

		assertTrue("FileNotFoundException not logged", logFileToString.contains(
				FileNotFoundException.class.getName()));
	}

	@Test
	public void should_log_file_rotate() throws InterruptedException, IOException {
		if (!System.getProperty("os.name").contains("Windows")) {
			String outString = StringUtilsTest.LOG_LINE;
			write(testLogFile, outString);
			Thread.sleep(THREAD_SLEEP);
			FileUtils.moveFile(testLogFile, testLogFileRotate);
			outString = StringUtilsTest.LOG_LINE;
			write(testLogFile, outString);
			Thread.sleep(THREAD_SLEEP);
			String logFileToString = FileUtils.readFileToString(INFO_LOG_FILE, Charset.defaultCharset());

			assertTrue("File rotation not logged", logFileToString.contains("File rotated"));
		}
	}

	@Test
	public void should_skip_old_file_content() throws URISyntaxException, IOException {
		URL testLogFileUrl = ClassLoader.getSystemResource("sample_dnsmasq.log");
		File fileWithData  = new File(testLogFileUrl.toURI());
		logTailer = new LogTailer(fileWithData, logTailerListener, SLEEP_TIMER);
		String logFileToString = FileUtils.readFileToString(INFO_LOG_FILE, Charset.defaultCharset());

		assertFalse("Loaded old content", logFileToString.contains("googleads.g.doubleclick.net"));
	}

	/**
	 * Append some lines to a file
	 */
	private static void write(File file, String... lines) throws IOException {
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