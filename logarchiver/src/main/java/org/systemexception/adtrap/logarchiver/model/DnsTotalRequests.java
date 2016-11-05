package org.systemexception.adtrap.logarchiver.model;

/**
 * @author leo
 * @date 05/11/2016 11:27
 */
public class DnsTotalRequests {

	private int totalCount;

	public int getTotalCount() {
		return totalCount;
	}

	public void setTotalCount(int totalCount) {
		this.totalCount = totalCount;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;

		DnsTotalRequests that = (DnsTotalRequests) o;

		return totalCount == that.totalCount;

	}

	@Override
	public int hashCode() {
		return totalCount;
	}
}
