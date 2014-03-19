package com.joymeter.entity;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

@Entity
@Table(name = "ACTIVITY")
@NamedQueries( { @NamedQuery(name = "Activity.findAllByUser", query = "SELECT a FROM Activity a WHERE a.userId=:userId")})
public class Activity implements Serializable{
	
	private static final long serialVersionUID = -3597156974325366320L;
	
	@Id 
	@GeneratedValue(strategy = GenerationType.AUTO)
	private int id;
	private String type;
	private String summary;
	private String description;
	private int levelOfJoy;
	private long startDate;
	private long endDate;
	private int userId;
	private boolean classified;
	
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
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
		return levelOfJoy;
	}
	public void setLevelOfJoy(int levelOfJoy) {
		this.levelOfJoy = levelOfJoy;
	}
	public long getStartDate() {
		return startDate;
	}
	public void setStartDate(long startDate) {
		this.startDate = startDate;
	}
	public long getEndDate() {
		return endDate;
	}
	public void setEndDate(long endDate) {
		this.endDate = endDate;
	}
	public int getUserId() {
		return userId;
	}
	public void setUserId(int userId) {
		this.userId = userId;
	}
	public boolean isClassified() {
		return classified;
	}
	public void setClassified(boolean classified) {
		this.classified = classified;
	}

}
