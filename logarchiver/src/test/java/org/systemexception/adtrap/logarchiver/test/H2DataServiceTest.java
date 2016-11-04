package org.systemexception.adtrap.logarchiver.test;

import org.junit.Before;
import org.junit.Test;
import org.systemexception.adtrap.logarchiver.model.DnsLogLine;
import org.systemexception.adtrap.logarchiver.repositories.DnsLogLineRepository;
import org.systemexception.adtrap.logarchiver.service.H2DataService;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.*;

/**
 * @author leo
 * @date 07/10/2016 19:02
 */
public class H2DataServiceTest {

	private H2DataService sut;
	private DnsLogLineRepository dataRepository;
	private DnsLogLine dnsLogLine;
	private final List<DnsLogLine> dnsLogLineList = new ArrayList<>();
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
		sut = new H2DataService(dataRepository);
	}

	@Test
	public void should_save_data() {
		DnsLogLine saved = sut.save(dnsLogLine);

		assertTrue(saved.equals(dnsLogLine));
		verify(dataRepository).save(dnsLogLine);
	}

	@Test
	public void should_count_all() {
		sut.countAll();

		verify(dataRepository).countAll();
	}

	@Test
	public void should_clean_up() {
		sut.cleanUpDatabase();

		verify(dataRepository).cleanUp(anyLong());
	}
}