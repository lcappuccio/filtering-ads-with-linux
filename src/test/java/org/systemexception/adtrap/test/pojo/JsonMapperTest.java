package org.systemexception.adtrap.test.pojo;

import org.junit.jupiter.api.Test;
import org.systemexception.adtrap.model.DnsLogLine;
import org.systemexception.adtrap.pojo.JsonMapper;
import org.systemexception.adtrap.pojo.StringUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;


/**
 * @author leo
 * @date 02/11/2016 19:22
 */
class JsonMapperTest {

	private final JsonMapper sut = new JsonMapper();

	@Test
	void should_convert_to_json() throws ParseException {
		DnsLogLine dnsLogLine = sut.dnsLogLineFromLogLine(StringUtilsTest.timeToDate() +
				StringUtils.LOG_LINE_SEPARATOR + StringUtilsTest.LOG_LINE).get();

		assertEquals("forwarded", dnsLogLine.getQueryType());
		assertEquals("e4478.a.akamaiedge.net", dnsLogLine.getQueryDomain());
		assertEquals("8.8.4.4", dnsLogLine.getQueryTarget());
	}

	@Test
	void should_convert_dhcp_to_json() throws ParseException {
		assertFalse(sut.dnsLogLineFromLogLine(StringUtilsTest.timeToDate() + StringUtils.LOG_LINE_SEPARATOR +
				StringUtilsTest.DHCP_LOG_LINE).isPresent());
	}

	@Test
	void should_convert_dhcpack_to_json() throws ParseException {
		DnsLogLine dnsLogLine = sut.dnsLogLineFromLogLine(StringUtilsTest.timeToDate() +
				StringUtils.LOG_LINE_SEPARATOR + StringUtilsTest.DHCPACK_LOG_LINE).get();

		assertEquals("DHCPACK(eth0)", dnsLogLine.getQueryType());
		assertEquals("34:12:98:77:5e:b3", dnsLogLine.getQueryDomain());
		assertEquals("192.168.0.214", dnsLogLine.getQueryTarget());
	}

	@Test
	void should_skip_bad_line() throws ParseException {
		assertFalse(sut.dnsLogLineFromLogLine(StringUtilsTest.BAD_LOG_LINE).isPresent());
	}

	@Test
	void should_skip_other_bad_line() throws ParseException {
		assertFalse(sut.dnsLogLineFromLogLine(StringUtilsTest.BAD_LOG_LINE_A).isPresent());
	}

	@Test
	void should_skip_old_records() throws ParseException {
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