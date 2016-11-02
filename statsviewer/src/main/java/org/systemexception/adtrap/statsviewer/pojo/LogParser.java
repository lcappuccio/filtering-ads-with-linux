package org.systemexception.adtrap.statsviewer.pojo;

import org.systemexception.adtrap.statsviewer.model.DnsLogLine;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * @author leo
 * @date 02/11/2016 20:45
 */
public class LogParser {

	private static final String LOG_LINE_SEPARATOR = " ", EMTPY_STRING = "";
	private static final int MONTH = 0;
	private static final int DAY = 1;
	private static final int HOUR_MINUTE_SECONDS = 2;
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
		dnsLogLine.setDate(getDateFromLog(splitLogLine.get(MONTH), splitLogLine.get(DAY),
				splitLogLine.get(HOUR_MINUTE_SECONDS)));
		dnsLogLine.setQueryType(splitLogLine.get(QUERY_TYPE));
		dnsLogLine.setQueryDomain(splitLogLine.get(DOMAIN));
		dnsLogLine.setQueryTarget(splitLogLine.get(TARGET));
		return dnsLogLine;
	}

	private Date getDateFromLog(final String month, final String day, final String hhmmss) throws ParseException {

		int year = Calendar.getInstance().get(Calendar.YEAR);
		DateFormat dateFormat = new SimpleDateFormat("yyyy MMM d H:m:s", Locale.getDefault());
		return dateFormat.parse(year + LOG_LINE_SEPARATOR + month + LOG_LINE_SEPARATOR + day + LOG_LINE_SEPARATOR +
				hhmmss);
	}
}
