package org.systemexception.adtrap.logtailer.test;

import org.junit.Test;
import org.systemexception.adtrap.logtailer.services.JsonMapper;
import org.systemexception.adtrap.logtailer.services.LogParser;

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
		String jsonFromLogLine = sut.jsonFromLogLine(LogParserTest.timeToDate() + LogParser.LOG_LINE_SEPARATOR +
				LogParserTest.LOG_LINE).get();

		assertTrue(jsonFromLogLine.contains("\"queryType\":\"forwarded\"," +
				"\"queryDomain\":\"e4478.a.akamaiedge.net\",\"queryTarget\":\"8.8.4.4\"}"));
	}

	@Test
	public void should_convert_dhcp_to_json() throws ParseException {
		assertFalse(sut.jsonFromLogLine(LogParserTest.timeToDate() + LogParser.LOG_LINE_SEPARATOR +
				LogParserTest.DHCP_LOG_LINE).isPresent());
	}

	@Test
	public void should_convert_dhcpack_to_json() throws ParseException {
		String jsonFromLogLine = sut.jsonFromLogLine(LogParserTest.timeToDate() + LogParser.LOG_LINE_SEPARATOR +
				LogParserTest.DHCPACK_LOG_LINE).get();

		assertTrue(jsonFromLogLine.contains("\"queryType\":\"DHCPACK(eth0)\"," +
				"\"queryDomain\":\"34:12:98:77:5e:b3\",\"queryTarget\":\"192.168.0.214\"}"));
	}

	@Test
	public void should_skip_bad_line() throws ParseException {
		assertFalse(sut.jsonFromLogLine(LogParserTest.BAD_LOG_LINE).isPresent());
	}

	@Test
	public void should_skip_other_bad_line() throws ParseException {
		assertFalse(sut.jsonFromLogLine(LogParserTest.BAD_LOG_LINE_A).isPresent());
	}

	@Test
	public void should_skip_old_records() throws ParseException {
		assertFalse(sut.jsonFromLogLine(timeToPreviousDate() + LogParser.LOG_LINE_SEPARATOR + LogParserTest.LOG_LINE)
		.isPresent());
	}

	private String timeToPreviousDate() {
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.MINUTE, -10);
		SimpleDateFormat dateParser = new SimpleDateFormat("MMM d HH:mm:ss", Locale.getDefault());
		return dateParser.format(cal.getTime());
	}

}