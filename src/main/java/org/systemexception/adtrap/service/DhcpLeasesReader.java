package org.systemexception.adtrap.service;

import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.systemexception.adtrap.model.DhcpLease;
import org.systemexception.adtrap.pojo.DhcpFileParser;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

/**
 * @author leo
 * @date 06/11/2016 22:43
 */
@Service
public class DhcpLeasesReader {

	private static final Logger LOGGER = LoggerFactory.getLogger(DhcpLeasesReader.class);
	private final File dhcpLeaseFile;
	private final String filePath;

	@Autowired
	public DhcpLeasesReader(String filePath) {
		LOGGER.info("adtrap dhcp lease file path: " + filePath);
		dhcpLeaseFile = new File(filePath);
		if (!dhcpLeaseFile.exists()) {
			String errorMessage = String.format("File %s does not exist", filePath);
			LOGGER.warn(errorMessage);
		}
		this.filePath = filePath;
	}

	/**
	 * Get a list of dhcp leases from a dnsmasq standard formatted file
	 *
	 * @return
	 */
	public List<DhcpLease> getDhcpLeases() {
		List<String> dhcpLeaseFileLines = new ArrayList<>();
		try {
			dhcpLeaseFileLines = FileUtils.readLines(dhcpLeaseFile, Charset.defaultCharset());
		} catch (IOException e) {
			LOGGER.warn(String.format("File %s does not exist", filePath));
		}
		return DhcpFileParser.parseFileContents(dhcpLeaseFileLines);
	}

}
