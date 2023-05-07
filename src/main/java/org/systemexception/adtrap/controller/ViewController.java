package org.systemexception.adtrap.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.systemexception.adtrap.pojo.StringUtils;
import org.systemexception.adtrap.service.DataService;

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
	private static final String VIEW_ADMIN = "admin";
	private static final String VIEW_CLIENTLIST = "clientlist";
	private static final String VIEW_LOGIN = "login";
	private static final String VIEW_STATISTICS = "statistics";
	private static final String VIEW_SYSTEM = "system";
	private static final String VIEW_TOP_FILTERED = "topfiltered";
	private static final String VIEW_TOP_REQUESTS = "toprequests";
	private final DataService dataService;

	@Autowired
	public ViewController(DataService dataService) {
		this.dataService = dataService;
	}

	@GetMapping(value = VIEW_LOGIN)
	public String login(Model model, String error, String logout) {
		if (error != null)
			model.addAttribute(ATTRIBUTE_ERROR, MESSAGE_ERROR);

		if (logout != null)
			model.addAttribute(ATTRIBUTE_MESSAGE, MESSAGE_LOGOUT);

		return VIEW_LOGIN;
	}

	@GetMapping(value = VIEW_ADMIN)
	public String viewAdmin(Model model) {
		model.addAttribute("ignoredDomainList", dataService.getIgnoredDomains());
		return VIEW_ADMIN;
	}

	@GetMapping(value = VIEW_CLIENTLIST)
	public String viewClientList(Model model) {
		return VIEW_CLIENTLIST;
	}

	@GetMapping(value = VIEW_STATISTICS)
	public String viewStatistics(Model model) {
		return VIEW_STATISTICS;
	}

	@GetMapping(value = VIEW_SYSTEM)
	public String viewSystem(Model model) {
		return VIEW_SYSTEM;
	}

	@GetMapping(value = VIEW_TOP_FILTERED)
	public String viewTopFiltered(Model model) {
		return VIEW_TOP_FILTERED;
	}

	@GetMapping(value = VIEW_TOP_REQUESTS)
	public String viewTopRequests(Model model) {
		return VIEW_TOP_REQUESTS;
	}
}
