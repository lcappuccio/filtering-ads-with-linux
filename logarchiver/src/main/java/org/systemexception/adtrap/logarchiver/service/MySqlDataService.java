package org.systemexception.adtrap.logarchiver.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.systemexception.adtrap.logarchiver.model.DnsLogLine;

import java.util.List;
import java.util.Map;

/**
 * @author leo
 * @date 05/11/2016 14:42
 */
public class MySqlDataService implements DataService {

	private static final Logger LOGGER = LoggerFactory.getLogger(MySqlDataService.class);
	private final JdbcTemplate jdbcTemplate;
	private final String ipAddress;

	@Autowired
	public MySqlDataService(JdbcTemplate jdbcTemplate, String ipAddress) {
		LOGGER.info("adtrap ip address: " + ipAddress);
		this.jdbcTemplate = jdbcTemplate;
		this.ipAddress = ipAddress;
	}


	@Override
	public DnsLogLine save(DnsLogLine dnsLogLine) {
		jdbcTemplate.update("INSERT INTO DNS_LOG_LINES (LOG_TIME, QUERY_TYPE, QUERY_DOMAIN, QUERY_TARGET) VALUES " +
						"(?, ?, ? ,?)", dnsLogLine.getDate(), dnsLogLine.getQueryType(),
				dnsLogLine.getQueryDomain(), dnsLogLine.getQueryTarget());
		return dnsLogLine;
	}

	@Override
	public int countAll() {
		return jdbcTemplate.queryForObject("SELECT count(*) FROM DNS_LOG_LINES", Integer.class);
	}

	@Override
	public int countAllFiltered() {
		return jdbcTemplate.queryForObject("SELECT count(*) FROM DNS_LOG_LINES WHERE QUERY_TARGET = ?",
				new Object[]{ipAddress}, Integer.class);
	}

	@Override
	public List<Map<String, Object>> countTopClients() {
		String query = "SELECT QUERY_TARGET, count(*) AS TOTAL FROM DNS_LOG_LINES WHERE QUERY_TYPE = ?\n" +
				"GROUP BY QUERY_TARGET ORDER BY 2 DESC  LIMIT 20";
		return jdbcTemplate.queryForList(query, new Object[]{"query[A]"});
	}

	@Override
	public List<Map<String, Object>> countTopRequests() {
		String query = "SELECT QUERY_DOMAIN, count(*) AS TOTAL FROM DNS_LOG_LINES WHERE QUERY_TYPE = ?\n" +
				"GROUP BY QUERY_DOMAIN ORDER BY 2 DESC  LIMIT 20";
		return jdbcTemplate.queryForList(query, new Object[]{"query[A]"});
	}

	@Override
	public List<Map<String, Object>> groupByQueryType() {
		String query = "SELECT QUERY_TYPE, count(*) AS TOTAL FROM DNS_LOG_LINES GROUP BY QUERY_TYPE\n"
				+ "ORDER BY 2 DESC LIMIT 20";
		return jdbcTemplate.queryForList(query);
	}

	@Override
	public List<Map<String, Object>> groupByQueryDomain() {
		String query =
				"SELECT QUERY_DOMAIN, count(*) AS TOTAL FROM DNS_LOG_LINES GROUP BY QUERY_DOMAIN\n" +
						"ORDER BY 2 DESC LIMIT 20";
		return jdbcTemplate.queryForList(query);
	}

	@Override
	public List<Map<String, Object>> groupByQueryTarget() {
		String query =
				"SELECT QUERY_TARGET, count(*) AS TOTAL FROM DNS_LOG_LINES GROUP BY QUERY_TARGET\n" +
						"ORDER BY 2 DESC LIMIT 20";
		return jdbcTemplate.queryForList(query);
	}

	@Override
	public List<Map<String, Object>> groupByFilteredDomains() {
		String query = "SELECT QUERY_DOMAIN, count(*) AS TOTAL FROM DNS_LOG_LINES\n" +
				"WHERE QUERY_TARGET = ? GROUP BY QUERY_DOMAIN ORDER BY 2 DESC LIMIT 20";
		return jdbcTemplate.queryForList(query, new Object[]{ipAddress});
	}

	/**
	 * Clean database every day of all events older than 1 month, uses cron schedule
	 */
	@Scheduled(cron = "0 0 * * * *")
	public void cleanUpDatabase() {
		long monthInMillis = System.currentTimeMillis() - (1000L * 60L * 60L * 24L * 30L);
		int deletedLines = jdbcTemplate.update("DELETE FROM DNS_LOG_LINES WHERE LOG_TIME < ?", monthInMillis);
		LOGGER.info("Scheduled database cleanup: " + deletedLines + " lines deleted");
	}

}
