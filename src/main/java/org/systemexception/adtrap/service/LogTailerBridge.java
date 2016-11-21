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
import java.util.List;
import java.util.Optional;

/**
 * @author leo
 */
@Service
public class LogTailerBridge {

	private static final Logger LOGGER = LoggerFactory.getLogger(LogTailerBridge.class);
	private final List<String> ignoreList;
	private final DataService dataService;
	private final LogQueue logQueue;
	private final JsonMapper jsonMapper = new JsonMapper();

	@Autowired
	public LogTailerBridge(DataService dataService, final LogQueue logQueue, final List<String> ignoreList) {
		LOGGER.info("Ignoring domains: " + ignoreList);
		this.ignoreList = ignoreList;
		this.dataService = dataService;
		this.logQueue = logQueue;
	}

	/**
	 * Posts data taken from the queue
	 */
	@Scheduled(cron = "* * * * * *")
	public synchronized void postData() throws ParseException, InterruptedException {
		int queueSize = logQueue.size();
		for (int i = 0; i < queueSize; i++) {
			String queueItem = (String) logQueue.take();
			Optional<DnsLogLine> dnsLogLine = jsonMapper.dnsLogLineFromLogLine(queueItem);
			if (dnsLogLine.isPresent() && !isDomainIgnored(dnsLogLine.get())) {
				dataService.save(dnsLogLine.get());
			} else {
				LOGGER.info("Bad line caught, skipped: " + queueItem);
			}
		}
	}

	/**
	 * Check if logline contains a domain in ignore list
	 *
	 * @param dnsLogLine
	 * @return
	 */
	private boolean isDomainIgnored(DnsLogLine dnsLogLine) {
		for (String ignoredDomain : ignoreList) {
			if (StringUtils.containsIgnoreCase(dnsLogLine.getQueryDomain(), ignoredDomain)) {
				LOGGER.info("Ignored domain: " + dnsLogLine.getQueryDomain());
				return true;
			}
		}
		return false;
	}
}
