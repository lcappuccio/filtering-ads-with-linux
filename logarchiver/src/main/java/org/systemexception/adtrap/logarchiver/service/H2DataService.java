package org.systemexception.adtrap.logarchiver.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.systemexception.adtrap.logarchiver.model.DnsLogLine;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

/**
 * @author leo
 * @date 05/11/2016 14:42
 */
@Component
public class H2DataService implements DataService {

	private static final Logger LOGGER = LoggerFactory.getLogger(H2DataService.class);
	private final JdbcTemplate jdbcTemplate;

	@Autowired
	public H2DataService(JdbcTemplate jdbcTemplate) throws SQLException {

		this.jdbcTemplate = jdbcTemplate;
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
	public HashMap groupByQueryType() {
		String query = "SELECT QUERY_TYPE, count(*) AS TOTAL FROM DNS_LOG_LINES GROUP BY QUERY_TYPE ORDER BY 2 DESC";

		ResultSetExtractor mapExtractor = rs -> {
			Map<String, Integer> mapOfKeys = new HashMap<>();
			while (rs.next()) {
				String key = rs.getString("QUERY_TYPE");
				Integer count = rs.getInt("TOTAL");
				mapOfKeys.put(key, count);
			}
			return mapOfKeys;
		};

		return (HashMap) jdbcTemplate.query(query, mapExtractor);
	}

	@Override
	public HashMap groupByQueryDomain() {
		String query = "SELECT QUERY_DOMAIN, count(*) AS TOTAL FROM DNS_LOG_LINES GROUP BY QUERY_DOMAIN ORDER BY 2 DESC";

		ResultSetExtractor mapExtractor = rs -> {
			Map<String, Integer> mapOfKeys = new HashMap<>();
			while (rs.next()) {
				String key = rs.getString("QUERY_DOMAIN");
				Integer count = rs.getInt("TOTAL");
				mapOfKeys.put(key, count);
			}
			return mapOfKeys;
		};

		return (HashMap) jdbcTemplate.query(query, mapExtractor);
	}

	@Override
	public HashMap groupByQueryTarget() {
		String query = "SELECT QUERY_TARGET, count(*) AS TOTAL FROM DNS_LOG_LINES GROUP BY QUERY_TARGET ORDER BY 2 DESC";

		ResultSetExtractor mapExtractor = rs -> {
			Map<String, Integer> mapOfKeys = new HashMap<>();
			while (rs.next()) {
				String key = rs.getString("QUERY_TARGET");
				Integer count = rs.getInt("TOTAL");
				mapOfKeys.put(key, count);
			}
			return mapOfKeys;
		};

		return (HashMap) jdbcTemplate.query(query, mapExtractor);
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
