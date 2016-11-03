package org.systemexception.adtrap.logarchiver.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.systemexception.adtrap.logarchiver.model.DnsLogLine;
import org.systemexception.adtrap.logarchiver.service.DataService;

import javax.validation.Valid;
import java.text.ParseException;

/**
 * @author leo
 * @date 02/11/2016 21:36
 */
@Controller
@RequestMapping(value = "logarchiver")
public class RestController {

	private static final Logger LOGGER = LoggerFactory.getLogger(RestController.class);
	private final DataService dataService;

	@Autowired
	public RestController(DataService dataService) {
		this.dataService = dataService;
	}

	@RequestMapping(value = "save", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE,
			produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<DnsLogLine> save(@RequestBody @Valid DnsLogLine dnsLogLine) throws ParseException {

		LOGGER.info("Received: " + dnsLogLine.toString());
		DnsLogLine dnsLogLineSaved = dataService.save(dnsLogLine);

		return new ResponseEntity<>(dnsLogLineSaved, HttpStatus.CREATED);
	}
}
