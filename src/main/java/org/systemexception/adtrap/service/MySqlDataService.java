package org.systemexception.adtrap.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.systemexception.adtrap.model.DnsLogLine;
import org.systemexception.adtrap.pojo.Queries;

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
		LOGGER.info("Received: " + dnsLogLine.toString());
		jdbcTemplate.update(Queries.SAVE_QUERY, dnsLogLine.getDate(), dnsLogLine.getQueryType(),
				dnsLogLine.getQueryDomain(), dnsLogLine.getQueryTarget());
		return dnsLogLine;
	}

	@Override
	public int countAll() {
		LOGGER.debug("Count all");
		return jdbcTemplate.queryForObject(Queries.COUNT_ALL, Integer.class);
	}

	@Override
	public int countAllFiltered() {
		LOGGER.debug("Count all filtered");
		return jdbcTemplate.queryForObject(Queries.COUNT_ALL_FILTERED, new Object[]{ipAddress}, Integer.class);
	}

	@Override
	public int countDistinctAdvertisersFiltered() {
		LOGGER.debug("Count distinct advertisers filtered");
		return jdbcTemplate.queryForObject(Queries.COUNT_DISTINCT_ADVERTISERS, new Object[]{ipAddress}, Integer.class);
	}

	@Override
	public List<Map<String, Object>> countTopClients() {
		LOGGER.debug("Count top clients");
		return jdbcTemplate.queryForList(Queries.COUNT_TOP_CLIENTS, "query[A]");
	}

	@Override
	public List<Map<String, Object>> countTopRequests() {
		LOGGER.debug("Count top requests");
		return jdbcTemplate.queryForList(Queries.COUNT_TOP_REQUESTS, "query[A]", "%" + homeDomain + "%");
	}

	@Override
	public List<Map<String, Object>> groupByQueryType() {
		LOGGER.debug("Group by query type");
		return jdbcTemplate.queryForList(Queries.GROUP_BY_QUERY_TYPE);
	}

	@Override
	public List<Map<String, Object>> groupByQueryDomain() {
		LOGGER.debug("Group by query domain");
		return jdbcTemplate.queryForList(Queries.GROUP_BY_QUERY_DOMAIN);
	}

	@Override
	public List<Map<String, Object>> groupByQueryTarget() {
		LOGGER.debug("Group by query target");
		return jdbcTemplate.queryForList(Queries.GROUP_BY_QUERY_TARGET);
	}

	@Override
	public List<Map<String, Object>> groupByFilteredDomains() {
		LOGGER.debug("Group by query filtered domains");
		return jdbcTemplate.queryForList(Queries.GROUP_BY_FILTERED_DOMAINS, ipAddress);
	}

	@Override
	public List<Map<String, Object>> dailyByHour() {
		LOGGER.debug("Get statistics by hour");
		return jdbcTemplate.queryForList(Queries.WEEKLY_BY_HOUR, ipAddress);
	}

	@Override
	public List<Map<String, Object>> monthlyByDay() {
		LOGGER.debug("Get statistics by day");
		return jdbcTemplate.queryForList(Queries.MONTHLY_BY_DAY, ipAddress);
	}

	@Override
	public List<Map<String, Object>> getIgnoredDomains() {
		LOGGER.debug("Refreshing ignored domain list");
		return jdbcTemplate.queryForList(Queries.GET_IGNORED_DOMAINS);
	}

	@Override
	public void addIgnoredDomain(final String ignoredDomain) {
		LOGGER.debug(String.format("Add ignored domain %s", ignoredDomain));
		jdbcTemplate.update(Queries.SAVE_IGNORED_DOMAIN, ignoredDomain);
	}

	@Override
	public void removeIgnoredDomain(final String ignoredDomain) {
		LOGGER.debug(String.format("Delete ignored domain %s", ignoredDomain));
		jdbcTemplate.update(Queries.DELETE_IGNORED_DOMAIN, ignoredDomain);
	}

	/**
	 * Clean database every day of all events older than 1 month, uses cron schedule
	 */
	@Scheduled(cron = "0 0 * * * *")
	public void cleanUpDatabase() {
		long dayInMills = 1000L * 60L * 60L * 24L;
		long daysBack = 30L;
		long monthInMillis = System.currentTimeMillis() - (dayInMills * daysBack);
		int deletedLines = jdbcTemplate.update(Queries.CLEANUP, monthInMillis);
		LOGGER.info("Scheduled database cleanup: " + deletedLines + " lines deleted");
	}

}
