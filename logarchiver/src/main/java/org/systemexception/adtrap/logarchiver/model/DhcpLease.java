package org.systemexception.adtrap.logarchiver.model;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * @author leo
 * @date 06/11/2016 22:46
 */
public class DhcpLease {

	private long leaseExpireDate;
	private String macAddress;
	private String ipAddress;
	private String hostname;
	private String vendor;

	public String getLeaseExpireDate() {
		return leaseTimeToDateString();
	}

	public void setLeaseExpireDate(long leaseExpireDate) {
		this.leaseExpireDate = leaseExpireDate;
	}

	public String getMacAddress() {
		return macAddress;
	}

	public void setMacAddress(String macAddress) {
		this.macAddress = macAddress;
	}

	public String getIpAddress() {
		return ipAddress;
	}

	public void setIpAddress(String ipAddress) {
		this.ipAddress = ipAddress;
	}

	public String getHostname() {
		return hostname;
	}

	public void setHostname(String hostname) {
		this.hostname = hostname;
	}

	public void setVendor(String vendor) {
		this.vendor = vendor;
	}

	public String getVendor() {
		return vendor;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;

		DhcpLease dhcpLease = (DhcpLease) o;

		if (leaseExpireDate != dhcpLease.leaseExpireDate) return false;
		if (macAddress != null ? !macAddress.equals(dhcpLease.macAddress) : dhcpLease.macAddress != null) return false;
		if (ipAddress != null ? !ipAddress.equals(dhcpLease.ipAddress) : dhcpLease.ipAddress != null) return false;
		return hostname != null ? hostname.equals(dhcpLease.hostname) : dhcpLease.hostname == null;

	}

	@Override
	public int hashCode() {
		int result = (int) (leaseExpireDate ^ (leaseExpireDate >>> 32));
		result = 31 * result + (macAddress != null ? macAddress.hashCode() : 0);
		result = 31 * result + (ipAddress != null ? ipAddress.hashCode() : 0);
		result = 31 * result + (hostname != null ? hostname.hashCode() : 0);
		return result;
	}

	@Override
	public String toString() {
		return leaseTimeToDateString() + " " + macAddress + " " + ipAddress + " " + hostname;
	}

	private String leaseTimeToDateString() {
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault());
		return simpleDateFormat.format(new Date(leaseExpireDate * 1000L));
	}
}
