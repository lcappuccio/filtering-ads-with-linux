package org.systemexception.adtrap.logarchiver.test;

import org.junit.Before;
import org.junit.Test;
import org.systemexception.adtrap.logarchiver.model.DnsLogLine;
import org.systemexception.adtrap.logarchiver.repositories.DnsLogLineRepository;
import org.systemexception.adtrap.logarchiver.service.H2Service;

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
	private static final Date NOW_DATE = Date.from(Instant.now());

	@Before
	public void setUp() {
		dnsLogLine = new DnsLogLine();
		long date = System.currentTimeMillis();
		dnsLogLine.setLogId(LOG_ID);
		dnsLogLine.setDate(date);
		dnsLogLine.setQueryType(QUERY_TYPE);
		dnsLogLine.setQueryDomain(QUERY_DOMAIN);
		dnsLogLine.setQueryTarget(QUERY_TARGET);
		dnsLogLineList.add(dnsLogLine);
		dataRepository = mock(DnsLogLineRepository.class);
		dnsLogLineList.add(dnsLogLine);
		when(dataRepository.save(dnsLogLine)).thenReturn(dnsLogLine);
		when(dataRepository.findOne(LOG_ID)).thenReturn(dnsLogLine);
		when(dataRepository.findByDate(NOW_DATE)).thenReturn(dnsLogLineList);
		when(dataRepository.findByQueryType(QUERY_TYPE)).thenReturn(dnsLogLineList);
		when(dataRepository.findByQueryDomain(QUERY_DOMAIN)).thenReturn(dnsLogLineList);
		when(dataRepository.findByQueryTarget(QUERY_TARGET)).thenReturn(dnsLogLineList);
	}

	@Test
	public void should_save_data() {
		sut = new H2Service(dataRepository);
		DnsLogLine saved = sut.save(dnsLogLine);

		assertTrue(saved.equals(dnsLogLine));
		verify(dataRepository).save(dnsLogLine);
	}

	@Test
	public void should_find_by_id() {
		sut = new H2Service(dataRepository);
		DnsLogLine one = sut.findOne(LOG_ID);

		assertTrue(one.equals(dnsLogLine));
		verify(dataRepository).findOne(LOG_ID);
	}

	@Test
	public void should_find_by_date() {
		sut = new H2Service(dataRepository);
		List<DnsLogLine> byDate = sut.findByDate(NOW_DATE);

		assertTrue(byDate.equals(dnsLogLineList));
		verify(dataRepository).findByDate(NOW_DATE);
	}

	@Test
	public void should_find_by_query_type() {
		sut = new H2Service(dataRepository);
		List<DnsLogLine> byQueryType = sut.findByQueryType(QUERY_TYPE);

		assertTrue(byQueryType.equals(dnsLogLineList));
		verify(dataRepository).findByQueryType(QUERY_TYPE);
	}

	@Test
	public void should_find_by_query_domain() {
		sut = new H2Service(dataRepository);
		List<DnsLogLine> byQueryDomain = sut.findByQueryDomain(QUERY_DOMAIN);

		assertTrue(byQueryDomain.equals(dnsLogLineList));
		verify(dataRepository).findByQueryDomain(QUERY_DOMAIN);
	}

	@Test
	public void should_find_by_query_target() {
		sut = new H2Service(dataRepository);
		List<DnsLogLine> byQueryTarget = sut.findByQueryTarget(QUERY_TARGET);

		assertTrue(byQueryTarget.equals(dnsLogLineList));
		verify(dataRepository).findByQueryTarget(QUERY_TARGET);
	}
}