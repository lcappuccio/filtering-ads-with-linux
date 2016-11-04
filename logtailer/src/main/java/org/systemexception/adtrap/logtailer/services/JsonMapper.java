package org.systemexception.adtrap.logtailer.services;

import com.google.gson.JsonObject;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Optional;

/**
 * @author leo
 * @date 02/11/2016 19:19
 */
public class JsonMapper {

	private final LogParser logParser = new LogParser();
	private static final int DNSMASQ_STANDARD_LINE_SIZE = 7, DNSMASQ_DHCP_LINE_SIZE = 8;
	private static final String DATE = "date";
	private static final String QUERY_DOMAIN = "queryDomain";
	private static final String QUERY_TARGET = "queryTarget";
	private static final String QUERY_TYPE = "queryType";

	/**
	 * Format log line as JSON for logarchiver
	 *
	 * @param logLine
	 * @return
	 * @throws ParseException
	 */
	public Optional<String> jsonFromLogLine(final String logLine) throws ParseException {
		JsonObject jsonObject = new JsonObject();
		ArrayList<String> logSplitted = logParser.splitLogLine(logLine);
		if (logSplitted.size() != DNSMASQ_STANDARD_LINE_SIZE && logSplitted.size() != DNSMASQ_DHCP_LINE_SIZE) {
			return Optional.empty();
		}
		jsonObject.addProperty(DATE, System.currentTimeMillis());
		if (logLine.contains("dnsmasq-dhcp")) {
			return Optional.of(jsonFromDhcpLogLine(logSplitted, jsonObject));
		}
		jsonObject.addProperty(QUERY_TYPE, logSplitted.get(LogParser.QUERY_TYPE));
		jsonObject.addProperty(QUERY_DOMAIN, logSplitted.get(LogParser.DOMAIN));
		jsonObject.addProperty(QUERY_TARGET, logSplitted.get(LogParser.TARGET));
		return Optional.of(jsonObject.toString());
	}

	/**
	 * Handle exception for DHCP logs
	 *
	 * @param logSplitted
	 * @param jsonObject
	 * @return
	 */
	private String jsonFromDhcpLogLine(final ArrayList<String> logSplitted, final JsonObject jsonObject) {
		jsonObject.addProperty(QUERY_TYPE, logSplitted.get(4));
		jsonObject.addProperty(QUERY_DOMAIN, logSplitted.get(6));
		jsonObject.addProperty(QUERY_TARGET, logSplitted.get(5));
		return jsonObject.toString();
	}

}
