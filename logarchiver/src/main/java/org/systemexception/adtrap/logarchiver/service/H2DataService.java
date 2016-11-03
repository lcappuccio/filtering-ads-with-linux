package org.systemexception.adtrap.logarchiver.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.systemexception.adtrap.logarchiver.model.DnsLogLine;
import org.systemexception.adtrap.logarchiver.repositories.DnsLogLineRepository;

import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * @author leo
 * @date 07/10/2016 18:49
 */
@Service
@Transactional
public class H2DataService implements DataService {

	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	private final DnsLogLineRepository dataRepository;
	private final static String DATETIME_FORMAT = "yyyy/MM/dd HH:mm:ss";

	/**
	 * {@inheritDoc}
	 */
	@Autowired
	public H2DataService(DnsLogLineRepository dataRepository) {
		this.dataRepository = dataRepository;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public DnsLogLine save(DnsLogLine dnsLogLine) {
		logger.info("Save data: " + dnsLogLine.toString());
		return dataRepository.save(dnsLogLine);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public DnsLogLine findOne(Long logId) {
		logger.info("Find id: " + logId);
		return dataRepository.findOne(logId);
	}


	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<DnsLogLine> findAll() {
		logger.info("Find all");
		return dataRepository.findAll();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<DnsLogLine> findByDate(Date date) {
		logger.info("Find by date: " + convertTime(date));
		return dataRepository.findByDate(date);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<DnsLogLine> findByQueryType(String queryType) {
		logger.info("Find by query type: " + queryType);
		return dataRepository.findByQueryType(queryType);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<DnsLogLine> findByQueryDomain(String domain) {
		logger.info("Find by domain: " + domain);
		return dataRepository.findByQueryDomain(domain);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<DnsLogLine> findByQueryTarget(String queryTarget) {
		logger.info("Find by target: " + queryTarget);
		return dataRepository.findByQueryTarget(queryTarget);
	}

	/**
	 * Convert from date to a string formatted type
	 *
	 * @param date the date to convert
	 * @return the formatted value
	 */
	private String convertTime(Date date) {
		Format format = new SimpleDateFormat(DATETIME_FORMAT, Locale.getDefault());
		return format.format(date);
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
