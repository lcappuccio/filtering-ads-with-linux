package org.systemexception.adtrap.statsviewer.pojo;

import org.systemexception.adtrap.statsviewer.model.DnsLogLine;

import java.text.ParseException;
import java.util.ArrayList;

/**
 * @author leo
 * @date 02/11/2016 20:45
 */
public class LogParser {

	private static final String LOG_LINE_SEPARATOR = " ", EMTPY_STRING = "";
	private static final int QUERY_TYPE = 4;
	private static final int DOMAIN = 5;
	private static final int TARGET = 7;

	public ArrayList<String> splitLogLine(final String logLine) {

		String[] splitString = logLine.split(LOG_LINE_SEPARATOR);
		ArrayList<String> result = new ArrayList<>();

		for (String split: splitString) {
			if(!LOG_LINE_SEPARATOR.equals(split) && !EMTPY_STRING.equals(split)) {
				result.add(split);
			}
		}

		return result;
	}

	public DnsLogLine dnsLogLineFromLogLine(final String logLine) throws ParseException {

		DnsLogLine dnsLogLine = new DnsLogLine();
		ArrayList<String> splitLogLine = splitLogLine(logLine);
		dnsLogLine.setDate(System.currentTimeMillis());
		dnsLogLine.setQueryType(splitLogLine.get(QUERY_TYPE));
		dnsLogLine.setQueryDomain(splitLogLine.get(DOMAIN));
		dnsLogLine.setQueryTarget(splitLogLine.get(TARGET));
		return dnsLogLine;
	}
}
