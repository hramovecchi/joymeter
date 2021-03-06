package com.joymeter.entity.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.joymeter.entity.Activity;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class ActivityDTO {
	private Long id;
	private String type;
	private String summary; 
	private String description;
	private String levelOfJoy;
	private String startDate;
	private String endDate;
	private String startDateTime;
	private String endDateTime;
	private Boolean classified;
	
	public ActivityDTO(){	
	}
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
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
	
	public Boolean getClassified() {
		return classified;
	}
	public void setClassified(Boolean classified) {
		this.classified = classified;
	}
	public String getStartDateTime() {
		return startDateTime;
	}
	public void setStartDateTime(String startDateTime) {
		this.startDateTime = startDateTime;
	}
	public String getEndDateTime() {
		return endDateTime;
	}
	public void setEndDateTime(String endDateTime) {
		this.endDateTime = endDateTime;
	}
	public ActivityDTO(Activity act){
		this.setClassified(act.isClassified());
		this.setDescription(act.getDescription());
		this.setEndDate(String.valueOf(act.getEndDate()));
		this.setId(act.getId());
		this.setLevelOfJoy(String.valueOf(act.getLevelOfJoy()));
		this.setStartDate(String.valueOf(act.getStartDate()));
		this.setSummary(act.getSummary());
		this.setType(act.getType());
	}
}
