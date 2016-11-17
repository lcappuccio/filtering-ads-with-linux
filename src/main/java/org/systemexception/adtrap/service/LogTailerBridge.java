package org.systemexception.adtrap.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.systemexception.adtrap.model.DnsLogLine;
import org.systemexception.adtrap.pojo.JsonMapper;
import org.systemexception.adtrap.pojo.LogQueue;

import java.text.ParseException;
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
		this.logQueue = logQueue;
		this.dataService = dataService;
	}

	/**
	 * Posts data taken from the queue
	 */
	@Scheduled(cron = "* * * * * *")
	private void postData() throws ParseException, InterruptedException {
		int queueSize = logQueue.size();
		for (int i = 0; i < queueSize; i++) {
			String queueItem = (String) logQueue.take();
			Optional<DnsLogLine> dnsLogLine = jsonMapper.dnsLogLineFromLogLine(queueItem);
			if (dnsLogLine.isPresent()) {
				dataService.save(dnsLogLine.get());
			} else {
				LOGGER.info("Bad line caught, skipped: " + queueItem);
			}
		}
	}
}
