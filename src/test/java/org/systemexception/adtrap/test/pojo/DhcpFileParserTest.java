package org.systemexception.adtrap.test.pojo;

import org.apache.commons.io.FileUtils;
import org.junit.Test;
import org.systemexception.adtrap.model.DhcpLease;
import org.systemexception.adtrap.pojo.DhcpFileParser;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.charset.Charset;
import java.util.List;

import static junit.framework.TestCase.assertEquals;

/**
 * @author leo
 * @date 07/11/2016 23:11
 */
public class DhcpFileParserTest {

	@Test
	public void should_parse_and_sort_file_lines() throws URISyntaxException, IOException {

		List<String> fileLines = readFile("sample.leases");

		List<DhcpLease> dhcpLeases = DhcpFileParser.parseFileContents(fileLines);

		assertEquals("host1", dhcpLeases.get(0).getHostname());
		assertEquals("Host2", dhcpLeases.get(1).getHostname());
		assertEquals("host3", dhcpLeases.get(2).getHostname());
	}

	@Test
	public void should_parse_and_sort_file_lines_with_same_hostname() throws URISyntaxException, IOException {

		List<String> fileLines = readFile("sample_same_hostname.leases");

		List<DhcpLease> dhcpLeases = DhcpFileParser.parseFileContents(fileLines);

		assertEquals("192.168.0.101", dhcpLeases.get(0).getIpAddress());
		assertEquals("192.168.0.102", dhcpLeases.get(1).getIpAddress());
		assertEquals("192.168.0.103", dhcpLeases.get(2).getIpAddress());
	}

	private List<String> readFile(String fileName) throws IOException, URISyntaxException {
		URI uri = ClassLoader.getSystemResource(fileName).toURI();
		File sampleFile = new File(uri);
		return FileUtils.readLines(sampleFile, Charset.defaultCharset());
	}

}