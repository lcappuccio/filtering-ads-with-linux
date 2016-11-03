package org.systemexception.adtrap.logarchiver.service;

import org.springframework.stereotype.Service;
import org.systemexception.adtrap.logarchiver.model.DnsLogLine;

import java.util.Date;
import java.util.List;

/**
 * @author leo
 * @date 03/11/2016 10:48
 */
@Service
public interface DataService {

	DnsLogLine save(DnsLogLine dnsLogLine);

	DnsLogLine findOne(Long logId);

	List<DnsLogLine> findAll();

	List<DnsLogLine> findByDate(Date date);

	List<DnsLogLine> findByQueryType(String queryType);

	List<DnsLogLine> findByQueryDomain(String domain);

	List<DnsLogLine> findByQueryTarget(String queryTarget);

}
