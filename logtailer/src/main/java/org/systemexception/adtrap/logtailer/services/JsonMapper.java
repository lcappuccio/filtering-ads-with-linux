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
		jsonObject.addProperty("date", System.currentTimeMillis());
		jsonObject.addProperty("queryDomain", logSplitted.get(LogParser.QUERY_TYPE));
		jsonObject.addProperty("queryTarget", logSplitted.get(LogParser.DOMAIN));
		jsonObject.addProperty("queryType", logSplitted.get(LogParser.TARGET));
		return jsonObject.toString();
	}

}
