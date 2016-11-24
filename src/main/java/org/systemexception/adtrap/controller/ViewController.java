package org.systemexception.adtrap.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * @author leo
 * @date 24/11/2016 10:49
 */
@Controller
@RequestMapping(value = "")
public class ViewController {

	public static final String ATTRIBUTE_ERROR = "error";
	public static final String ATTRIBUTE_MESSAGE = "message";
	public static final String MESSAGE_ERROR = "Bad username or password.";
	public static final String MESSAGE_LOGOUT = "You have been logged out.";

	@RequestMapping(value = "/login", method = RequestMethod.GET)
	public String login(Model model, String error, String logout) {
		if (error != null)
			model.addAttribute(ATTRIBUTE_ERROR, MESSAGE_ERROR);

		if (logout != null)
			model.addAttribute(ATTRIBUTE_MESSAGE, MESSAGE_LOGOUT);

		return "login";
	}

	@RequestMapping(value = "/clientlist", method = RequestMethod.GET)
	public String viewClientList(Model model) {
		return "clientlist";
	}

	@RequestMapping(value = "/statistics", method = RequestMethod.GET)
	public String viewStatistics(Model model) {
		return "statistics";
	}

	@RequestMapping(value = "/system", method = RequestMethod.GET)
	public String viewSystem(Model model) {
		return "system";
	}

	@RequestMapping(value = "/topfiltered", method = RequestMethod.GET)
	public String viewTopFiltered(Model model) {
		return "topfiltered";
	}

	@RequestMapping(value = "/toprequests", method = RequestMethod.GET)
	public String viewTopRequests(Model model) {
		return "toprequests";
	}
}
