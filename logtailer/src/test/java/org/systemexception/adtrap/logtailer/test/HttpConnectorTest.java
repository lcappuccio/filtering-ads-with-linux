package org.systemexception.adtrap.logtailer.test;

import org.apache.commons.io.FileUtils;
import org.junit.Before;
import org.junit.Test;
import org.systemexception.adtrap.logtailer.services.HttpConnector;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.concurrent.LinkedBlockingQueue;

import static junit.framework.TestCase.assertTrue;

public class HttpConnectorTest {

	private HttpConnector sut;
	private static final LinkedBlockingQueue BLOCKING_QUEUE = new LinkedBlockingQueue();

	@Before
	public void setUp() {
		sut = new HttpConnector(BLOCKING_QUEUE);
		Thread threadHttpConnector = new Thread(sut);
		threadHttpConnector.start();
	}

	@Test
	public void should_take() throws InterruptedException, IOException {
		sut = new HttpConnector(BLOCKING_QUEUE);
		String outString = "Test" + System.currentTimeMillis();
		BLOCKING_QUEUE.put(outString);

		Thread.sleep(LogTailerListenerTest.THREAD_SLEEP);
		String logFileToString = FileUtils.readFileToString(LogTailerListenerTest.INFO_LOG_FILE,
				Charset.defaultCharset());

		assertTrue("Not logged " + outString, logFileToString.contains(outString));
	}

	@Test
	public void should_take_poison_pill() throws InterruptedException, IOException {
		sut = new HttpConnector(BLOCKING_QUEUE);
		BLOCKING_QUEUE.put(this);
		Thread.sleep(LogTailerListenerTest.THREAD_SLEEP);
		String logFileToString = FileUtils.readFileToString(LogTailerListenerTest.ERROR_LOG_FILE,
				Charset.defaultCharset());

		assertTrue(ClassCastException.class.getName() + " not logged",
				logFileToString.contains(ClassCastException.class.getName()));
	}

}