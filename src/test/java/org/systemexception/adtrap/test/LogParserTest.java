package org.systemexception.adtrap.test;

import org.junit.Test;
import org.systemexception.adtrap.pojo.LogParser;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

import static org.junit.Assert.assertEquals;

/**
 * @author leo
 * @date 02/11/2016 20:47
 */
public class LogParserTest {

	public static final String LOG_LINE = "dnsmasq[27711]: forwarded e4478.a.akamaiedge.net to 8.8.4.4";
	public static final String DHCP_LOG_LINE = "dnsmasq-dhcp[1643]: DHCPREQUEST(eth0) 192.168.0.214 34:12:98:77:5e:b3";
	public static final String DHCPACK_LOG_LINE = "dnsmasq-dhcp[1643]: DHCPACK(eth0) 192.168.0.214 " +
			"34:12:98:77:5e:b3 SomeServer";
	public static final String BAD_LOG_LINE = "dnsmasq[20998]: compile time options: IPv6 GNU-getopt " +
			"DBus i18n IDN DHCP DHCPv6 no-Lua TFTP conntrack ipset auth DNSSEC loop-detect";
	public static final String BAD_LOG_LINE_A = "dnsmasq[32597]: using nameserver 8.8.4.4#53";
	private static final String QUERY_TYPE = "forwarded";
	private static final String QUERY_DOMAIN = "e4478.a.akamaiedge.net";
	private static final String QUERY_TARGET = "8.8.4.4";

	@Test
	public void should_split_log_line() {
		ArrayList<String> splitLog = LogParser.splitLogLine(timeToDate() + LogParser.LOG_LINE_SEPARATOR + LOG_LINE);

		assertEquals("dnsmasq[27711]:", splitLog.get(3));
		assertEquals(QUERY_TYPE, splitLog.get(4));
		assertEquals(QUERY_DOMAIN, splitLog.get(5));
		assertEquals("to", splitLog.get(6));
		assertEquals(QUERY_TARGET, splitLog.get(7));
	}

	@Test
	public void should_split_dhcp_log_line() {
		ArrayList<String> splitLog = LogParser.splitLogLine(timeToDate() + LogParser.LOG_LINE_SEPARATOR + DHCP_LOG_LINE);

		assertEquals("dnsmasq-dhcp[1643]:", splitLog.get(3));
		assertEquals("DHCPREQUEST(eth0)", splitLog.get(4));
		assertEquals("192.168.0.214", splitLog.get(5));
		assertEquals("34:12:98:77:5e:b3", splitLog.get(6));
	}

	@Test
	public void should_split_dhcpack_log_line() {
		ArrayList<String> splitLog = LogParser.splitLogLine(timeToDate() + LogParser.LOG_LINE_SEPARATOR + DHCPACK_LOG_LINE);

		assertEquals("dnsmasq-dhcp[1643]:", splitLog.get(3));
		assertEquals("DHCPACK(eth0)", splitLog.get(4));
		assertEquals("192.168.0.214", splitLog.get(5));
		assertEquals("34:12:98:77:5e:b3", splitLog.get(6));
	}

	public static String timeToDate() {
		Calendar cal = Calendar.getInstance();
		SimpleDateFormat dateParser = new SimpleDateFormat("MMM d HH:mm:ss", Locale.getDefault());
		return dateParser.format(cal.getTime());
	}
}