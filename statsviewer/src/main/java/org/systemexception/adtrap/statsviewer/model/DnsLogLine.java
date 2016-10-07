package org.systemexception.adtrap.statsviewer.model;

import javax.persistence.*;
import java.util.Date;

/**
 * @author leo
 * @date 07/10/2016 17:15
 */
@Entity
public class DnsLogLine {

	@Id
	@GeneratedValue
	@Column(name = "LOG_ID", unique = true, nullable = false)
	private long logId;

	@Column(name = "LOG_TIMESTAMP")
	@Temporal(TemporalType.TIMESTAMP)
	private Date date;

	@Column(name = "QUERY_TYPE")
	private String queryType;

	@Column(name = "DOMAIN")
	private String queryDomain;

	@Column(name = "REQUESTER")
	private String queryFromOrTo;
}
