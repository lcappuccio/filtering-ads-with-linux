package org.systemexception.adtrap.logarchiver.controller;

import io.swagger.annotations.Api;
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
import org.systemexception.adtrap.logarchiver.Application;
import org.systemexception.adtrap.logarchiver.model.DnsLogLine;
import org.systemexception.adtrap.logarchiver.service.DataService;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import javax.validation.Valid;
import java.text.ParseException;
import java.util.List;
import java.util.Map;

/**
 * @author leo
 * @date 02/11/2016 21:36
 */
@Controller
@RequestMapping(value = Application.CONTEXT)
@EnableSwagger2
@Api(basePath = Application.CONTEXT, value = "Data", description = "Data REST API")
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

	@RequestMapping(value = "countall", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Integer> countAll() {

		LOGGER.info("Count all");

		return new ResponseEntity<>(dataService.countAll(), HttpStatus.OK);
	}

	@RequestMapping(value = "countallfiltered", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Integer> countAllFiltered() {

		LOGGER.info("Count all filtered");

		return new ResponseEntity<>(dataService.countAllFiltered(), HttpStatus.OK);
	}

	@RequestMapping(value = "counttopclients", method = RequestMethod.GET,
			produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<List<Map>> countTopClients() {

		LOGGER.info("Count top clients");
		List countTopRequests = dataService.countTopClients();

		return new ResponseEntity<>(countTopRequests, HttpStatus.OK);
	}

	@RequestMapping(value = "counttoprequests", method = RequestMethod.GET,
			produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<List<Map>> countTopRequests() {

		LOGGER.info("Count top requests");
		List countTopRequests = dataService.countTopRequests();

		return new ResponseEntity<>(countTopRequests, HttpStatus.OK);
	}

	@RequestMapping(value = "groupbyquerytype", method = RequestMethod.GET,
			produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<List<Map>> groupByQueryType() {

		LOGGER.info("Group by query type");
		List groupByQueryResult = dataService.groupByQueryType();

		return new ResponseEntity<>(groupByQueryResult, HttpStatus.OK);
	}

	@RequestMapping(value = "groupbyquerydomain", method = RequestMethod.GET,
			produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<List<Map>> groupByQueryDomain() {

		LOGGER.info("Group by query domain");
		List groupByQueryResult = dataService.groupByQueryDomain();

		return new ResponseEntity<>(groupByQueryResult, HttpStatus.OK);
	}

	@RequestMapping(value = "groupbyquerytarget", method = RequestMethod.GET,
			produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<List<Map>> groupByQueryTarget() {

		LOGGER.info("Group by query target");
		List groupByQueryResult = dataService.groupByQueryTarget();

		return new ResponseEntity<>(groupByQueryResult, HttpStatus.OK);
	}

	@RequestMapping(value = "groupbyfiltereddomains", method = RequestMethod.GET,
			produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<List<Map>> groupByFilteredDomains() {

		LOGGER.info("Group by query filtered domains");
		List groupByQueryResult = dataService.groupByFilteredDomains();

		return new ResponseEntity<>(groupByQueryResult, HttpStatus.OK);
	}
}
