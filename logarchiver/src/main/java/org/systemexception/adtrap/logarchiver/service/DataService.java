package org.systemexception.adtrap.logarchiver.service;

import org.springframework.stereotype.Service;
import org.systemexception.adtrap.logarchiver.model.DnsLogLine;

import java.util.Map;

/**
 * @author leo
 * @date 03/11/2016 10:48
 */
@Service
public interface DataService {

	DnsLogLine save(DnsLogLine dnsLogLine);

	int countAll();

	Map<String, Integer> groupByQueryType();

}
