package org.systemexception.adtrap.logtailer.test;

import org.junit.Test;
import org.systemexception.adtrap.logtailer.services.LogParser;

import java.util.ArrayList;

import static org.junit.Assert.assertEquals;

/**
 * @author leo
 * @date 02/11/2016 20:47
 */
public class LogParserTest {

	private static final String LOG_LINE = "Nov  2 20:42:55 dnsmasq[27711]: forwarded e4478.a.akamaiedge.net to " +
			"8.8.4.4";
	private static final String QUERY_TYPE = "forwarded";
	private static final String QUERY_DOMAIN = "e4478.a.akamaiedge.net";
	private static final String QUERY_TARGET = "8.8.4.4";
	private final LogParser sut = new LogParser();

	@Test
	public void should_split_log_line() {
		ArrayList<String> splitLog = sut.splitLogLine(LOG_LINE);

		assertEquals("Nov", splitLog.get(0));
		assertEquals("2", splitLog.get(1));
		assertEquals("20:42:55", splitLog.get(2));
		assertEquals("dnsmasq[27711]:", splitLog.get(3));
		assertEquals(QUERY_TYPE, splitLog.get(4));
		assertEquals(QUERY_DOMAIN, splitLog.get(5));
		assertEquals("to", splitLog.get(6));
		assertEquals(QUERY_TARGET, splitLog.get(7));
	}
}