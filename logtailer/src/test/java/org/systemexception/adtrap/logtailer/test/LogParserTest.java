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
	public static final String DHCP_LOG_LINE = "Nov  3 19:30:57 dnsmasq-dhcp[1643]: DHCPREQUEST(eth0) 192.168.0.214 " +
			"34:12:98:77:5e:b3";
	public static final String DHCPACK_LOG_LINE = "Nov  3 19:30:57 dnsmasq-dhcp[1643]: DHCPACK(eth0) 192.168.0.214 " +
			"34:12:98:77:5e:b3 SomeServer";
	public static final String BAD_LOG_LINE = "Nov  3 20:11:15 dnsmasq[20998]: compile time options: IPv6 GNU-getopt " +
			"DBus i18n IDN DHCP DHCPv6 no-Lua TFTP conntrack ipset auth DNSSEC loop-detect";
	public static final String BAD_LOG_LINE_A = "Nov  4 18:47:39 dnsmasq[32597]: using nameserver 8.8.4.4#53";
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
		assertEquals("19:30:57", splitLog.get(2));
		assertEquals("dnsmasq-dhcp[1643]:", splitLog.get(3));
		assertEquals("DHCPREQUEST(eth0)", splitLog.get(4));
		assertEquals("192.168.0.214", splitLog.get(5));
		assertEquals("34:12:98:77:5e:b3", splitLog.get(6));
	}

	@Test
	public void should_split_dhcpack_log_line() {
		ArrayList<String> splitLog = sut.splitLogLine(DHCPACK_LOG_LINE);

		assertEquals("Nov", splitLog.get(0));
		assertEquals("3", splitLog.get(1));
		assertEquals("19:30:57", splitLog.get(2));
		assertEquals("dnsmasq-dhcp[1643]:", splitLog.get(3));
		assertEquals("DHCPACK(eth0)", splitLog.get(4));
		assertEquals("192.168.0.214", splitLog.get(5));
		assertEquals("34:12:98:77:5e:b3", splitLog.get(6));
	}
}