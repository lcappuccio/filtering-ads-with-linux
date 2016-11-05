package org.systemexception.adtrap.logarchiver.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.systemexception.adtrap.logarchiver.model.DnsLogLine;
import org.systemexception.adtrap.logarchiver.repositories.DnsLogLineRepository;

import java.util.Map;

/**
 * @author leo
 * @date 07/10/2016 18:49
 */
@Service
@Transactional
public class H2DataService implements DataService {

	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	private final DnsLogLineRepository dataRepository;

	@Autowired
	public H2DataService(DnsLogLineRepository dataRepository) {
		this.dataRepository = dataRepository;
	}

	@Override
	public DnsLogLine save(DnsLogLine dnsLogLine) {
		return dataRepository.save(dnsLogLine);
	}

	@Override
	public int countAll() {
		return dataRepository.countAll();
	}

	@Override
	public Map<String, Integer> groupByQueryType() {
		return dataRepository.groupByQueryType();
	}

	/**
	 * Clean database every day of all events older than 1 month, uses cron schedule
	 */
	@Scheduled(cron = "0 0 * * * *")
	public void cleanUpDatabase() {
		logger.info("Scheduled database cleanup");
		long monthInMillis = System.currentTimeMillis() * 1000L * 60L * 60L * 24L * 30L;
		dataRepository.cleanUp(monthInMillis);
	}
}
