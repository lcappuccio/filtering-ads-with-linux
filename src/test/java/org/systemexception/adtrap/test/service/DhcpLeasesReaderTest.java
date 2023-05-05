package org.systemexception.adtrap.test.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.systemexception.adtrap.model.DhcpLease;
import org.systemexception.adtrap.service.DhcpLeasesReader;

import java.io.File;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author leo
 * @date 06/11/2016 23:03
 */
class DhcpLeasesReaderTest {

	private List<DhcpLease> dhcpLeases;
	private DhcpLeasesReader sut;
	private File file;

	@BeforeEach
	void setUp() throws URISyntaxException {
		URL systemResource = ClassLoader.getSystemResource("sample.leases");
		file = new File(systemResource.toURI());
	}

	@Test
	void should_read_file() {

		sut = new DhcpLeasesReader(file.getAbsolutePath());

		dhcpLeases = sut.getDhcpLeases();

		assertNotNull(dhcpLeases);
		assertEquals(3, dhcpLeases.size());
	}

	@Test
	void should_have_valid_values() {

		sut = new DhcpLeasesReader(file.getAbsolutePath());

		dhcpLeases = sut.getDhcpLeases();
		DhcpLease dhcpLease = dhcpLeases.get(0);

		assertTrue(dhcpLease.getLeaseExpireDate().contains("07/11/2016") &&
				dhcpLease.getLeaseExpireDate().contains(":52"));
		assertEquals("00:50:56:31:51:28", dhcpLease.getMacAddress());
		assertEquals("192.168.0.102", dhcpLease.getIpAddress());
		assertEquals("host1", dhcpLease.getHostname());
		assertTrue("VMWARE, INC.".equalsIgnoreCase(dhcpLease.getVendor()));
	}

	@Test
	void should_log_nonexisting_file() {

		File file = new File("nonexisting.file");
		sut = new DhcpLeasesReader(file.getAbsolutePath());

		dhcpLeases = sut.getDhcpLeases();

		assertEquals(0, dhcpLeases.size());
	}
}
