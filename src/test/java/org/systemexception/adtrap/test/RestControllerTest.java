package org.systemexception.adtrap.test;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.systemexception.adtrap.Application;
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
// TODO add mocks and tests with asserts here
public class RestControllerTest {

	@MockBean
	private DataService dataService;
	@MockBean
	private DhcpLeasesReader dhcpLeasesReader;
	@Autowired
	private RestController restController;
	private MockMvc sut;

	@Before
	public void setUp() {
		sut = MockMvcBuilders.standaloneSetup(restController).build();
	}

	@Test
	public void should_count_all() throws Exception {
		ResultActions resultActions = sut.perform(MockMvcRequestBuilders.get("/logarchiver/countall"))
				.andExpect(status().is(HttpStatus.OK.value()));

		assertNull(resultActions.andReturn().getResponse().getErrorMessage());
		verify(dataService).countAll();
	}

	@Test
	public void should_count_all_filtered() throws Exception {
		ResultActions resultActions = sut.perform(MockMvcRequestBuilders.get("/logarchiver/countallfiltered"))
				.andExpect(status().is(HttpStatus.OK.value()));

		assertNull(resultActions.andReturn().getResponse().getErrorMessage());
		verify(dataService).countAllFiltered();
	}

	@Test
	public void should_count_top_clients() throws Exception {
		ResultActions resultActions = sut.perform(MockMvcRequestBuilders.get("/logarchiver/counttopclients"))
				.andExpect(status().is(HttpStatus.OK.value()));

		assertNull(resultActions.andReturn().getResponse().getErrorMessage());
		verify(dataService).countTopClients();
	}

	@Test
	public void should_count_top_requests() throws Exception {
		ResultActions resultActions = sut.perform(MockMvcRequestBuilders.get("/logarchiver/counttoprequests"))
				.andExpect(status().is(HttpStatus.OK.value()));

		assertNull(resultActions.andReturn().getResponse().getErrorMessage());
		verify(dataService).countTopRequests();
	}

	@Test
	public void should_count_by_type() throws Exception {
		ResultActions resultActions = sut.perform(MockMvcRequestBuilders.get("/logarchiver/groupbyquerytype"))
				.andExpect(status().is(HttpStatus.OK.value()));

		assertNull(resultActions.andReturn().getResponse().getErrorMessage());
		verify(dataService).groupByQueryType();
	}

	@Test
	public void should_count_by_domain() throws Exception {
		ResultActions resultActions = sut.perform(MockMvcRequestBuilders.get("/logarchiver/groupbyquerydomain"))
				.andExpect(status().is(HttpStatus.OK.value()));

		assertNull(resultActions.andReturn().getResponse().getErrorMessage());
		verify(dataService).groupByQueryDomain();
	}

	@Test
	public void should_count_by_target() throws Exception {
		ResultActions resultActions = sut.perform(MockMvcRequestBuilders.get("/logarchiver/groupbyquerytarget"))
				.andExpect(status().is(HttpStatus.OK.value()));

		assertNull(resultActions.andReturn().getResponse().getErrorMessage());
		verify(dataService).groupByQueryTarget();
	}

	@Test
	public void should_count_by_filtered_domain() throws Exception {
		ResultActions resultActions = sut.perform(MockMvcRequestBuilders.get("/logarchiver/groupbyfiltereddomains"))
				.andExpect(status().is(HttpStatus.OK.value()));

		assertNull(resultActions.andReturn().getResponse().getErrorMessage());
		verify(dataService).groupByFilteredDomains();
	}

	@Test
	public void should_list_hourly_statistics() throws Exception {
		ResultActions resultActions = sut.perform(MockMvcRequestBuilders.get("/logarchiver/dailybyhour"))
				.andExpect(status().is(HttpStatus.OK.value()));

		assertNull(resultActions.andReturn().getResponse().getErrorMessage());
		verify(dataService).dailyByHour();
	}

	@Test
	public void should_list_daily_statistics() throws Exception {
		ResultActions resultActions = sut.perform(MockMvcRequestBuilders.get("/logarchiver/monthlybyday"))
				.andExpect(status().is(HttpStatus.OK.value()));

		assertNull(resultActions.andReturn().getResponse().getErrorMessage());
		verify(dataService).monthlyByDay();
	}

	@Test
	public void should_list_dhcp_leases() throws Exception {
		ResultActions resultActions = sut.perform(MockMvcRequestBuilders.get("/logarchiver/listdhcpleases"))
				.andExpect(status().is(HttpStatus.OK.value()));

		assertNull(resultActions.andReturn().getResponse().getErrorMessage());
		verify(dhcpLeasesReader).getDhcpLeases();
	}
}
