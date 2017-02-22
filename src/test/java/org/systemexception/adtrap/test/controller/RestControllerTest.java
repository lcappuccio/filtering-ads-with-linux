package org.systemexception.adtrap.test.controller;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers;
import org.springframework.security.web.FilterChainProxy;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.systemexception.adtrap.Application;
import org.systemexception.adtrap.SecurityConfig;
import org.systemexception.adtrap.controller.RestController;
import org.systemexception.adtrap.service.DataService;
import org.systemexception.adtrap.service.DhcpLeasesReader;

import static org.junit.Assert.assertNull;
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
public class RestControllerTest {

	public static final String ADMIN = "admin", PASSWORD = "123456";
	@MockBean
	private DataService dataService;

	@MockBean
	private DhcpLeasesReader dhcpLeasesReader;

	@Autowired
	private RestController restController;

	@Autowired
	private FilterChainProxy springSecurityFilterChain;

	private MockMvc sut;

	@Before
	public void setUp() {
		sut = MockMvcBuilders.standaloneSetup(restController)
				.apply(SecurityMockMvcConfigurers.springSecurity(springSecurityFilterChain)).build();
	}

	@Test
	@WithMockUser(username = ADMIN, password = PASSWORD, roles = {SecurityConfig.USER_ROLE})
	public void should_count_all() throws Exception {
		ResultActions resultActions = sut.perform(MockMvcRequestBuilders.get("/restapi/countall"))
				.andExpect(status().isOk());

		assertNull(resultActions.andReturn().getResponse().getErrorMessage());
		verify(dataService).countAll();
	}

	@Test
	@WithMockUser(username = ADMIN, password = PASSWORD, roles = {SecurityConfig.USER_ROLE})
	public void should_count_all_filtered() throws Exception {
		ResultActions resultActions = sut.perform(MockMvcRequestBuilders.get("/restapi/countallfiltered"))
				.andExpect(status().isOk());

		assertNull(resultActions.andReturn().getResponse().getErrorMessage());
		verify(dataService).countAllFiltered();
	}

	@Test
	@WithMockUser(username = ADMIN, password = PASSWORD, roles = {SecurityConfig.USER_ROLE})
	public void should_count_distinct_filtered() throws Exception {
		ResultActions resultActions = sut.perform(MockMvcRequestBuilders.get("/restapi/countdistinctfiltered"))
				.andExpect(status().isOk());

		assertNull(resultActions.andReturn().getResponse().getErrorMessage());
		verify(dataService).countDistinctAdvertisersFiltered();
	}

	@Test
	@WithMockUser(username = ADMIN, password = PASSWORD, roles = {SecurityConfig.USER_ROLE})
	public void should_count_top_clients() throws Exception {
		ResultActions resultActions = sut.perform(MockMvcRequestBuilders.get("/restapi/counttopclients"))
				.andExpect(status().isOk());

		assertNull(resultActions.andReturn().getResponse().getErrorMessage());
		verify(dataService).countTopClients();
	}

	@Test
	@WithMockUser(username = ADMIN, password = PASSWORD, roles = {SecurityConfig.USER_ROLE})
	public void should_count_top_requests() throws Exception {
		ResultActions resultActions = sut.perform(MockMvcRequestBuilders.get("/restapi/counttoprequests"))
				.andExpect(status().isOk());

		assertNull(resultActions.andReturn().getResponse().getErrorMessage());
		verify(dataService).countTopRequests();
	}

	@Test
	@WithMockUser(username = ADMIN, password = PASSWORD, roles = {SecurityConfig.USER_ROLE})
	public void should_count_by_type() throws Exception {
		ResultActions resultActions = sut.perform(MockMvcRequestBuilders.get("/restapi/groupbyquerytype"))
				.andExpect(status().isOk());

		assertNull(resultActions.andReturn().getResponse().getErrorMessage());
		verify(dataService).groupByQueryType();
	}

	@Test
	@WithMockUser(username = ADMIN, password = PASSWORD, roles = {SecurityConfig.USER_ROLE})
	public void should_count_by_domain() throws Exception {
		ResultActions resultActions = sut.perform(MockMvcRequestBuilders.get("/restapi/groupbyquerydomain"))
				.andExpect(status().isOk());

		assertNull(resultActions.andReturn().getResponse().getErrorMessage());
		verify(dataService).groupByQueryDomain();
	}

	@Test
	@WithMockUser(username = ADMIN, password = PASSWORD, roles = {SecurityConfig.USER_ROLE})
	public void should_count_by_target() throws Exception {
		ResultActions resultActions = sut.perform(MockMvcRequestBuilders.get("/restapi/groupbyquerytarget"))
				.andExpect(status().isOk());

		assertNull(resultActions.andReturn().getResponse().getErrorMessage());
		verify(dataService).groupByQueryTarget();
	}

	@Test
	@WithMockUser(username = ADMIN, password = PASSWORD, roles = {SecurityConfig.USER_ROLE})
	public void should_count_by_filtered_domain() throws Exception {
		ResultActions resultActions = sut.perform(MockMvcRequestBuilders.get("/restapi/groupbyfiltereddomains"))
				.andExpect(status().isOk());

		assertNull(resultActions.andReturn().getResponse().getErrorMessage());
		verify(dataService).groupByFilteredDomains();
	}

	@Test
	@WithMockUser(username = ADMIN, password = PASSWORD, roles = {SecurityConfig.USER_ROLE})
	public void should_list_hourly_statistics() throws Exception {
		ResultActions resultActions = sut.perform(MockMvcRequestBuilders.get("/restapi/dailybyhour"))
				.andExpect(status().isOk());

		assertNull(resultActions.andReturn().getResponse().getErrorMessage());
		verify(dataService).dailyByHour();
	}

	@Test
	@WithMockUser(username = ADMIN, password = PASSWORD, roles = {SecurityConfig.USER_ROLE})
	public void should_list_daily_statistics() throws Exception {
		ResultActions resultActions = sut.perform(MockMvcRequestBuilders.get("/restapi/monthlybyday"))
				.andExpect(status().isOk());

		assertNull(resultActions.andReturn().getResponse().getErrorMessage());
		verify(dataService).monthlyByDay();
	}

	@Test
	@WithMockUser(username = ADMIN, password = PASSWORD, roles = {SecurityConfig.USER_ROLE})
	public void should_list_dhcp_leases() throws Exception {
		ResultActions resultActions = sut.perform(MockMvcRequestBuilders.get("/restapi/listdhcpleases"))
				.andExpect(status().isOk());

		assertNull(resultActions.andReturn().getResponse().getErrorMessage());
		verify(dhcpLeasesReader).getDhcpLeases();
	}

	@Test
	@WithMockUser(username = ADMIN, password = PASSWORD, roles = {SecurityConfig.USER_ROLE})
	public void should_list_ignored_domains() throws Exception {
		ResultActions resultActions = sut.perform(MockMvcRequestBuilders.get("/restapi/getignoreddomains"))
				.andExpect(status().isOk());

		assertNull(resultActions.andReturn().getResponse().getErrorMessage());
		verify(dataService).getIgnoredDomains();
	}
}
