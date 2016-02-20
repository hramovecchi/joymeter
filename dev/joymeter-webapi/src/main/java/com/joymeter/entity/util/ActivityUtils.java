package com.joymeter.entity.util;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;

import com.joymeter.entity.Activity;
import com.joymeter.entity.dto.ActivityDTO;

public class ActivityUtils {
	
	private static int min = 3600000;
	private static int max = min * 3;
	private static String pattern = "dd-MM-yyyy HH:mm";
	
	public static Activity mappedToActivity (Activity activity, ActivityDTO dto){
		activity.setClassified(dto.getClassified() != null ? dto.getClassified().booleanValue(): activity.isClassified());
		activity.setDescription(dto.getDescription() != null ? dto.getDescription() : activity.getDescription());
		activity.setLevelOfJoy(dto.getLevelOfJoy() != null ? Integer.parseInt(dto.getLevelOfJoy()) : activity.getLevelOfJoy());
		activity.setSummary(dto.getSummary() != null ? dto.getSummary() : activity.getSummary());
		activity.setType(dto.getType() != null ? dto.getType() : activity.getType());
		
		activity.setStartDate(dto.getStartDateTime() != null ? 
									DateTime.parse(dto.getStartDateTime(), DateTimeFormat.forPattern(pattern)).getMillis() : 
									(dto.getStartDate() != null ? 
											Long.parseLong(dto.getStartDate()) : 
											activity.getStartDate()));
		activity.setEndDate(dto.getEndDateTime() != null ? 
									DateTime.parse(dto.getEndDateTime(), DateTimeFormat.forPattern(pattern)).getMillis() : 
									(dto.getEndDate() != null ? 
											Long.parseLong(dto.getEndDate()) : 
											activity.getEndDate()));

		return activity;
	}
	
	public static long durationToSuggest (){
		long num=(long)Math.floor(Math.random()*(min-(max+1))+(max+1));
        return num;
	}

}
