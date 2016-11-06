package org.systemexception.adtrap.logarchiver.service;

import org.springframework.stereotype.Service;
import org.systemexception.adtrap.logarchiver.model.DnsLogLine;

import java.util.HashMap;

/**
 * @author leo
 * @date 03/11/2016 10:48
 */
@Service
public interface DataService {

	DnsLogLine save(DnsLogLine dnsLogLine);

	int countAll();

	HashMap countTopClients();

	HashMap countTopRequests();

	HashMap groupByQueryType();

	HashMap groupByQueryDomain();

	HashMap groupByQueryTarget();

	HashMap groupByFilteredDomains();

	int countAllFiltered();

}
