package com.joymeter.entity;

import java.util.List;

/**
 * Simple POJO that represents a list of activities.
 * 
 */
public class Activities {
	
	private List<Activity> activities;
	
	public Activities(){
	}
	
	public Activities(List<Activity> activities){
		this.activities = activities;
	}
	
	public List<Activity> getActivities(){
		return activities;
	}

}
