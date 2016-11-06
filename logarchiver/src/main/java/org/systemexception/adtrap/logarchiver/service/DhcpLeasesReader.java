package org.systemexception.adtrap.logarchiver.service;

import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.systemexception.adtrap.logarchiver.model.DhcpLease;
import org.systemexception.adtrap.logarchiver.pojo.DhcpFileParser;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.charset.Charset;
import java.security.InvalidParameterException;
import java.util.List;

/**
 * @author leo
 * @date 06/11/2016 22:43
 */
@Service
public class DhcpLeasesReader {

	private static final Logger LOGGER = LoggerFactory.getLogger(DhcpLeasesReader.class);
	private final File dhcpLeaseFile;

	public DhcpLeasesReader(String filePath) throws URISyntaxException {
		URL dhcpLeaseFileUrl = ClassLoader.getSystemResource(filePath);
		dhcpLeaseFile = new File(dhcpLeaseFileUrl.toURI());
		if (!dhcpLeaseFile.exists()) {
			String errorMessage = String.format("File %s does not exist", filePath);
			LOGGER.error(errorMessage);
			throw new InvalidParameterException(errorMessage);
		}
	}

	/**
	 * Get a list of dhcp leases from a dnsmasq standard formatted file
	 *
	 * @return
	 * @throws URISyntaxException
	 * @throws IOException
	 */
	public List<DhcpLease> getDhcpLeases() throws IOException {
		List<String> dhcpLeaseFileLines = FileUtils.readLines(dhcpLeaseFile, Charset.defaultCharset());
		return DhcpFileParser.parseFileContents(dhcpLeaseFileLines);
	}

}
