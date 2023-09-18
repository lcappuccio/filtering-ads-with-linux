package org.systemexception.adtrap.service;

import org.systemexception.adtrap.model.DnsLogLine;

import java.util.List;
import java.util.Map;

/**
 * @author leo
 * @date 03/11/2016 10:48
 */
public interface DataService {

	/**
	 * Insert a dns log line into the database
	 *
	 * @param dnsLogLine
	 * @return
	 */
	DnsLogLine save(DnsLogLine dnsLogLine);

	/**
	 * Count all entries
	 *
	 * @return
	 */
	Integer countAll();

	/**
	 * Count all entries that were filtered
	 *
	 * @return
	 */
    Integer countAllFiltered();

	/**
	 * Count distinct advertisers
	 *
	 * @return
	 */
    Integer countDistinctAdvertisersFiltered();

	/**
	 * Count top dns clients
	 *
	 * @return
	 */
	List<Map<String, Object>> countTopClients();

	/**
	 * Count top requested domains
	 *
	 * @return
	 */
	List<Map<String, Object>> countTopRequests();

	/**
	 * Group by dns query type
	 *
	 * @return
	 */
	List<Map<String, Object>> groupByQueryType();

	/**
	 * Group by queried domain
	 *
	 * @return
	 */
	List<Map<String, Object>> groupByQueryDomain();

	/**
	 * Group by query target
	 *
	 * @return
	 */
	List<Map<String, Object>> groupByQueryTarget();

	/**
	 * Group by filtered domain
	 *
	 * @return
	 */
	List<Map<String, Object>> groupByFilteredDomains();

	/**
	 * Daily statistic by hour
	 *
	 * @return
	 */
	List<Map<String, Object>> dailyByHour();

	/**
	 * Monthly statistic by day
	 *
	 * @return
	 */
	List<Map<String, Object>> monthlyByDay();

	/**
	 * Get list of ignored domains
	 * @return
	 */
	List<Map<String, Object>> getIgnoredDomains();

	/**
	 * Add a new entry to the ignored domain list
	 */
	void addIgnoredDomain(String ignoredDomain);

	/**
	 * Remove an entry from the ignored domain list
	 */
	void removeIgnoredDomain(String ignoredDomain);

}
