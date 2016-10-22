package com.joymeter.service.imp.recommender.base;

import com.google.common.base.Predicate;
import com.joymeter.entity.Activity;

public class ActivityMomentOfDayPredicate implements Predicate<Activity>{
	private MomentOfDay mod;
	
	public ActivityMomentOfDayPredicate(MomentOfDay momentOfDay){
		mod = momentOfDay;
	}

	public boolean apply(Activity input) {
		if (mod.name().equals(MomentOfDay.valueOf(input.getStartDate()))){
			return false;
		}
		return true;
	}
}