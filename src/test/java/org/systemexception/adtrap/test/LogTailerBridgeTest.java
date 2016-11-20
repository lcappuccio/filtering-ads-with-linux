package org.systemexception.adtrap.test;

import org.apache.commons.io.FileUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.systemexception.adtrap.Application;
import org.systemexception.adtrap.pojo.LogParser;
import org.systemexception.adtrap.pojo.LogQueue;

import java.io.IOException;
import java.nio.charset.Charset;

import static junit.framework.TestCase.assertTrue;
import static org.junit.Assert.assertEquals;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = {Application.class})
@WebAppConfiguration
@TestPropertySource(locations = "classpath:application.properties")
public class LogTailerBridgeTest {

	@Autowired
	private LogQueue logQueue;

	@Test
	public void should_take() throws InterruptedException, IOException {
		String outString = LogParserTest.timeToDate() + LogParser.LOG_LINE_SEPARATOR + LogParserTest.LOG_LINE;
		logQueue.put(outString);

		Thread.sleep(LogTailerListenerTest.THREAD_SLEEP);
		String logFileToString = FileUtils.readFileToString(LogTailerListenerTest.INFO_LOG_FILE,
				Charset.defaultCharset());

		assertTrue("Not logged " + outString, logFileToString.contains("Received: forwarded e4478.a.akamaiedge.net"));
		assertEquals(0, logQueue.size());
	}

	@Test
	public void should_take_poison_pill() throws InterruptedException, IOException {
		logQueue.put(this);
		Thread.sleep(LogTailerListenerTest.THREAD_SLEEP);
		String logFileToString = FileUtils.readFileToString(LogTailerListenerTest.INFO_LOG_FILE,
				Charset.defaultCharset());

		assertTrue(ClassCastException.class.getName() + " not logged",
				logFileToString.contains(ClassCastException.class.getName()));
		assertEquals(0, logQueue.size());
	}

	@Test
	public void should_ignore_domains() throws InterruptedException, IOException {
		String logLineToIgnore = LogParserTest.timeToDate() + LogParser.LOG_LINE_SEPARATOR +
				"dnsmasq[26446]: query[A] www.ignore1.com from 192.168.0.1";
		logQueue.put(logLineToIgnore);

		Thread.sleep(LogTailerListenerTest.THREAD_SLEEP);
		String logFileToString = FileUtils.readFileToString(LogTailerListenerTest.INFO_LOG_FILE,
				Charset.defaultCharset());

		assertTrue("Not logged " + logLineToIgnore, logFileToString.contains("Ignored domain: www.ignore1.com"));
		assertEquals(0, logQueue.size());
	}

}