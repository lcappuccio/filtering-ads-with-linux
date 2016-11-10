package org.systemexception.adtrap.service;

import org.springframework.stereotype.Service;
import org.systemexception.adtrap.model.DnsLogLine;

import java.util.List;
import java.util.Map;

/**
 * @author leo
 * @date 03/11/2016 10:48
 */
@Service
public interface DataService {

	DnsLogLine save(DnsLogLine dnsLogLine);

	int countAll();

	int countAllFiltered();

	List<Map<String, Object>> countTopClients();

	List<Map<String, Object>> countTopRequests();

	List<Map<String, Object>> groupByQueryType();

	List<Map<String, Object>> groupByQueryDomain();

	List<Map<String, Object>> groupByQueryTarget();

	List<Map<String, Object>> groupByFilteredDomains();

	List<Map<String, Object>> dailyByHour();

	List<Map<String, Object>> monthlyByDay();

}
