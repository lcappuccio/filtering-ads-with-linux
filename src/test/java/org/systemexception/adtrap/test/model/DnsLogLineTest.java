package org.systemexception.adtrap.test.model;

import org.junit.jupiter.api.Test;
import org.systemexception.adtrap.model.DnsLogLine;
import org.systemexception.adtrap.pojo.StringUtils;

import static org.junit.jupiter.api.Assertions.assertEquals;


/**
 * @author leo
 * @date 11/11/2016 01:10
 */
class DnsLogLineTest {

	@Test
	void should_respect_string_format() {

		long creationTime = System.currentTimeMillis();
		DnsLogLine sut = new DnsLogLine();
		sut.setDate(creationTime);
		sut.setQueryDomain("www.test.it");
		sut.setQueryType("query[A]");
		sut.setQueryTarget("8.8.8.8");

		assertEquals("query[A]" + StringUtils.LOG_LINE_SEPARATOR + "www.test.it" +
				StringUtils.LOG_LINE_SEPARATOR + "8.8.8.8",sut.toString());

	}
}