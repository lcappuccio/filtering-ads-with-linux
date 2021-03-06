package org.systemexception.adtrap.test.service;

import org.apache.commons.io.FileUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.systemexception.adtrap.Application;
import org.systemexception.adtrap.pojo.StringUtils;
import org.systemexception.adtrap.pojo.LogQueue;
import org.systemexception.adtrap.service.DataService;
import org.systemexception.adtrap.test.pojo.LogTailerListenerTest;
import org.systemexception.adtrap.test.pojo.StringUtilsTest;

import java.io.IOException;
import java.nio.charset.Charset;

import static junit.framework.TestCase.assertTrue;
import static org.junit.Assert.assertEquals;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = {Application.class})
@WebAppConfiguration
@TestPropertySource(locations = "classpath:application.properties")
public class LogTailerBridgeTest {

	private static final String TEST_IGNORE_DOMAIN = "TEST_IGNORE_DOMAIN";
	@Autowired
	private LogQueue logQueue;
	@Autowired
	private DataService dataService;


	@Before
	public void setUp() {
		dataService.addIgnoredDomain(TEST_IGNORE_DOMAIN);
	}

	@After
	public void tearDown() {
		dataService.removeIgnoredDomain(TEST_IGNORE_DOMAIN);
	}

	@Test
	public void should_take() throws InterruptedException, IOException {
		String outString = StringUtilsTest.timeToDate() + StringUtils.LOG_LINE_SEPARATOR + StringUtilsTest.LOG_LINE;
		logQueue.put(outString);

		Thread.sleep(LogTailerListenerTest.THREAD_SLEEP);
		String logFileToString = FileUtils.readFileToString(LogTailerListenerTest.INFO_LOG_FILE,
				Charset.defaultCharset());

		assertTrue("Not logged " + outString,
				logFileToString.contains("Received: forwarded e4478.a.akamaiedge.net"));
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
		String logLineToIgnore = StringUtilsTest.timeToDate() + StringUtils.LOG_LINE_SEPARATOR +
				"dnsmasq[26446]: query[A] TEST_IGNORE_DOMAIN from 192.168.0.1";
		logQueue.put(logLineToIgnore);

		Thread.sleep(LogTailerListenerTest.THREAD_SLEEP);
		String logFileToString = FileUtils.readFileToString(LogTailerListenerTest.INFO_LOG_FILE,
				Charset.defaultCharset());

		assertTrue("Not logged " + logLineToIgnore,
				logFileToString.contains("Ignored domain: TEST_IGNORE_DOMAIN"));
		assertEquals(0, logQueue.size());
	}

}