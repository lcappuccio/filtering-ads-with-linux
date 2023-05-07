package org.systemexception.adtrap.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.systemexception.adtrap.Application;
import org.systemexception.adtrap.model.DhcpLease;
import org.systemexception.adtrap.pojo.StringUtils;
import org.systemexception.adtrap.service.DataService;
import org.systemexception.adtrap.service.DhcpLeasesReader;

import java.util.List;
import java.util.Map;

/**
 * @author leo
 * @date 02/11/2016 21:36
 */
@Controller
@RequestMapping(value = Application.CONTEXT)
//@EnableSwagger2
//@Api(basePath = Application.CONTEXT, value = "Data", description = "Data REST API")
public class RestController {

	private static final Logger LOGGER = LoggerFactory.getLogger(RestController.class);
	private static final String DOMAIN_PARAMETER = "domain";
	private final DataService dataService;
	private final DhcpLeasesReader dhcpLeasesReader;

	@Autowired
	public RestController(DataService dataService, DhcpLeasesReader dhcpLeasesReader) {
		this.dataService = dataService;
		this.dhcpLeasesReader = dhcpLeasesReader;
	}

	@GetMapping(value = "countall", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Integer> countAll() {

		LOGGER.info("RestController Count all");

		return new ResponseEntity<>(dataService.countAll(), HttpStatus.OK);
	}

	@GetMapping(value = "countallfiltered", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Integer> countAllFiltered() {

		LOGGER.info("RestController Count all filtered");

		return new ResponseEntity<>(dataService.countAllFiltered(), HttpStatus.OK);
	}

	@GetMapping(value = "countdistinctfiltered", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Integer> countDistinctFiltered() {

		LOGGER.info("RestController Count distinct filtered");

		return new ResponseEntity<>(dataService.countDistinctAdvertisersFiltered(), HttpStatus.OK);
	}

	@GetMapping(value = "counttopclients", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<List<Map<String, Object>>> countTopClients() {

		LOGGER.info("RestController Count top clients");
        List<Map<String, Object>> countTopRequests = dataService.countTopClients();

		return new ResponseEntity<>(countTopRequests, HttpStatus.OK);
	}

	@GetMapping(value = "counttoprequests", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<List<Map<String, Object>>> countTopRequests() {

		LOGGER.info("RestController Count top requests");
        List<Map<String, Object>> countTopRequests = dataService.countTopRequests();

		return new ResponseEntity<>(countTopRequests, HttpStatus.OK);
	}

	@GetMapping(value = "groupbyquerytype", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<List<Map<String, Object>>> groupByQueryType() {

		LOGGER.info("RestController Group by query type");
        List<Map<String, Object>> groupByQueryResult = dataService.groupByQueryType();

		return new ResponseEntity<>(groupByQueryResult, HttpStatus.OK);
	}

	@GetMapping(value = "groupbyquerydomain", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<List<Map<String, Object>>> groupByQueryDomain() {

		LOGGER.info("RestController Group by query domain");
        List<Map<String, Object>> groupByQueryResult = dataService.groupByQueryDomain();

		return new ResponseEntity<>(groupByQueryResult, HttpStatus.OK);
	}

	@GetMapping(value = "groupbyquerytarget", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<List<Map<String, Object>>> groupByQueryTarget() {

		LOGGER.info("RestController Group by query target");
        List<Map<String, Object>> groupByQueryResult = dataService.groupByQueryTarget();

		return new ResponseEntity<>(groupByQueryResult, HttpStatus.OK);
	}

	@GetMapping(value = "groupbyfiltereddomains", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<List<Map<String, Object>>> groupByFilteredDomains() {

		LOGGER.info("RestController Group by query filtered domains");
        List<Map<String, Object>> groupByQueryResult = dataService.groupByFilteredDomains();

		return new ResponseEntity<>(groupByQueryResult, HttpStatus.OK);
	}

	@GetMapping(value = "dailybyhour", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<List<Map<String, Object>>> dailyByHour() {

		LOGGER.info("RestController Get statistics by hour");
        List<Map<String, Object>> groupByQueryResult = dataService.dailyByHour();

		return new ResponseEntity<>(groupByQueryResult, HttpStatus.OK);
	}

	@GetMapping(value = "monthlybyday", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<List<Map<String, Object>>> monthlyByDay() {

		LOGGER.info("RestController Get statistics by day");
        List<Map<String, Object>> groupByQueryResult = dataService.monthlyByDay();

		return new ResponseEntity<>(groupByQueryResult, HttpStatus.OK);
	}

	@GetMapping(value = "listdhcpleases", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<List<DhcpLease>> listDhcpLeases() {

		LOGGER.info("RestController List DHCP leases");
		List<DhcpLease> dhcpLeases = dhcpLeasesReader.getDhcpLeases();

		return new ResponseEntity<>(dhcpLeases, HttpStatus.OK);
	}

	@GetMapping(value = "getignoreddomains", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<List<Map<String, Object>>> getIgnoredDomains() {

		LOGGER.info("RestController List ignored domains");
		List<Map<String, Object>> ignoredDomains = dataService.getIgnoredDomains();

		return new ResponseEntity<>(ignoredDomains, HttpStatus.OK);
	}

	@PostMapping(value = "addignoreddomain", params = {DOMAIN_PARAMETER}, produces = MediaType.TEXT_PLAIN_VALUE)
	public ResponseEntity<HttpStatus> addIgnoredDomain(@RequestParam(value = DOMAIN_PARAMETER) final String ignoredDomain) {

		LOGGER.info("RestController Add ignored domain {}", StringUtils.sanitizeDomain(ignoredDomain));
		dataService.addIgnoredDomain(ignoredDomain);

		return new ResponseEntity<>(HttpStatus.OK);
	}

	@PostMapping(value = "removeignoreddomain", params = {DOMAIN_PARAMETER}, produces = MediaType.TEXT_PLAIN_VALUE)
	public ResponseEntity<HttpStatus> removeIgnoredDomain(@RequestParam(value = DOMAIN_PARAMETER) final String ignoredDomain) {

		LOGGER.info("RestController Remove ignored domain {}", StringUtils.sanitizeDomain(ignoredDomain));
		dataService.removeIgnoredDomain(ignoredDomain);

		return new ResponseEntity<>(HttpStatus.OK);
	}
}
