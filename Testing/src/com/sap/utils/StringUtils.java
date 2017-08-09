package com.sap.utils;

public class StringUtils {
	public static String convertFirstLetterToLowerCase(String qualifier) {

		if (qualifier == null || qualifier.isEmpty())
			return qualifier;
		return qualifier.substring(0, 1).toLowerCase() + qualifier.substring(1);
	}

	public static String convertFirstLetterToUpperCase(String qualifier) {

		if (qualifier == null || qualifier.isEmpty())
			return qualifier;
		return qualifier.substring(0, 1).toUpperCase() + qualifier.substring(1);
	}
}
