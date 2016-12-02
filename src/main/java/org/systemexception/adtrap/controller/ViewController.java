package org.systemexception.adtrap.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.systemexception.adtrap.pojo.StringUtils;

/**
 * @author leo
 * @date 24/11/2016 10:49
 */
@Controller
@RequestMapping(value = StringUtils.EMTPY_STRING)
public class ViewController {

	public static final String ATTRIBUTE_ERROR = "error";
	public static final String ATTRIBUTE_MESSAGE = "message";
	public static final String MESSAGE_ERROR = "Bad username or password.";
	public static final String MESSAGE_LOGOUT = "You have been logged out.";
	private static final String VIEW_CLIENTLIST = "clientlist";
	private static final String VIEW_LOGIN = "login";
	private static final String VIEW_STATISTICS = "statistics";
	private static final String VIEW_SYSTEM = "system";
	private static final String VIEW_TOP_FILTERED = "topfiltered";
	private static final String VIEW_TOP_REQUESTS = "toprequests";

	@RequestMapping(value = VIEW_LOGIN, method = RequestMethod.GET)
	public String login(Model model, String error, String logout) {
		if (error != null)
			model.addAttribute(ATTRIBUTE_ERROR, MESSAGE_ERROR);

		if (logout != null)
			model.addAttribute(ATTRIBUTE_MESSAGE, MESSAGE_LOGOUT);

		return VIEW_LOGIN;
	}

	@RequestMapping(value = VIEW_CLIENTLIST, method = RequestMethod.GET)
	public String viewClientList(Model model) {
		return VIEW_CLIENTLIST;
	}

	@RequestMapping(value = VIEW_STATISTICS, method = RequestMethod.GET)
	public String viewStatistics(Model model) {
		return VIEW_STATISTICS;
	}

	@RequestMapping(value = VIEW_SYSTEM, method = RequestMethod.GET)
	public String viewSystem(Model model) {
		return VIEW_SYSTEM;
	}

	@RequestMapping(value = VIEW_TOP_FILTERED, method = RequestMethod.GET)
	public String viewTopFiltered(Model model) {
		return VIEW_TOP_FILTERED;
	}

	@RequestMapping(value = VIEW_TOP_REQUESTS, method = RequestMethod.GET)
	public String viewTopRequests(Model model) {
		return VIEW_TOP_REQUESTS;
	}
}
