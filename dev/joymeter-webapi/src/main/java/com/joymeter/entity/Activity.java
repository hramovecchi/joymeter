package com.joymeter.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.joymeter.entity.dto.ActivityDTO;

@JsonInclude(Include.NON_EMPTY)
@JsonIgnoreProperties
@Entity
@Table(name = "ACTIVITY")
@NamedQueries( { @NamedQuery(name = "Activity.findAllByUser", query = "SELECT a FROM Activity a WHERE a.user.id=:userID"),
				 @NamedQuery(name = "Activity.fetchByUserAndDeleteState", query = "SELECT a FROM Activity a WHERE a.user.id=:userID AND a.deleted=:deletestate"),
				 @NamedQuery(name = "Activity.findDayActivitiesByUser", query = "SELECT a FROM Activity a WHERE a.user.id=:userID AND a.start_date <= :endday AND a.end_date >= :startday")})

public class Activity implements Serializable{
	
	private static final long serialVersionUID = -3597156974325366320L;
	
	@Id 
	@Column(name="id", insertable=true, updatable=true, unique=true, nullable=false)
	private long id;
	private String type;
	private String summary;
	private String description;
	private int level_of_joy;
	private long start_date;
	private long end_date;
	
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "user_id")
	@JsonIgnore
	private User user;
	
	private boolean classified;
	@Column(name="deleted", columnDefinition="boolean default false")
	private boolean deleted;
	
	
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
	public boolean isDeleted() {
		return deleted;
	}
	public void setDeleted(boolean deleted) {
		this.deleted = deleted;
	}
}
