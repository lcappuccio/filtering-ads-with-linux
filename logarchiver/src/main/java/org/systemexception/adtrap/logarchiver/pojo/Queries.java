package org.systemexception.adtrap.logarchiver.pojo;

/**
 * @author leo
 * @date 07/11/2016 12:59
 */
public class Queries {

	// SAVE
	public static final String SAVE_QUERY = "INSERT INTO DNS_LOG_LINES " +
			"(LOG_TIME, QUERY_TYPE, QUERY_DOMAIN, QUERY_TARGET) VALUES (?, ?, ? ,?)";

	// COUNT
	public static final String COUNT_ALL = "SELECT count(*) FROM DNS_LOG_LINES";
	public static final String COUNT_ALL_FILTERED = "SELECT count(*) FROM DNS_LOG_LINES WHERE QUERY_TARGET = ?";
	public static final String COUNT_TOP_CLIENTS = "SELECT QUERY_TARGET, count(*) AS TOTAL FROM DNS_LOG_LINES " +
			"WHERE QUERY_TYPE = ? GROUP BY QUERY_TARGET ORDER BY 2 DESC  LIMIT 20";
	public static final String COUNT_TOP_REQUESTS = "SELECT QUERY_DOMAIN, count(*) AS TOTAL FROM DNS_LOG_LINES " +
			"WHERE QUERY_TYPE = ? AND QUERY_DOMAIN NOT LIKE ? GROUP BY QUERY_DOMAIN ORDER BY 2 " +
			"DESC LIMIT 20";

	// GROUP BY
	public static final String GROUP_BY_QUERY_TYPE = "SELECT QUERY_TYPE, count(*) AS TOTAL FROM DNS_LOG_LINES " +
			"GROUP BY QUERY_TYPE ORDER BY 2 DESC LIMIT 20";
	public static final String GROUP_BY_QUERY_DOMAIN = "SELECT QUERY_DOMAIN, count(*) AS TOTAL FROM DNS_LOG_LINES " +
			"GROUP BY QUERY_DOMAIN ORDER BY 2 DESC LIMIT 20";
	public static final String GROUP_BY_QUERY_TARGET = "SELECT QUERY_TARGET, count(*) AS TOTAL FROM DNS_LOG_LINES " +
			"GROUP BY QUERY_TARGET ORDER BY 2 DESC LIMIT 20";
	public static final String GROUP_BY_FILTERED_DOMAINS = "SELECT QUERY_DOMAIN, count(*) AS TOTAL " +
			"FROM DNS_LOG_LINES WHERE QUERY_TARGET = ? GROUP BY QUERY_DOMAIN ORDER BY 2 DESC LIMIT 20";

	// STATISTICS
	public static final String WEEKLY_BY_HOUR = "select FROM_UNIXTIME(LOG_TIME/1000, '%d/%m/%Y %H') as LOG_DATE, " +
			"count(*) as TOTAL from DNS_LOG_LINES where QUERY_TARGET = ?\n" +
			"and STR_TO_DATE(FROM_UNIXTIME(LOG_TIME/1000, '%d/%m/%Y'), '%d/%m/%Y') > CURRENT_DATE - INTERVAL 7 DAY\n" +
			"group by FROM_UNIXTIME(LOG_TIME/1000, '%d/%m/%Y %H') order by 1 asc";
	public static final String MONTHLY_BY_DAY = "select FROM_UNIXTIME(LOG_TIME/1000, '%d/%m/%Y') as LOG_DATE, count(*)" +
			" as TOTAL from DNS_LOG_LINES where QUERY_TARGET = ?\n" +
			"and STR_TO_DATE(FROM_UNIXTIME(LOG_TIME/1000, '%d/%m/%Y'), '%d/%m/%Y') > CURRENT_DATE - INTERVAL 30 DAY\n" +
			"group by FROM_UNIXTIME(LOG_TIME/1000, '%d/%m/%Y') order by 1 asc";

	// CLEANUP
	public static final String CLEANUP = "DELETE FROM DNS_LOG_LINES WHERE LOG_TIME < ?";
}
