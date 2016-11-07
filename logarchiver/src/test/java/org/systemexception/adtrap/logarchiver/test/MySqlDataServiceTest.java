package org.systemexception.adtrap.logarchiver.test;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.systemexception.adtrap.logarchiver.Application;
import org.systemexception.adtrap.logarchiver.model.DnsLogLine;
import org.systemexception.adtrap.logarchiver.service.MySqlDataService;

import java.util.List;
import java.util.Map;

import static junit.framework.TestCase.assertTrue;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * @author leo
 * @date 06/11/2016 21:13
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = {Application.class})
@WebAppConfiguration
@TestPropertySource(locations = "classpath:application.properties")
public class MySqlDataServiceTest {

	@Value("${adtrap.ipaddress}")
	private String ipAddress;
	@Autowired
	private MySqlDataService sut;
	@Autowired
	private JdbcTemplate jdbcTemplate;
	private DnsLogLine dnsLogLine;

	@Before
	public void setUp() {
		assertNotNull(jdbcTemplate);
		dnsLogLine = new DnsLogLine();
		dnsLogLine.setDate(System.currentTimeMillis());
		dnsLogLine.setQueryDomain("TestQueryDomain");
		dnsLogLine.setQueryTarget("TestQueryTarget");
		dnsLogLine.setQueryType("query[A]");
		sut.save(dnsLogLine);
	}

	@Test
	public void should_save_data() {
		DnsLogLine save = sut.save(dnsLogLine);

		assertEquals(dnsLogLine, save);
	}

	@Test
	public void should_count_all() {
		int countAll = sut.countAll();

		assertTrue("No records in table", countAll > 0);
	}

	@Test
	public void should_count_filtered() {
		dnsLogLine = new DnsLogLine();
		dnsLogLine.setDate(System.currentTimeMillis());
		dnsLogLine.setQueryDomain("TestQueryDomain");
		dnsLogLine.setQueryTarget(ipAddress);
		dnsLogLine.setQueryType("query[A]");
		sut.save(dnsLogLine);

		int countAllFiltered = sut.countAllFiltered();

		assertTrue("No filtered record", countAllFiltered > 0);
	}

	@Test
	public void should_count_top_clients() {
		List<Map<String, Object>> maps = sut.countTopClients();

		assertTrue(maps.size() > 0);
	}

	@Test
	public void should_count_top_requests() {
		List<Map<String, Object>> maps = sut.countTopRequests();

		assertTrue(maps.size() > 0);
	}

	@Test
	public void should_group_by_query_type() {
		List<Map<String, Object>> maps = sut.groupByQueryType();

		assertTrue(maps.size() > 0);
	}

	@Test
	public void should_group_by_query_domain() {
		List<Map<String, Object>> maps = sut.groupByQueryDomain();

		assertTrue(maps.size() > 0);
	}

	@Test
	public void should_group_by_query_target() {
		List<Map<String, Object>> maps = sut.groupByQueryTarget();

		assertTrue(maps.size() > 0);
	}

	@Test
	public void should_group_by_filtered_domains() {
		List<Map<String, Object>> maps = sut.groupByFilteredDomains();

		assertTrue(maps.size() > 0);
	}
}
