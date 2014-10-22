package com.joymeter.entity.util;

public class FacebookUtils {

	public static String getFullName(String first, String middle, String last) {
		String fullName = new String();
		if(first != null && !first.isEmpty()){
			fullName = String.format("%s", first);
		}
		if(middle != null && !middle.isEmpty()){
			fullName = String.format("%s %s",fullName, middle);
		}
		if(last != null && !last.isEmpty()){
			fullName = String.format("%s %s",fullName, last);
		}
		if (!fullName.isEmpty()){
			return fullName;
		}
		return null;
	}
}
