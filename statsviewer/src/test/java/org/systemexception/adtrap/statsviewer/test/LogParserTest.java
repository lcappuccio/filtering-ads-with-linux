package org.systemexception.adtrap.statsviewer.test;

import org.junit.Test;
import org.systemexception.adtrap.statsviewer.pojo.LogParser;

import java.util.ArrayList;

import static org.junit.Assert.assertEquals;

/**
 * @author leo
 * @date 02/11/2016 20:47
 */
public class LogParserTest {

	private static final String LOG_LINE = "Nov  2 20:42:55 dnsmasq[27711]: forwarded e4478.a.akamaiedge.net to " +
			"8.8.4.4";

	@Test
	public void should_split_log_line() {
		ArrayList<String> splitLog = LogParser.splitLogLine(LOG_LINE);

		assertEquals("Nov", splitLog.get(0));
		assertEquals("2", splitLog.get(1));
		assertEquals("20:42:55", splitLog.get(2));
		assertEquals("dnsmasq[27711]:", splitLog.get(3));
		assertEquals("forwarded", splitLog.get(4));
		assertEquals("e4478.a.akamaiedge.net", splitLog.get(5));
		assertEquals("to", splitLog.get(6));
		assertEquals("8.8.4.4", splitLog.get(7));
	}
}