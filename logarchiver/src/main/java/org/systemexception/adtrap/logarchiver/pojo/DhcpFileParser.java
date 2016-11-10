package org.systemexception.adtrap.logarchiver.pojo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.systemexception.adtrap.logarchiver.model.DhcpLease;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author leo
 * @date 06/11/2016 23:18
 */
public class DhcpFileParser {

	private static final Logger LOGGER = LoggerFactory.getLogger(DhcpFileParser.class);
	private static final String RECORD_SEPARATOR = " ";
	private static final int DATE_POSITION = 0;
	private static final int HOSTNAME_POSITION = 3;
	private static final int IP_ADDRESS_POSITION = 2;
	private static final int MAC_ADDRESS_POSITION = 1;

	public static List<DhcpLease> parseFileContents(List<String> fileLines) {
		List<DhcpLease> dhcpLeases = new ArrayList<>();
		for (String fileLine : fileLines) {
			String[] split = fileLine.split(RECORD_SEPARATOR);
			DhcpLease dhcpLease = new DhcpLease();
			dhcpLease.setLeaseExpireDate(Long.parseLong(split[DATE_POSITION]));
			dhcpLease.setMacAddress(split[MAC_ADDRESS_POSITION]);
			dhcpLease.setIpAddress(split[IP_ADDRESS_POSITION]);
			dhcpLease.setHostname(split[HOSTNAME_POSITION]);
			dhcpLease.setVendor(getVendor(split[MAC_ADDRESS_POSITION]));
			dhcpLeases.add(dhcpLease);
		}
		Collections.sort(dhcpLeases, (o1, o2) -> {
			if (!o1.getHostname().equals(o2.getHostname())) {
				return o1.getHostname().compareToIgnoreCase(o2.getHostname());
			} else {
				return o1.getIpAddress().compareTo(o2.getIpAddress());
			}
		});
		return dhcpLeases;
	}

	private static String getVendor(String macAddress) {
		RestTemplate restTemplate = new RestTemplate();
		try {
			return restTemplate.getForObject("http://api.macvendors.com/" + macAddress, String.class);
		} catch (RestClientException e) {
			LOGGER.error("Error in mac address api");
			return "N/A";
		}
	}
}
