package com.joymeter.service.imp.recommender.base;

import com.google.common.base.Predicate;
import com.joymeter.entity.Activity;

public class ActivityTypePredicate implements Predicate<Activity>{
	private String type;
	
	public ActivityTypePredicate(String activityType){
		type = activityType;
	}

	public boolean apply(Activity input) {
		if (input.getType().equals(type)){
			return false;
		}
		return true;
	}
}