package com.joymeter.entity.util;

/**
 * Simple class that represents the Activity table on the DB for data loading purpose on
 * applicationContext file 
 * 
 */
public class ActivityDBRow{
	
	private long id;
	private String type;
	private String summary;
	private String description;
	private int level_of_joy;
	private long start_date;
	private long end_date;
	private long user_id;
	private boolean classified;
	
	
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getSummary() {
		return summary;
	}
	public void setSummary(String summary) {
		this.summary = summary;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public int getLevelOfJoy() {
		return level_of_joy;
	}
	public void setLevelOfJoy(int levelOfJoy) {
		this.level_of_joy = levelOfJoy;
	}
	public long getStartDate() {
		return start_date;
	}
	public void setStartDate(long startDate) {
		this.start_date = startDate;
	}
	public long getEndDate() {
		return end_date;
	}
	public void setEndDate(long endDate) {
		this.end_date = endDate;
	}
	public long getUserId() {
		return user_id;
	}
	public void setUserId(long user_id) {
		this.user_id = user_id;
	}
	public boolean isClassified() {
		return classified;
	}
	public void setClassified(boolean classified) {
		this.classified = classified;
	}

}
