package org.systemexception.adtrap.test.service;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.web.WebAppConfiguration;
import org.systemexception.adtrap.Application;
import org.systemexception.adtrap.model.DnsLogLine;
import org.systemexception.adtrap.pojo.Queries;
import org.systemexception.adtrap.service.DataService;
import org.systemexception.adtrap.service.MySqlDataService;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

/**
 * @author leo
 * @date 06/11/2016 21:13
 */
@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = {Application.class})
@WebAppConfiguration
@TestPropertySource(locations = "classpath:application-test.properties")
class MySqlDataServiceTest {

	@Value("${adtrap.ipaddress}")
	private String ipAddress;

	@Value("${home.domain}")
	private String homeDomain;

	@Autowired
	private DataService sut;

	@Autowired
	private JdbcTemplate jdbcTemplate;
	private DnsLogLine dnsLogLine;

	@BeforeEach
	void setUp() {
		assertNotNull(jdbcTemplate);
		assertNotNull(homeDomain);
		dnsLogLine = new DnsLogLine();
		long oneDayInMillis = 1000 * 60 * 60 * 24;
		dnsLogLine.setDate(System.currentTimeMillis() - oneDayInMillis);
		dnsLogLine.setQueryDomain("TestQueryDomain");
		dnsLogLine.setQueryTarget("127.0.0.1");
		dnsLogLine.setQueryType("query[A]");
		sut.save(dnsLogLine);
		sut.addIgnoredDomain("example_domain");
	}

	@AfterEach
	void tearDown() {
		jdbcTemplate.update("DELETE FROM DNS_LOG_LINES");
		jdbcTemplate.update("DELETE FROM DNS_IGNORE");
	}

	@Test
	void should_save_data() {
		DnsLogLine save = sut.save(dnsLogLine);

		assertEquals(dnsLogLine, save);
	}

	@Test
	void should_count_all() {
		int countAll = sut.countAll();

		assertTrue(countAll > 0);
	}

	@Test
	void should_count_filtered() {
		dnsLogLine = new DnsLogLine();
		dnsLogLine.setDate(System.currentTimeMillis());
		dnsLogLine.setQueryDomain("TestQueryDomain");
		dnsLogLine.setQueryTarget(ipAddress);
		dnsLogLine.setQueryType("query[A]");
		sut.save(dnsLogLine);

		int countAllFiltered = sut.countAllFiltered();

		assertTrue(countAllFiltered > 0);
	}

	@Test
	void should_count_distinct_advertisers_filtered() {
		int countAdvertisers = sut.countDistinctAdvertisersFiltered();

        assertTrue(countAdvertisers > 0);
	}

	@Test
	void should_count_top_clients() {
		List<Map<String, Object>> maps = sut.countTopClients();

        assertFalse(maps.isEmpty());
	}

	@Test
	void should_count_top_requests() {
		List<Map<String, Object>> maps = sut.countTopRequests();

        assertFalse(maps.isEmpty());
	}

	@Test
	void should_group_by_query_type() {
		List<Map<String, Object>> maps = sut.groupByQueryType();

        assertFalse(maps.isEmpty());
	}

	@Test
	void should_group_by_query_domain() {
		List<Map<String, Object>> maps = sut.groupByQueryDomain();

        assertFalse(maps.isEmpty());
	}

	@Test
	void should_group_by_query_target() {
		List<Map<String, Object>> maps = sut.groupByQueryTarget();

        assertFalse(maps.isEmpty());
	}

	@Test
	void should_group_by_filtered_domains() {
		List<Map<String, Object>> maps = sut.groupByFilteredDomains();

        assertFalse(maps.isEmpty());
	}

	@Test
	void should_count_hourly_stats() {
		List<Map<String, Object>> maps = sut.dailyByHour();

        assertFalse(maps.isEmpty());
	}

	@Test
	void should_count_monthly_stats() {
		List<Map<String, Object>> maps = sut.monthlyByDay();

        assertFalse(maps.isEmpty());
	}

	@Test
	void should_have_an_ignored_domain() {
		List<Map<String, Object>> ignoredDomains = sut.getIgnoredDomains();

		assertEquals(1, ignoredDomains.size());
	}

	@Test
	void should_add_an_ignored_domain() {
		String ignoredDomain = "TEST" + System.currentTimeMillis();
		sut.addIgnoredDomain(ignoredDomain);
		boolean domainIsSaved = false;

		List<Map<String, Object>> ignoredDomains = sut.getIgnoredDomains();

		for (Map<String, Object> object: ignoredDomains) {
            if (object.containsValue(ignoredDomain)) {
                domainIsSaved = true;
                break;
            }
		}

		assertTrue(domainIsSaved);
		assertEquals(2, ignoredDomains.size());
	}

	@Test
	void should_remove_an_ignored_domain() {
		String ignoredDomain = "TEST" + System.currentTimeMillis();
		jdbcTemplate.update(Queries.SAVE_IGNORED_DOMAIN, ignoredDomain);
		sut.removeIgnoredDomain(ignoredDomain);
		boolean domainIsSaved = false;

		List<Map<String, Object>> ignoredDomains = sut.getIgnoredDomains();

		for (Map<String, Object> object: ignoredDomains) {
            if (object.containsValue(ignoredDomain)) {
                domainIsSaved = true;
                break;
            }
		}

		assertFalse(domainIsSaved);
		assertEquals(1, ignoredDomains.size());
	}

    @Test
    void should_cleanup_database() {
        final JdbcTemplate mockJdbcTemplate = mock(JdbcTemplate.class);
        doReturn(50).when(mockJdbcTemplate).update(eq(Queries.CLEANUP), anyLong());
        MySqlDataService innerSut = new MySqlDataService(mockJdbcTemplate, anyString(), anyString());

        innerSut.cleanUpDatabase();

        verify(mockJdbcTemplate, times(1)).update(eq(Queries.CLEANUP), anyLong());
    }
}
