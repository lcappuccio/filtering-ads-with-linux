package org.systemexception.adtrap.test.pojo;

import org.junit.Test;
import org.systemexception.adtrap.model.DnsLogLine;
import org.systemexception.adtrap.pojo.JsonMapper;
import org.systemexception.adtrap.pojo.StringUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import static junit.framework.TestCase.assertFalse;
import static junit.framework.TestCase.assertTrue;

/**
 * @author leo
 * @date 02/11/2016 19:22
 */
public class JsonMapperTest {

	private final JsonMapper sut = new JsonMapper();

	@Test
	public void should_convert_to_json() throws ParseException {
		DnsLogLine dnsLogLine = sut.dnsLogLineFromLogLine(StringUtilsTest.timeToDate() +
				StringUtils.LOG_LINE_SEPARATOR + StringUtilsTest.LOG_LINE).get();

		assertTrue("forwarded".equals(dnsLogLine.getQueryType()));
		assertTrue("e4478.a.akamaiedge.net".equals(dnsLogLine.getQueryDomain()));
		assertTrue("8.8.4.4".equals(dnsLogLine.getQueryTarget()));
	}

	@Test
	public void should_convert_dhcp_to_json() throws ParseException {
		assertFalse(sut.dnsLogLineFromLogLine(StringUtilsTest.timeToDate() + StringUtils.LOG_LINE_SEPARATOR +
				StringUtilsTest.DHCP_LOG_LINE).isPresent());
	}

	@Test
	public void should_convert_dhcpack_to_json() throws ParseException {
		DnsLogLine dnsLogLine = sut.dnsLogLineFromLogLine(StringUtilsTest.timeToDate() +
				StringUtils.LOG_LINE_SEPARATOR + StringUtilsTest.DHCPACK_LOG_LINE).get();

		assertTrue("DHCPACK(eth0)".equals(dnsLogLine.getQueryType()));
		assertTrue("34:12:98:77:5e:b3".equals(dnsLogLine.getQueryDomain()));
		assertTrue("192.168.0.214".equals(dnsLogLine.getQueryTarget()));
	}

	@Test
	public void should_skip_bad_line() throws ParseException {
		assertFalse(sut.dnsLogLineFromLogLine(StringUtilsTest.BAD_LOG_LINE).isPresent());
	}

	@Test
	public void should_skip_other_bad_line() throws ParseException {
		assertFalse(sut.dnsLogLineFromLogLine(StringUtilsTest.BAD_LOG_LINE_A).isPresent());
	}

	@Test
	public void should_skip_old_records() throws ParseException {
		assertFalse(sut.dnsLogLineFromLogLine(timeToPreviousDate() + StringUtils.LOG_LINE_SEPARATOR +
				StringUtilsTest.LOG_LINE).isPresent());
	}

	private String timeToPreviousDate() {
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.MINUTE, -10);
		SimpleDateFormat dateParser = new SimpleDateFormat("MMM d HH:mm:ss", Locale.getDefault());
		return dateParser.format(cal.getTime());
	}

}