package com.joymeter.service.imp.recommender.base;

import java.util.function.Predicate;

import com.joymeter.entity.Activity;

public class ActivityPredicate implements Predicate<Activity>{
	private String type;
	
	public ActivityPredicate(String activityType){
		type = activityType;
	}

	public boolean test(Activity input) {
		if (input.getType().equals(type)){
			return false;
		}
		return true;
	}
}