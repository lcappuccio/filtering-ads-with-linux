package org.systemexception.adtrap.logarchiver.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.scheduling.annotation.Scheduled;
import org.systemexception.adtrap.logarchiver.model.DnsLogLine;

import java.util.HashMap;
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
		this.jdbcTemplate = jdbcTemplate;
		this.ipAddress = ipAddress;
		LOGGER.info("Starting DB service on " + ipAddress);
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
	public HashMap countTopClients() {
		String query = "SELECT QUERY_TARGET, count(*) AS TOTAL FROM DNS_LOG_LINES WHERE QUERY_TYPE = ?\n" +
				"GROUP BY QUERY_TARGET ORDER BY 2 DESC";
		ResultSetExtractor mapExtractor = getResultSetExtractor("QUERY_TARGET");
		return (HashMap) jdbcTemplate.query(query, new Object[]{"query[A]"}, mapExtractor);
	}

	@Override
	public HashMap countTopRequests() {
		String query = "SELECT QUERY_DOMAIN, count(*) AS TOTAL FROM DNS_LOG_LINES WHERE QUERY_TYPE = ?\n" +
				"GROUP BY QUERY_DOMAIN ORDER BY 2 DESC";
		ResultSetExtractor mapExtractor = getResultSetExtractor("QUERY_DOMAIN");
		return (HashMap) jdbcTemplate.query(query, new Object[]{"query[A]"}, mapExtractor);
	}

	@Override
	public HashMap groupByQueryType() {
		String query = "SELECT QUERY_TYPE, count(*) AS TOTAL FROM DNS_LOG_LINES GROUP BY QUERY_TYPE ORDER BY 2 DESC";
		ResultSetExtractor mapExtractor = getResultSetExtractor("QUERY_TYPE");
		return (HashMap) jdbcTemplate.query(query, mapExtractor);
	}

	@Override
	public HashMap groupByQueryDomain() {
		String query =
				"SELECT QUERY_DOMAIN, count(*) AS TOTAL FROM DNS_LOG_LINES GROUP BY QUERY_DOMAIN ORDER BY 2 DESC";
		ResultSetExtractor mapExtractor = getResultSetExtractor("QUERY_DOMAIN");
		return (HashMap) jdbcTemplate.query(query, mapExtractor);
	}

	@Override
	public HashMap groupByQueryTarget() {
		String query =
				"SELECT QUERY_TARGET, count(*) AS TOTAL FROM DNS_LOG_LINES GROUP BY QUERY_TARGET ORDER BY 2 DESC";
		ResultSetExtractor mapExtractor = getResultSetExtractor("QUERY_TARGET");
		return (HashMap) jdbcTemplate.query(query, mapExtractor);
	}

	@Override
	public HashMap groupByFilteredDomains() {
		String query = "SELECT QUERY_DOMAIN, count(*) AS TOTAL FROM DNS_LOG_LINES\n" +
				"WHERE QUERY_TARGET = ? GROUP BY QUERY_DOMAIN ORDER BY 2 DESC";
		ResultSetExtractor mapExtractor = getResultSetExtractor("QUERY_DOMAIN");
		return (HashMap) jdbcTemplate.query(query, new Object[]{ipAddress}, mapExtractor);
	}

	@Override
	public int countAllFiltered() {
		return jdbcTemplate.queryForObject("SELECT count(*) FROM DNS_LOG_LINES WHERE QUERY_TARGET = ?",
				new Object[]{ipAddress}, Integer.class);
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

	/**
	 * Builds a result set extractor for Map<String, Integer> types
	 *
	 * @param queryTarget
	 * @return
	 */
	private ResultSetExtractor getResultSetExtractor(String queryTarget) {
		return rs -> {
			Map<String, Integer> mapOfKeys = new HashMap<>();
			while (rs.next()) {
				String key = rs.getString(queryTarget);
				Integer count = rs.getInt("TOTAL");
				mapOfKeys.put(key, count);
			}
			return mapOfKeys;
		};
	}

}
