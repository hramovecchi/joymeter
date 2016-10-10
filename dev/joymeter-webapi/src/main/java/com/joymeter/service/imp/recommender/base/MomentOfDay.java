package com.joymeter.service.imp.recommender.base;

import java.util.ArrayList;
import java.util.List;

import org.joda.time.DateTime;

public enum MomentOfDay {
	MORNING(6,14),
	EVENING(14,22),
	NIGHT(22,6);
	
	int min;
	int max;
	
	public static List<String> stringValues() {
		List<String> list = new ArrayList<String>();
		for (MomentOfDay value: values()){
			list.add(value.name());
		}
		return list;
	}
	
	MomentOfDay(int min, int max) {
		this.min = min;
		this.max = max;
	};

	public static MomentOfDay valueOf(long timeInMillis) {
		DateTime date = new DateTime(timeInMillis);
		
		int hod = date.getHourOfDay();
			
		if (hod >= MomentOfDay.MORNING.min && hod < MomentOfDay.MORNING.max){
			return MomentOfDay.MORNING;
		} else if (hod >= MomentOfDay.EVENING.min && hod < MomentOfDay.EVENING.max) {
			return MomentOfDay.EVENING;
		}
		
		return MomentOfDay.NIGHT;
	}
}
