package org.systemexception.adtrap.pojo;

/**
 * @author leo
 * @date 07/11/2016 12:59
 */
public class Queries {

    private Queries() {
    }

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
	public static final String COUNT_DISTINCT_ADVERTISERS = "select count(distinct(QUERY_DOMAIN)) as TOTAL " +
			"from DNS_LOG_LINES where QUERY_TARGET = ?";

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
	public static final String WEEKLY_BY_HOUR = "select LOG_DATE, TOTAL from (" +
			"select FROM_UNIXTIME(LOG_TIME/1000, '%a %d %H:59') as LOG_DATE, LOG_TIME, count(*) as TOTAL " +
			"from DNS_LOG_LINES where QUERY_TARGET = ? and UNIX_TIMESTAMP() - LOG_TIME/1000 > 3600 " +
			"group by FROM_UNIXTIME(LOG_TIME/1000, '%d%m%H') order by LOG_TIME desc limit 24) data " +
			"order by LOG_TIME asc";
	public static final String MONTHLY_BY_DAY = "select FROM_UNIXTIME(LOG_TIME/1000, '%a %d/%m') as LOG_DATE, " +
			"count(*) as TOTAL from DNS_LOG_LINES where QUERY_TARGET = ? " +
			"and STR_TO_DATE(FROM_UNIXTIME(LOG_TIME/1000, '%d/%m/%Y'), '%d/%m/%Y') " +
			"BETWEEN NOW() - INTERVAL 31 DAY AND NOW() - INTERVAL 1 DAY " +
			"group by FROM_UNIXTIME(LOG_TIME/1000, '%a%d%m') " +
			"order by LOG_TIME asc";

	// CLEANUP
	public static final String CLEANUP = "DELETE FROM DNS_LOG_LINES WHERE LOG_TIME < ?";

	/**
	 * IGNORE LIST
	 */

	// Save ignored domain
	public static final String SAVE_IGNORED_DOMAIN = "INSERT INTO DNS_IGNORE (IGNORE_DOMAIN) VALUES (?)";

	// Remove ignored domain
	public static final String DELETE_IGNORED_DOMAIN = "DELETE FROM DNS_IGNORE WHERE IGNORE_DOMAIN = ?";

	// Get ignored domains list
	public static final String GET_IGNORED_DOMAINS = "SELECT * FROM DNS_IGNORE ORDER BY 1 DESC";
}
