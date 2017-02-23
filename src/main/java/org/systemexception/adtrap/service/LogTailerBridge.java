package org.systemexception.adtrap.service;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.systemexception.adtrap.model.DnsLogLine;
import org.systemexception.adtrap.pojo.JsonMapper;
import org.systemexception.adtrap.pojo.LogQueue;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * @author leo
 */
@Service
public class LogTailerBridge {

	private static final Logger LOGGER = LoggerFactory.getLogger(LogTailerBridge.class);
	private final DataService dataService;
	private final LogQueue logQueue;
	private final JsonMapper jsonMapper = new JsonMapper();

	@Autowired
	public LogTailerBridge(DataService dataService, final LogQueue logQueue) {
		this.dataService = dataService;
		this.logQueue = logQueue;
	}

	/**
	 * Posts data taken from the queue
	 */
	@Scheduled(cron = "* * * * * *")
	public synchronized void postData() throws ParseException, InterruptedException {

		// TODO LC refresh ignore list here

		int queueSize = logQueue.size();
		for (int i = 0; i < queueSize; i++) {
			String queueItem = (String) logQueue.take();
			Optional<DnsLogLine> dnsLogLine = jsonMapper.dnsLogLineFromLogLine(queueItem);
			if (dnsLogLine.isPresent() && !isDomainIgnored(dnsLogLine.get())) {
				dataService.save(dnsLogLine.get());
			} else {
				LOGGER.warn("Bad line caught, skipped: " + queueItem);
			}
		}
	}

	/**
	 * Check if logline contains a domain in ignore list
	 *
	 * @param dnsLogLine {@link org.systemexception.adtrap.model.DnsLogLine}
	 * @return true if the domain is in the ignore list
	 */
	private boolean isDomainIgnored(DnsLogLine dnsLogLine) {
		List<String> ignoreList = getIgnoredDomainList();
		for (String ignoredDomain : ignoreList) {
			if (StringUtils.containsIgnoreCase(dnsLogLine.getQueryDomain(), ignoredDomain)) {
				LOGGER.info("Ignored domain: " + dnsLogLine.getQueryDomain());
				return true;
			}
		}
		return false;
	}

	/**
	 * Fetch the ignored domain list from the database
	 *
	 * @return
	 */
	private List<String> getIgnoredDomainList() {
		List<String> ignoredDomainList = new ArrayList<>();
		List<Map<String, Object>> ignoredDomains = dataService.getIgnoredDomains();

		for (Map<String, Object> entry : ignoredDomains) {
			ignoredDomainList.add(String.valueOf(entry.get("IGNORE_DOMAIN")));
		}

		return ignoredDomainList;
	}
}
