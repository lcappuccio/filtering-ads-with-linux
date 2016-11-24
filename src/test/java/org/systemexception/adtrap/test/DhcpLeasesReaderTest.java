package org.systemexception.adtrap.test;

import org.junit.Before;
import org.junit.Test;
import org.systemexception.adtrap.model.DhcpLease;
import org.systemexception.adtrap.service.DhcpLeasesReader;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.List;

import static junit.framework.TestCase.assertTrue;
import static org.hibernate.validator.internal.util.Contracts.assertNotNull;
import static org.junit.Assert.assertEquals;

/**
 * @author leo
 * @date 06/11/2016 23:03
 */
public class DhcpLeasesReaderTest {

	private List<DhcpLease> dhcpLeases;

	@Before
	public void setUp() throws IOException, URISyntaxException {
		URL systemResource = ClassLoader.getSystemResource("sample.leases");
		File file = new File(systemResource.toURI());
		DhcpLeasesReader sut = new DhcpLeasesReader(file.getAbsolutePath());
		dhcpLeases = sut.getDhcpLeases();
	}

	@Test
	public void should_read_file() {

		assertNotNull(dhcpLeases);
		assertEquals(3, dhcpLeases.size());
	}

	@Test
	public void should_have_valid_values() {

		DhcpLease dhcpLease = dhcpLeases.get(0);

		assertTrue(dhcpLease.getLeaseExpireDate().contains("07/11/2016") &&
				dhcpLease.getLeaseExpireDate().contains(":52"));
		assertEquals("00:50:56:31:51:28", dhcpLease.getMacAddress());
		assertEquals("192.168.0.102", dhcpLease.getIpAddress());
		assertEquals("host1", dhcpLease.getHostname());
		assertTrue("VMware, Inc.".equals(dhcpLease.getVendor()) || "N/A".equals(dhcpLease.getVendor()));
	}
}
