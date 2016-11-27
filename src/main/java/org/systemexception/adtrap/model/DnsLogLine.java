package org.systemexception.adtrap.model;

import org.systemexception.adtrap.pojo.StringUtils;

/**
 * @author leo
 * @date 07/10/2016 17:15
 */
public class DnsLogLine {

	private long date;

	private String queryType;

	private String queryDomain;

	private String queryTarget;

	public long getDate() {
		return date;
	}

	public void setDate(long date) {
		this.date = date;
	}

	public String getQueryType() {
		return queryType;
	}

	public void setQueryType(String queryType) {
		this.queryType = queryType;
	}

	public String getQueryDomain() {
		return queryDomain;
	}

	public void setQueryDomain(String queryDomain) {
		this.queryDomain = queryDomain;
	}

	public String getQueryTarget() {
		return queryTarget;
	}

	public void setQueryTarget(String queryTarget) {
		this.queryTarget = queryTarget;
	}

	@Override
	public String toString() {
		return queryType + StringUtils.LOG_LINE_SEPARATOR + queryDomain + StringUtils.LOG_LINE_SEPARATOR + queryTarget;
	}
}
