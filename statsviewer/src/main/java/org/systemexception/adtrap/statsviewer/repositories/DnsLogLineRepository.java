package org.systemexception.adtrap.statsviewer.repositories;

import org.springframework.stereotype.Repository;
import org.systemexception.adtrap.statsviewer.model.DnsLogLine;

import java.util.Date;
import java.util.List;

/**
 * @author leo
 * @date 07/10/2016 18:34
 */
@Repository
public interface DnsLogLineRepository {

	/**
	 * Save data to the database
	 *
	 * @param dnsLogLine the data to be saved
	 * @return the data saved
	 */
	DnsLogLine save(DnsLogLine dnsLogLine);

	/**
	 * @param logId
	 * @return a single Dns Log line
	 */
	DnsLogLine findOne(Long logId);

	/**
	 * Returns all queries on specified date
	 *
	 * @param date
	 * @return a list of Dns Log lines
	 */
	List<DnsLogLine> findByDate(Date date);

	/**
	 * Returns all queries by type
	 *
	 * @param queryType https://en.wikipedia.org/wiki/List_of_DNS_record_types
	 * @return a list of Dns Log lines
	 */
	List<DnsLogLine> findByQueryType(String queryType);

	/**
	 * Returns all queries by requester
	 *
	 * @param requester the requester ip address
	 * @return a list of Dns Log lines
	 */
	List<DnsLogLine> findByQueryRequester(String requester);

	/**
	 * Returns all queries by domain (from or to)
	 *
	 * @param fromOrTo the domain in the dns request
	 * @return a list of Dns Log lines
	 */
	List<DnsLogLine> findByQueryFromOrTo(String fromOrTo);

}
