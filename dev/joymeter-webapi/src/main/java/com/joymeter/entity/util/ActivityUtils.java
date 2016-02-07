package com.joymeter.entity.util;

import com.joymeter.entity.Activity;
import com.joymeter.entity.dto.ActivityDTO;

public class ActivityUtils {
	
	private static int min = 3600000;
	private static int max = min * 3;
	
	public static Activity mappedToActivity (Activity activity, ActivityDTO dto){
		activity.setClassified(dto.getClassified() != null ? dto.getClassified().booleanValue(): activity.isClassified());
		activity.setDescription(dto.getDescription() != null ? dto.getDescription() : activity.getDescription());
		activity.setEndDate(dto.getEndDate() != null ? Long.parseLong(dto.getEndDate()) : activity.getEndDate());
		activity.setLevelOfJoy(dto.getLevelOfJoy() != null ? Integer.parseInt(dto.getLevelOfJoy()) : activity.getLevelOfJoy());
		activity.setStartDate(dto.getStartDate() != null ? Long.parseLong(dto.getStartDate()) : activity.getStartDate());
		activity.setSummary(dto.getSummary() != null ? dto.getSummary() : activity.getSummary());
		activity.setType(dto.getType() != null ? dto.getType() : activity.getType());
		
		return activity;
	}
	
	public static long durationToSuggest (){
		long num=(long)Math.floor(Math.random()*(min-(max+1))+(max+1));
        return num;
	}

}
