package com.joymeter.service.imp.recommender.base;

import java.util.ArrayList;
import java.util.List;

import org.joda.time.DateTime;
import org.joda.time.DateTimeConstants;

public enum DayType {
	WEEKEND,
	WORKINGDAY;
	
	public static List<String> stringValues() {
		List<String> list = new ArrayList<String>();
		for (DayType value: values()){
			list.add(value.name());
		}
		return list;
	}
	
	public static DayType valueOf(long timeInMillis){
		DateTime date = new DateTime(timeInMillis);
	    int dow = date.getDayOfWeek();
	    
		switch (dow) {
		case DateTimeConstants.SATURDAY:
			return DayType.WEEKEND;
		case DateTimeConstants.SUNDAY:
			return DayType.WEEKEND;
		default:
			return DayType.WORKINGDAY;
		}
	}
}
