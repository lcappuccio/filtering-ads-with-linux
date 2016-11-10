package org.systemexception.adtrap.test;

import org.apache.commons.io.FileUtils;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.systemexception.adtrap.Application;
import org.systemexception.adtrap.pojo.logtailer.LogTailerBridge;
import org.systemexception.adtrap.pojo.LogQueue;
import org.systemexception.adtrap.service.DataService;

import java.io.IOException;
import java.nio.charset.Charset;

import static junit.framework.TestCase.assertTrue;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = {Application.class})
@WebAppConfiguration
@TestPropertySource(locations = "classpath:application.properties")
public class LogTailerBridgeTest {

	private LogTailerBridge sut;

	@Autowired
	private DataService dataService;

	@Autowired
	private LogQueue logQueue;

	@Before
	public void setUp() {
		sut = new LogTailerBridge(dataService, logQueue);
		Thread threadLogTailerBridge = new Thread(sut);
		threadLogTailerBridge.start();
	}

	@Test
	public void should_take() throws InterruptedException, IOException {
		sut = new LogTailerBridge(dataService, logQueue);
		String outString = LogParserTest.LOG_LINE;
		logQueue.put(outString);

		Thread.sleep(LogTailerListenerTest.THREAD_SLEEP);
		String logFileToString = FileUtils.readFileToString(LogTailerListenerTest.INFO_LOG_FILE,
				Charset.defaultCharset());

		assertTrue("Not logged " + outString, logFileToString.contains("e4478.a.akamaiedge.net"));
	}

	@Test
	public void should_take_poison_pill() throws InterruptedException, IOException {
		sut = new LogTailerBridge(dataService, logQueue);
		logQueue.put(this);
		Thread.sleep(LogTailerListenerTest.THREAD_SLEEP);
		String logFileToString = FileUtils.readFileToString(LogTailerListenerTest.INFO_LOG_FILE,
				Charset.defaultCharset());

		assertTrue(ClassCastException.class.getName() + " not logged",
				logFileToString.contains(ClassCastException.class.getName()));
	}

}