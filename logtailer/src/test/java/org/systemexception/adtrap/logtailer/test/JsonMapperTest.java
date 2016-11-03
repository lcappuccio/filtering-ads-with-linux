package org.systemexception.adtrap.logtailer.test;

import org.junit.Test;
import org.systemexception.adtrap.logtailer.services.JsonMapper;

import java.text.ParseException;

import static junit.framework.TestCase.assertTrue;

/**
 * @author leo
 * @date 02/11/2016 19:22
 */
public class JsonMapperTest {

	private final JsonMapper sut = new JsonMapper();

	@Test
	public void should_convert_to_json() throws ParseException {
		String jsonFromLogLine = sut.jsonFromLogLine(LogParserTest.LOG_LINE);

		assertTrue(jsonFromLogLine.contains("\"queryType\":\"forwarded\"," +
				"\"queryDomain\":\"e4478.a.akamaiedge.net\",\"queryTarget\":\"8.8.4.4\"}"));
	}

}