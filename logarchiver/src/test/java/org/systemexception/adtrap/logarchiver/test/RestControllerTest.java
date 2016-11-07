package org.systemexception.adtrap.logarchiver.test;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers;
import org.springframework.security.web.FilterChainProxy;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.systemexception.adtrap.logarchiver.Application;
import org.systemexception.adtrap.logarchiver.SecurityConfig;
import org.systemexception.adtrap.logarchiver.controller.RestController;
import org.systemexception.adtrap.logarchiver.model.DnsLogLine;
import org.systemexception.adtrap.logarchiver.service.DataService;
import org.systemexception.adtrap.logarchiver.service.DhcpLeasesReader;

import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * @author leo
 * @date 02/11/2016 23:49
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = {Application.class})
@WebAppConfiguration
@TestPropertySource(locations = "classpath:application.properties")
// TODO add mocks and tests with asserts here
public class RestControllerTest {

	@Autowired
	private FilterChainProxy springSecurityFilterChain;
	@MockBean
	private DataService dataService;
	@MockBean
	private DhcpLeasesReader dhcpLeasesReader;
	@Autowired
	private RestController restController;
	private MockMvc sut;
	private DnsLogLine dnsLogLine;
	private static final long DNS_DATE = 1234567L;
	private static final String DNS_QUERY_TYPE = "config";
	private static final String DNS_QUERY_DOMAIN = "www.test.it";
	private static final String DNS_QUERY_TARGET = "8.8.8.8";

	@Before
	public void setUp() {
		dnsLogLine = new DnsLogLine();
		dnsLogLine.setDate(DNS_DATE);
		dnsLogLine.setQueryType(DNS_QUERY_TYPE);
		dnsLogLine.setQueryDomain(DNS_QUERY_DOMAIN);
		dnsLogLine.setQueryTarget(DNS_QUERY_TARGET);
		sut = MockMvcBuilders.standaloneSetup(restController)
				.apply(SecurityMockMvcConfigurers.springSecurity(springSecurityFilterChain)).build();
	}

	@Test
	@WithMockUser(username = SecurityConfig.ADMIN_USER, password = SecurityConfig.ADMIN_PASSWORD,
			roles = {SecurityConfig.ADMIN_ROLE})
	public void should_save_data() throws Exception {
		sut.perform(MockMvcRequestBuilders.post("/logarchiver/save").contentType(MediaType.APPLICATION_JSON_VALUE)
				.content(dataJson().getBytes())).andExpect(status().is(HttpStatus.CREATED.value()));

		verify(dataService).save(dnsLogLine);
	}

	@Test
	public void should_count_all() throws Exception {
		sut.perform(MockMvcRequestBuilders.get("/logarchiver/countall")).andExpect(
				status().is(HttpStatus.OK.value()));

		verify(dataService).countAll();
	}

	@Test
	public void should_count_all_filtered() throws Exception {
		sut.perform(MockMvcRequestBuilders.get("/logarchiver/countallfiltered")).andExpect(
				status().is(HttpStatus.OK.value()));

		verify(dataService).countAllFiltered();
	}

	@Test
	public void should_count_top_clients() throws Exception {
		sut.perform(MockMvcRequestBuilders.get("/logarchiver/counttopclients")).andExpect(
				status().is(HttpStatus.OK.value()));

		verify(dataService).countTopClients();
	}

	@Test
	public void should_count_top_requests() throws Exception {
		sut.perform(MockMvcRequestBuilders.get("/logarchiver/counttoprequests")).andExpect(
				status().is(HttpStatus.OK.value()));

		verify(dataService).countTopRequests();
	}

	@Test
	public void should_count_by_type() throws Exception {
		sut.perform(MockMvcRequestBuilders.get("/logarchiver/groupbyquerytype")).andExpect(
				status().is(HttpStatus.OK.value()));

		verify(dataService).groupByQueryType();
	}

	@Test
	public void should_count_by_domain() throws Exception {
		sut.perform(MockMvcRequestBuilders.get("/logarchiver/groupbyquerydomain")).andExpect(
				status().is(HttpStatus.OK.value()));

		verify(dataService).groupByQueryDomain();
	}

	@Test
	public void should_count_by_target() throws Exception {
		sut.perform(MockMvcRequestBuilders.get("/logarchiver/groupbyquerytarget")).andExpect(
				status().is(HttpStatus.OK.value()));

		verify(dataService).groupByQueryTarget();
	}

	@Test
	public void should_count_by_filtered_domain() throws Exception {
		sut.perform(MockMvcRequestBuilders.get("/logarchiver/groupbyfiltereddomains")).andExpect(
				status().is(HttpStatus.OK.value()));

		verify(dataService).groupByFilteredDomains();
	}

	@Test
	public void should_list_dhcp_leases() throws Exception {
		sut.perform(MockMvcRequestBuilders.get("/logarchiver/listdhcpleases")).andExpect(
				status().is(HttpStatus.OK.value()));

		verify(dhcpLeasesReader).getDhcpLeases();
	}

	private String dataJson() {
		return "{\"date\": \"" + DNS_DATE + "\"," +
				"\"queryType\": \"" + DNS_QUERY_TYPE + "\"," +
				"\"queryDomain\": \"" + DNS_QUERY_DOMAIN + "\"," +
				"\"queryTarget\": \"" + DNS_QUERY_TARGET + "\"}";
	}
}
