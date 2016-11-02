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

	private static final String SAMPLE_LOG_LINE = "Oct  5 07:29:15 dnsmasq[14261]: config googleads.g.doubleclick" +
			".net is 127.0.0.1";
	private final JsonMapper sut = new JsonMapper();

	@Test
	public void should_convert_to_json() throws ParseException {
		String jsonFromLogLine = sut.jsonFromLogLine(SAMPLE_LOG_LINE);

		assertTrue(jsonFromLogLine.contains("\"queryType\":\"config\"," +
				"\"queryDomain\":\"googleads.g.doubleclick.net\",\"queryTarget\":\"127.0.0.1\"}"));
	}

}