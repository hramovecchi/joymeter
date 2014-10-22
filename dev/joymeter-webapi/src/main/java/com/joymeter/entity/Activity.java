package com.joymeter.entity;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

@Entity
@Table(name = "ACTIVITY")
@NamedQueries( { @NamedQuery(name = "Activity.findAllByUser", query = "SELECT a FROM Activity a WHERE a.user.id=:userID")})
public class Activity implements Serializable{
	
	private static final long serialVersionUID = -3597156974325366320L;
	
	@Id 
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long id;
	private String type;
	private String summary;
	private String description;
	private int level_of_joy;
	private long start_date;
	private long end_date;
	
	@ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
	@JoinColumn(name = "user_id")
	private User user;
	
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
	public User getUser() {
		return user;
	}
	public void setUser(User user) {
		this.user = user;
	}
	public boolean isClassified() {
		return classified;
	}
	public void setClassified(boolean classified) {
		this.classified = classified;
	}

}
