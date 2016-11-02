package org.systemexception.adtrap.logtailer.test;

import org.junit.Test;
import org.systemexception.adtrap.logtailer.services.JsonMapper;

import static junit.framework.TestCase.assertTrue;

/**
 * @author leo
 * @date 02/11/2016 19:22
 */
public class JsonMapperTest {

	private static final String SAMPLE_LOG_LINE = "Oct  5 07:29:15 dnsmasq[14261]: config googleads.g.doubleclick" +
			".net" +
			" is 127.0.0.1";

	@Test
	public void should_convert_to_json() {
		String jsonFromLogLine = JsonMapper.jsonFromLogLine(SAMPLE_LOG_LINE);

		assertTrue("{\"logLine\":\"Oct  5 07:29:15 dnsmasq[14261]: config googleads.g.doubleclick.net is 127.0.0.1\"}"
				.equals(jsonFromLogLine));
	}

	@Test
	public void should_keep_api_with_statsviewer() {
		String jsonFromLogLine = JsonMapper.jsonFromLogLine(SAMPLE_LOG_LINE);

		assertTrue(jsonFromLogLine.contains(JsonMapper.JSON_PROPERTY_LOG_LINE));

	}

}