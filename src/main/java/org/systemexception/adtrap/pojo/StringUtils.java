package org.systemexception.adtrap.pojo;

import java.util.ArrayList;

/**
 * @author leo
 * @date 02/11/2016 20:45
 */
public class StringUtils {

	public static final String LOG_LINE_SEPARATOR = " ";
	public static final int QUERY_MONTH = 0;
	public static final int QUERY_DAY = 1;
	public static final int QUERY_TIME = 2;
	public static final int QUERY_TYPE = 4;
	public static final int DOMAIN = 5;
	public static final int TARGET = 7;
	public static final String EMTPY_STRING = "";

	public static ArrayList<String> splitLogLine(final String logLine) {

		String[] splitString = logLine.split(LOG_LINE_SEPARATOR);
		ArrayList<String> result = new ArrayList<>();

		for (String split: splitString) {
			if(!LOG_LINE_SEPARATOR.equals(split) && !EMTPY_STRING.equals(split)) {
				result.add(split);
			}
		}

		return result;
	}
}
