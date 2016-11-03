package org.systemexception.adtrap.logtailer.services;

import com.google.gson.JsonObject;

import java.text.ParseException;
import java.util.ArrayList;

/**
 * @author leo
 * @date 02/11/2016 19:19
 */
public class JsonMapper {

	private final LogParser logParser = new LogParser();
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
	public String jsonFromLogLine(final String logLine) throws ParseException {
		JsonObject jsonObject = new JsonObject();
		ArrayList<String> logSplitted = logParser.splitLogLine(logLine);
		jsonObject.addProperty(DATE, System.currentTimeMillis());
		if (logLine.contains("dnsmasq-dhcp")) {
			return jsonFromDhcpLogLine(logSplitted, jsonObject);
		}
		jsonObject.addProperty(QUERY_TYPE, logSplitted.get(LogParser.QUERY_TYPE));
		jsonObject.addProperty(QUERY_DOMAIN, logSplitted.get(LogParser.DOMAIN));
		jsonObject.addProperty(QUERY_TARGET, logSplitted.get(LogParser.TARGET));
		return jsonObject.toString();
	}

	/**
	 * Handle exception for DHCP logs
	 *
	 * @param logSplitted
	 * @param jsonObject
	 * @return
	 */
	private String jsonFromDhcpLogLine(final ArrayList<String> logSplitted, final JsonObject jsonObject) {
		jsonObject.addProperty(QUERY_TYPE, logSplitted.get(5));
		jsonObject.addProperty(QUERY_DOMAIN, logSplitted.get(7));
		jsonObject.addProperty(QUERY_TARGET, logSplitted.get(6));
		return jsonObject.toString();
	}

}
