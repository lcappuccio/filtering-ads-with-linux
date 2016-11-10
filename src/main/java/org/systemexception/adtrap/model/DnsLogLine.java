package org.systemexception.adtrap.model;

import org.systemexception.adtrap.pojo.LogParser;

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
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;

		DnsLogLine that = (DnsLogLine) o;

		if (date != that.date) return false;
		if (queryType != null ? !queryType.equals(that.queryType) : that.queryType != null) return false;
		if (queryDomain != null ? !queryDomain.equals(that.queryDomain) : that.queryDomain != null) return false;
		return queryTarget != null ? queryTarget.equals(that.queryTarget) : that.queryTarget == null;

	}

	@Override
	public int hashCode() {
		int result = (int) (date ^ (date >>> 32));
		result = 31 * result + (queryType != null ? queryType.hashCode() : 0);
		result = 31 * result + (queryDomain != null ? queryDomain.hashCode() : 0);
		result = 31 * result + (queryTarget != null ? queryTarget.hashCode() : 0);
		return result;
	}

	@Override
	public String toString() {
		return queryType + LogParser.LOG_LINE_SEPARATOR + queryDomain + LogParser.LOG_LINE_SEPARATOR + queryTarget;
	}
}
