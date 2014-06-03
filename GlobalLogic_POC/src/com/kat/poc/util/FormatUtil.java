package com.kat.poc.util;

import com.kat.poc.constants.AppConstants;

public class FormatUtil {

	/**
	 * Splits the given string on comma(,)<br>
	 * Returned String array will contain trimmed values
	 * 
	 * @param nextLine
	 * @return
	 */
	public static String[] splitAndFormat(String nextLine) {
		if(nextLine == null) {
			return null;
		}
		String[] retVal = nextLine.split(AppConstants.CSV_SEPARATOR);
		
		for (int index = 0; index < retVal.length; index++) {
			retVal[index] = retVal[index].trim();
		}
		return retVal;
	}

	public static void addToStringBuilder(StringBuilder sb, Object... args) {
		if(sb == null || args == null || args.length == 0) {
			return;
		}
		
		for(Object arg : args) {
			if(arg!= null) {
				sb.append(String.valueOf(arg));
			}
		}
	}
}
