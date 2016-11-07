package org.systemexception.adtrap.logarchiver.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.systemexception.adtrap.logarchiver.model.DnsLogLine;
import org.systemexception.adtrap.logarchiver.pojo.Queries;

import java.util.List;
import java.util.Map;

/**
 * @author leo
 * @date 05/11/2016 14:42
 */
public class MySqlDataService implements DataService {

	private static final Logger LOGGER = LoggerFactory.getLogger(MySqlDataService.class);
	private final JdbcTemplate jdbcTemplate;
	private final String ipAddress, homeDomain;

	@Autowired
	public MySqlDataService(JdbcTemplate jdbcTemplate, String ipAddress, String homeDomain) {
		LOGGER.info("adtrap ip address: " + ipAddress);
		this.jdbcTemplate = jdbcTemplate;
		this.ipAddress = ipAddress;
		this.homeDomain = homeDomain;
	}


	@Override
	public DnsLogLine save(DnsLogLine dnsLogLine) {
		jdbcTemplate.update(Queries.SAVE_QUERY, dnsLogLine.getDate(), dnsLogLine.getQueryType(),
				dnsLogLine.getQueryDomain(), dnsLogLine.getQueryTarget());
		return dnsLogLine;
	}

	@Override
	public int countAll() {
		return jdbcTemplate.queryForObject(Queries.COUNT_ALL, Integer.class);
	}

	@Override
	public int countAllFiltered() {
		return jdbcTemplate.queryForObject(Queries.COUNT_ALL_FILTERED, new Object[]{ipAddress}, Integer.class);
	}

	@Override
	public List<Map<String, Object>> countTopClients() {
		return jdbcTemplate.queryForList(Queries.COUNT_TOP_CLIENTS, new Object[]{"query[A]"});
	}

	@Override
	public List<Map<String, Object>> countTopRequests() {
		return jdbcTemplate.queryForList(Queries.COUNT_TOP_REQUESTS, new Object[]{"query[A]", "%" + homeDomain + "%"});
	}

	@Override
	public List<Map<String, Object>> groupByQueryType() {
		return jdbcTemplate.queryForList(Queries.GROUP_BY_QUERY_TYPE);
	}

	@Override
	public List<Map<String, Object>> groupByQueryDomain() {
		return jdbcTemplate.queryForList(Queries.GROUP_BY_QUERY_DOMAIN);
	}

	@Override
	public List<Map<String, Object>> groupByQueryTarget() {
		return jdbcTemplate.queryForList(Queries.GROUP_BY_QUERY_TARGET);
	}

	@Override
	public List<Map<String, Object>> groupByFilteredDomains() {
		return jdbcTemplate.queryForList(Queries.GROUP_BY_FILTERED_DOMAINS, new Object[]{ipAddress});
	}

	@Override
	public List<Map<String, Object>> dailyByHour() {
		return jdbcTemplate.queryForList(Queries.WEEKLY_BY_HOUR, new Object[]{ipAddress});
	}

	@Override
	public List<Map<String, Object>> monthlyByDay() {
		return jdbcTemplate.queryForList(Queries.MONTHLY_BY_DAY, new Object[]{ipAddress});
	}

	/**
	 * Clean database every day of all events older than 1 month, uses cron schedule
	 */
	@Scheduled(cron = "0 0 * * * *")
	public void cleanUpDatabase() {
		long monthInMillis = System.currentTimeMillis() - (1000L * 60L * 60L * 24L * 30L);
		int deletedLines = jdbcTemplate.update(Queries.CLEANUP, monthInMillis);
		LOGGER.info("Scheduled database cleanup: " + deletedLines + " lines deleted");
	}

}
