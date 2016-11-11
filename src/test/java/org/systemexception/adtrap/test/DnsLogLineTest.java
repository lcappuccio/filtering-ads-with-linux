package org.systemexception.adtrap.test;

import org.junit.Test;
import org.systemexception.adtrap.model.DnsLogLine;
import org.systemexception.adtrap.pojo.LogParser;

import static org.junit.Assert.assertEquals;

/**
 * @author leo
 * @date 11/11/2016 01:10
 */
public class DnsLogLineTest {

	private DnsLogLine sut;

	@Test
	public void should_respect_string_format() {

		Long creationTime = System.currentTimeMillis();

		sut = new DnsLogLine();
		sut.setDate(creationTime);
		sut.setQueryDomain("www.test.it");
		sut.setQueryType("query[A]");
		sut.setQueryTarget("8.8.8.8");

		assertEquals("query[A]" + LogParser.LOG_LINE_SEPARATOR + "www.test.it" +
				LogParser.LOG_LINE_SEPARATOR + "8.8.8.8",sut.toString());

	}

}