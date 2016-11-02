package org.systemexception.adtrap.statsviewer.pojo;

import java.util.ArrayList;

/**
 * @author leo
 * @date 02/11/2016 20:45
 */
public class LogParser {

	private static final String LOG_LINE_SEPARATOR = " ", EMTPY_STRING = "";

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
