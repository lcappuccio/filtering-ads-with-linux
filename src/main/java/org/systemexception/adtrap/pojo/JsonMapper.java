package org.systemexception.adtrap.pojo;

import org.systemexception.adtrap.model.DnsLogLine;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @author leo
 * @date 02/11/2016 19:19
 */
public class JsonMapper {

	private static final int DNSMASQ_STANDARD_LINE_SIZE = 8;

	/**
	 * Format log line as JSON for logarchiver
	 *
	 * @param logLine
	 * @return
	 * @throws ParseException
	 */
	public Optional<DnsLogLine> dnsLogLineFromLogLine(final String logLine) throws ParseException {
		DnsLogLine dnsLogLine = new DnsLogLine();
		ArrayList<String> logSplitted = LogParser.splitLogLine(logLine);
		if (logSplitted.size() != DNSMASQ_STANDARD_LINE_SIZE || !isValidDate(logLine)) {
			return Optional.empty();
		}
		dnsLogLine.setDate(System.currentTimeMillis());
		if (logLine.contains("dnsmasq-dhcp")) {
			return Optional.of(dnsLogLineFromDhcpLogLine(logSplitted, dnsLogLine));
		}
		dnsLogLine.setQueryType(logSplitted.get(LogParser.QUERY_TYPE));
		dnsLogLine.setQueryDomain(logSplitted.get(LogParser.DOMAIN));
		dnsLogLine.setQueryTarget(logSplitted.get(LogParser.TARGET));
		return Optional.of(dnsLogLine);
	}

	/**
	 * Handle exception for DHCP logs
	 *
	 * @param logSplitted
	 * @param dnsLogLine
	 * @return
	 */
	private DnsLogLine dnsLogLineFromDhcpLogLine(final ArrayList<String> logSplitted, final DnsLogLine dnsLogLine) {
		dnsLogLine.setQueryType(logSplitted.get(4));
		dnsLogLine.setQueryDomain(logSplitted.get(6));
		dnsLogLine.setQueryTarget(logSplitted.get(5));
		return dnsLogLine;
	}

	private boolean isValidDate(final String logLine)
			throws ParseException {

		ArrayList<String> logSplitted = LogParser.splitLogLine(logLine);
		String monthText = logSplitted.get(LogParser.QUERY_MONTH);
		String dayOfMonth = logSplitted.get(LogParser.QUERY_DAY);
		String hhmmss = logSplitted.get(LogParser.QUERY_TIME);
		Calendar calendar = Calendar.getInstance();
		String year = String.valueOf(calendar.get(Calendar.YEAR));

		String dateString = dayOfMonth + "/" + monthText + "/" + year + LogParser.LOG_LINE_SEPARATOR + hhmmss;
		SimpleDateFormat dateParser = new SimpleDateFormat("d/MMM/yyyy HH:mm:ss", Locale.getDefault());
		Date parsedDate = dateParser.parse(dateString);

		calendar.setTimeInMillis(System.currentTimeMillis());
		calendar.add(Calendar.MINUTE, -1);
		Date time1MinutAgo = calendar.getTime();

		return parsedDate.after(time1MinutAgo);

	}
}
