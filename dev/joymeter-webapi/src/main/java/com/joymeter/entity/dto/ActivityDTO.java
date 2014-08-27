package com.joymeter.entity.dto;

import com.joymeter.entity.Activity;


public class ActivityDTO {
	private String type;
	private String summary; 
	private String description;
	private String levelOfJoy;
	private String startDate;
	private String endDate;
	private boolean classified;
	private String userId;
	
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
	public String getLevelOfJoy() {
		return levelOfJoy;
	}
	public void setLevelOfJoy(String levelOfJoy) {
		this.levelOfJoy = levelOfJoy;
	}
	public String getStartDate() {
		return startDate;
	}
	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}
	public String getEndDate() {
		return endDate;
	}
	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}
	public boolean isClassified() {
		return classified;
	}
	public void setClassified(boolean classified) {
		this.classified = classified;
	}
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	
	public Activity mappedToActivity(){
		Activity activity = new Activity();
		activity.setClassified(this.isClassified());
		activity.setDescription(this.getDescription());
		activity.setEndDate(Long.parseLong(this.getEndDate()));
		activity.setLevelOfJoy(Integer.parseInt(this.levelOfJoy));
		activity.setStartDate(Long.parseLong(this.getStartDate()));
		activity.setSummary(this.getSummary());
		activity.setType(this.getType());
		
		return activity;
	}
}
