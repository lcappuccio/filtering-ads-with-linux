package org.systemexception.adtrap.statsviewer.test;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
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
import org.systemexception.adtrap.statsviewer.Application;
import org.systemexception.adtrap.statsviewer.SecurityConfig;
import org.systemexception.adtrap.statsviewer.controller.RestController;

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
	@Autowired
	private RestController restController;
	private MockMvc sut;

	@Before
	public void setUp() {
		sut = MockMvcBuilders.standaloneSetup(restController)
				.apply(SecurityMockMvcConfigurers.springSecurity(springSecurityFilterChain)).build();
	}

	@Test
	@WithMockUser(username = SecurityConfig.ADMIN_USER, password = SecurityConfig.ADMIN_PASSWORD,
			roles = {SecurityConfig.ADMIN_ROLE})
	public void save_data() throws Exception {
		sut.perform(MockMvcRequestBuilders.post("/statsviewer/save").contentType(MediaType.APPLICATION_JSON_VALUE)
				.content(dataJson().getBytes())).andExpect(status().is(HttpStatus.CREATED.value()));
	}

	private String dataJson() {
		return "{\"date\": \"1234567\",\"queryType\": \"123\",\"queryDomain\": \"www.test.org\"," +
				"\"queryTarget\": \"8.8.8.8\"}";
	}
}
