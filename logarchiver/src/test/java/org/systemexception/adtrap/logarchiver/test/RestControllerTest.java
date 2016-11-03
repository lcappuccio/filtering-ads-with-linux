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

	@Autowired
	private FilterChainProxy springSecurityFilterChain;
	@MockBean
	private DataService dataService;
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
	public void save_data() throws Exception {
		sut.perform(MockMvcRequestBuilders.post("/logarchiver/save").contentType(MediaType.APPLICATION_JSON_VALUE)
				.content(dataJson().getBytes())).andExpect(status().is(HttpStatus.CREATED.value()));

		verify(dataService).save(dnsLogLine);
	}

	private String dataJson() {
		return "{\"date\": \"" + DNS_DATE + "\"," +
				"\"queryType\": \"" + DNS_QUERY_TYPE + "\"," +
				"\"queryDomain\": \"" + DNS_QUERY_DOMAIN + "\"," +
				"\"queryTarget\": \"" + DNS_QUERY_TARGET + "\"}";
	}
}
