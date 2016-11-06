package org.systemexception.adtrap.logarchiver.test;

import org.junit.Before;
import org.junit.Test;
import org.systemexception.adtrap.logarchiver.model.DhcpLease;
import org.systemexception.adtrap.logarchiver.service.DhcpLeasesReader;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.List;

import static org.hibernate.validator.internal.util.Contracts.assertNotNull;
import static org.junit.Assert.assertEquals;

/**
 * @author leo
 * @date 06/11/2016 23:03
 */
public class DhcpLeasesReaderTest {

	DhcpLeasesReader sut;
	private List<DhcpLease> dhcpLeases;

	@Before
	public void setUp() throws IOException, URISyntaxException {
		URL systemResource = ClassLoader.getSystemResource("sample.leases");
		File file = new File(systemResource.toURI());
		sut = new DhcpLeasesReader(file.getAbsolutePath());
		dhcpLeases = sut.getDhcpLeases();
	}

	@Test
	public void should_read_file() {

		assertNotNull(dhcpLeases);
		assertEquals(6, dhcpLeases.size());
	}
}
