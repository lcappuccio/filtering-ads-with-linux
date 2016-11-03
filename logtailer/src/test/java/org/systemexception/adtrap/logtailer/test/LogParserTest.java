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

	public static final String LOG_LINE = "Nov  2 20:42:55 dnsmasq[27711]: forwarded e4478.a.akamaiedge.net to " +
			"8.8.4.4";
	public static final String DHCP_LOG_LINE = "Nov  3 11:35:19 dnsmasq-dhcp[27711]: 469193315 DHCPREQUEST(eth0) " +
			"192.168.0.168 e8:b2:ac:1a:79:af";
	public static final String DHCPACK_LOG_LINE = "Nov  3 06:35:46 dnsmasq-dhcp[27711]: 1306210624 DHCPACK(eth0) " +
			"192.168.0.208 b0:e8:92:6f:a2:df SomeServer";
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

	@Test
	public void should_split_dhcp_log_line() {
		ArrayList<String> splitLog = sut.splitLogLine(DHCP_LOG_LINE);

		assertEquals("Nov", splitLog.get(0));
		assertEquals("3", splitLog.get(1));
		assertEquals("11:35:19", splitLog.get(2));
		assertEquals("dnsmasq-dhcp[27711]:", splitLog.get(3));
		assertEquals("469193315", splitLog.get(4));
		assertEquals("DHCPREQUEST(eth0)", splitLog.get(5));
		assertEquals("192.168.0.168", splitLog.get(6));
		assertEquals("e8:b2:ac:1a:79:af", splitLog.get(7));
	}

	@Test
	public void should_split_dhcpack_log_line() {
		ArrayList<String> splitLog = sut.splitLogLine(DHCPACK_LOG_LINE);

		assertEquals("Nov", splitLog.get(0));
		assertEquals("3", splitLog.get(1));
		assertEquals("06:35:46", splitLog.get(2));
		assertEquals("dnsmasq-dhcp[27711]:", splitLog.get(3));
		assertEquals("1306210624", splitLog.get(4));
		assertEquals("DHCPACK(eth0)", splitLog.get(5));
		assertEquals("192.168.0.208", splitLog.get(6));
		assertEquals("b0:e8:92:6f:a2:df", splitLog.get(7));
	}
}