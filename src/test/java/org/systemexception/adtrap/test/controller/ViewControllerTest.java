package org.systemexception.adtrap.test.controller;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers;
import org.springframework.security.web.FilterChainProxy;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.systemexception.adtrap.Application;
import org.systemexception.adtrap.SecurityConfig;
import org.systemexception.adtrap.controller.ViewController;

import static junit.framework.TestCase.assertNull;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * @author leo
 * @date 24/11/2016 11:39
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = {Application.class})
@WebAppConfiguration
@TestPropertySource(locations = "classpath:application.properties")
public class ViewControllerTest {

	private static final String TITLE_HEADER_TAG = "<title>adtrap</title>";
	private static final String TITLE_LOGIN_HEADER_TAG = "<title>adtrap - login</title>";

	@Autowired
	private FilterChainProxy springSecurityFilterChain;

	@Autowired
	private WebApplicationContext wac;

	private MockMvc sut;

	@Before
	public void setUp() {
		sut = MockMvcBuilders.webAppContextSetup(wac)
				.apply(SecurityMockMvcConfigurers.springSecurity(springSecurityFilterChain)).build();
	}

	@Test
	public void main_page_is_redirect() throws Exception {
		MvcResult mvcResult = sut.perform(MockMvcRequestBuilders.get("/")).andExpect(status().isFound())
				.andReturn();

		assertNull(mvcResult.getResponse().getErrorMessage());
	}

	@Test
	public void login_should_be_allowed_to_all() throws Exception {
		MvcResult mvcResult = sut.perform(MockMvcRequestBuilders.get("/login")).andExpect(status().isOk())
				.andReturn();

		assertNull(mvcResult.getResponse().getErrorMessage());
		assertTrue(mvcResult.getResponse().getContentAsString().contains("Please Login"));
		assertTrue(mvcResult.getResponse().getContentAsString().contains(TITLE_LOGIN_HEADER_TAG));
	}

	@Test
	public void login_with_bad_credentials() throws Exception {
		MvcResult mvcResult = sut.perform(MockMvcRequestBuilders.get("/login?error")
				.param(ViewController.ATTRIBUTE_ERROR, "someError")).andExpect(status().isOk()).andReturn();

		assertNull(mvcResult.getResponse().getErrorMessage());
		assertTrue(mvcResult.getResponse().getContentAsString().contains("Please Login"));
		assertTrue(mvcResult.getResponse().getContentAsString().contains(TITLE_LOGIN_HEADER_TAG));
		assertTrue(mvcResult.getResponse().getContentAsString().contains(ViewController.MESSAGE_ERROR));
		assertEquals(ViewController.MESSAGE_ERROR,
				mvcResult.getModelAndView().getModel().get(ViewController.ATTRIBUTE_ERROR));
	}

	@Test
	@WithMockUser(username = RestControllerTest.ADMIN, password = RestControllerTest.PASSWORD,
			roles = {SecurityConfig.USER_ROLE})
	public void logout_authenticated_user() throws Exception {
		MvcResult mvcResult = sut.perform(MockMvcRequestBuilders.get("/login?logout")
				.param("logout", "logoutMessage")).andExpect(status().isOk()).andReturn();

		assertNull(mvcResult.getResponse().getErrorMessage());
		assertTrue(mvcResult.getResponse().getContentAsString().contains("Please Login"));
		assertTrue(mvcResult.getResponse().getContentAsString().contains(TITLE_LOGIN_HEADER_TAG));
		assertTrue(mvcResult.getResponse().getContentAsString().contains(ViewController.MESSAGE_LOGOUT));
		assertEquals(ViewController.MESSAGE_LOGOUT,
				mvcResult.getModelAndView().getModel().get(ViewController.ATTRIBUTE_MESSAGE));
	}

	@Test
	@WithMockUser(username = RestControllerTest.ADMIN, password = RestControllerTest.PASSWORD,
			roles = {SecurityConfig.USER_ROLE})
	public void clientlist() throws Exception {
		MvcResult mvcResult = sut.perform(MockMvcRequestBuilders.get("/clientlist")).andExpect(status()
				.isOk()).andReturn();

		assertNull(mvcResult.getResponse().getErrorMessage());
		assertTrue(mvcResult.getResponse().getContentAsString().contains(TITLE_HEADER_TAG));
	}

	@Test
	@WithMockUser(username = RestControllerTest.ADMIN, password = RestControllerTest.PASSWORD,
			roles = {SecurityConfig.USER_ROLE})
	public void admin() throws Exception {
		MvcResult mvcResult = sut.perform(MockMvcRequestBuilders.get("/admin")).andExpect(status()
				.isOk()).andReturn();

		assertNull(mvcResult.getResponse().getErrorMessage());
		assertTrue(mvcResult.getResponse().getContentAsString().contains(TITLE_HEADER_TAG));
	}

	@Test
	@WithMockUser(username = RestControllerTest.ADMIN, password = RestControllerTest.PASSWORD,
			roles = {SecurityConfig.USER_ROLE})
	public void statistics() throws Exception {
		MvcResult mvcResult = sut.perform(MockMvcRequestBuilders.get("/statistics")).andExpect(status()
				.isOk()).andReturn();

		assertNull(mvcResult.getResponse().getErrorMessage());
		assertTrue(mvcResult.getResponse().getContentAsString().contains(TITLE_HEADER_TAG));
	}

	@Test
	@WithMockUser(username = RestControllerTest.ADMIN, password = RestControllerTest.PASSWORD,
			roles = {SecurityConfig.USER_ROLE})
	public void system() throws Exception {
		MvcResult mvcResult = sut.perform(MockMvcRequestBuilders.get("/system")).andExpect(status()
				.isOk()).andReturn();

		assertNull(mvcResult.getResponse().getErrorMessage());
		assertTrue(mvcResult.getResponse().getContentAsString().contains(TITLE_HEADER_TAG));
	}

	@Test
	@WithMockUser(username = RestControllerTest.ADMIN, password = RestControllerTest.PASSWORD,
			roles = {SecurityConfig.USER_ROLE})
	public void topfiltered() throws Exception {
		MvcResult mvcResult = sut.perform(MockMvcRequestBuilders.get("/topfiltered")).andExpect(status()
				.isOk()).andReturn();

		assertNull(mvcResult.getResponse().getErrorMessage());
		assertTrue(mvcResult.getResponse().getContentAsString().contains(TITLE_HEADER_TAG));
	}

	@Test
	@WithMockUser(username = RestControllerTest.ADMIN, password = RestControllerTest.PASSWORD,
			roles = {SecurityConfig.USER_ROLE})
	public void toprequests() throws Exception {
		MvcResult mvcResult = sut.perform(MockMvcRequestBuilders.get("/toprequests")).andExpect(status()
				.isOk()).andReturn();

		assertNull(mvcResult.getResponse().getErrorMessage());
		assertTrue(mvcResult.getResponse().getContentAsString().contains(TITLE_HEADER_TAG));
	}
}
