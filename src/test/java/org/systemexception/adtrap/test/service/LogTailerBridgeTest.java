package org.systemexception.adtrap.test.service;

import org.apache.commons.io.FileUtils;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.web.WebAppConfiguration;
import org.systemexception.adtrap.Application;
import org.systemexception.adtrap.pojo.LogQueue;
import org.systemexception.adtrap.pojo.StringUtils;
import org.systemexception.adtrap.service.DataService;
import org.systemexception.adtrap.test.pojo.LogTailerListenerTest;
import org.systemexception.adtrap.test.pojo.StringUtilsTest;

import java.io.IOException;
import java.nio.charset.Charset;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.util.AssertionErrors.assertTrue;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = {Application.class})
@WebAppConfiguration
@TestPropertySource(locations = "classpath:application-test.properties")
@DirtiesContext
class LogTailerBridgeTest {

	private static final String TEST_IGNORE_DOMAIN = "TEST_IGNORE_DOMAIN";
	@Autowired
	private LogQueue logQueue;
	@Autowired
	private DataService dataService;

	@BeforeEach
	void setUp() {
		dataService.addIgnoredDomain(TEST_IGNORE_DOMAIN);
	}

	@AfterEach
	void tearDown() {
		dataService.removeIgnoredDomain(TEST_IGNORE_DOMAIN);
	}

	@Test
	void should_take() throws InterruptedException, IOException {
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
	void should_take_poison_pill() throws InterruptedException, IOException {
		logQueue.put(this);
		Thread.sleep(LogTailerListenerTest.THREAD_SLEEP);
		String logFileToString = FileUtils.readFileToString(LogTailerListenerTest.INFO_LOG_FILE,
				Charset.defaultCharset());

		assertTrue(ClassCastException.class.getName() + " not logged",
				logFileToString.contains(ClassCastException.class.getName()));
		assertEquals(0, logQueue.size());
	}

	@Test
	void should_ignore_domains() throws InterruptedException, IOException {
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