package org.systemexception.adtrap.statsviewer.test;

import org.junit.Before;
import org.junit.Test;
import org.systemexception.adtrap.statsviewer.model.DnsLogLine;
import org.systemexception.adtrap.statsviewer.repositories.DnsLogLineRepository;
import org.systemexception.adtrap.statsviewer.service.H2Service;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * @author leo
 * @date 07/10/2016 19:02
 */
public class H2ServiceTest {

	private H2Service sut;
	private DnsLogLineRepository dataRepository;
	private DnsLogLine dnsLogLine;
	private List<DnsLogLine> dnsLogLineList = new ArrayList<>();
	private static final Long LOG_ID = 1L;
	private static final String QUERY_TYPE = "query[A]", QUERY_DOMAIN = "somedomain.org", QUERY_TARGET = "127.0.0.1";

	@Before
	public void setUp() {
		Date nowDate = Date.from(Instant.now());
		dnsLogLine = new DnsLogLine();
		dnsLogLine.setLogId(LOG_ID);
		dnsLogLine.setDate(nowDate);
		dnsLogLine.setQueryType(QUERY_TYPE);
		dnsLogLine.setQueryDomain(QUERY_DOMAIN);
		dnsLogLine.setQueryTarget(QUERY_TARGET);
		dnsLogLineList.add(dnsLogLine);
		dataRepository = mock(DnsLogLineRepository.class);
		dnsLogLineList.add(dnsLogLine);
		when(dataRepository.save(dnsLogLine)).thenReturn(dnsLogLine);
		when(dataRepository.findOne(LOG_ID)).thenReturn(dnsLogLine);
		when(dataRepository.findByDate(nowDate)).thenReturn(dnsLogLineList);
		when(dataRepository.findByQueryType(QUERY_TYPE)).thenReturn(dnsLogLineList);
		when(dataRepository.findByQueryDomain(QUERY_DOMAIN)).thenReturn(dnsLogLineList);
		when(dataRepository.findByQueryTarget(QUERY_TARGET)).thenReturn(dnsLogLineList);
		sut = new H2Service(dataRepository);
	}

	@Test
	public void should_save_data() {
		DnsLogLine saved = sut.save(dnsLogLine);

		assertTrue(saved.equals(dnsLogLine));
		verify(dataRepository).save(dnsLogLine);
	}

	@Test
	public void should_find_by_id() {
		DnsLogLine one = sut.findOne(LOG_ID);

		assertTrue(one.equals(dnsLogLine));
		verify(dataRepository).findOne(LOG_ID);
	}
}