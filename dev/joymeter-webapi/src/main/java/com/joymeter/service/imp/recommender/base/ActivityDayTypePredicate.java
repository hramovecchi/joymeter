package com.joymeter.service.imp.recommender.base;

import com.google.common.base.Predicate;
import com.joymeter.entity.Activity;

public class ActivityDayTypePredicate implements Predicate<Activity>{
	private DayType dt;
	
	public ActivityDayTypePredicate(DayType dayType){
		dt = dayType;
	}

	public boolean apply(Activity input) {
		if (dt.equals(DayType.valueOf(input.getStartDate()))){
			return false;
		}
		return true;
	}
}