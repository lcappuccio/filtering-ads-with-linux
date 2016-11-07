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

	private String leaseTimeToDateString() {
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault());
		return simpleDateFormat.format(new Date(leaseExpireDate * 1000L));
	}
}
