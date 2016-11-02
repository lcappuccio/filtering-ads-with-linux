package org.systemexception.adtrap.logtailer.services;

import com.google.gson.JsonObject;

/**
 * @author leo
 * @date 02/11/2016 19:19
 */
public class JsonMapper {

	public static final String JSON_PROPERTY_LOG_LINE = "logLine";

	public static String jsonFromLogLine(final String logLine) {
		JsonObject jsonObject = new JsonObject();
		jsonObject.addProperty(JSON_PROPERTY_LOG_LINE, logLine);
		return jsonObject.toString();
	}
}
