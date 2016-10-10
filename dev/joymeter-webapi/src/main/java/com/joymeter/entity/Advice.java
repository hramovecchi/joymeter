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

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(Include.NON_EMPTY)
@JsonIgnoreProperties(ignoreUnknown=true)
@Entity
@Table(name = "ADVICE")
@NamedQueries( { @NamedQuery(name = "Advice.findAllByUser", query = "SELECT a FROM Advice a WHERE a.user.id=:userID"),
				 @NamedQuery(name = "Advice.findAcceptedByType", query = "SELECT a FROM Advice a WHERE a.user.id=:userID AND a.accepted=:state AND a.suggestedActivity.type=:type"),
				 @NamedQuery(name = "Advice.findByType", query = "SELECT a FROM Advice a WHERE a.user.id=:userID AND a.suggestedActivity.type=:type")})

public class Advice implements Serializable{

	private static final long serialVersionUID = -3952756609238098688L;
	@Id 
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long id;
	private long date;
	private boolean accepted;
	private String technique;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "user_id")
	@JsonIgnore
	private User user;
	
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "suggested_activity_id")
	private Activity suggestedActivity;

	@ManyToOne(optional = true, fetch = FetchType.EAGER)
	@JoinColumn(name = "created_activity_id")
	private Activity createdActivity;

	public long getDate() {
		return date;
	}

	public void setDate(long date) {
		this.date = date;
	}

	public boolean isAccepted() {
		return accepted;
	}

	public void setAccepted(boolean accepted) {
		this.accepted = accepted;
	}
	
	public Activity getSuggestedActivity() {
		return suggestedActivity;
	}

	public void setSuggestedActivity(Activity suggestedActivity) {
		this.suggestedActivity = suggestedActivity;
	}
	
	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public Activity getCreatedActivity() {
		return createdActivity;
	}

	public void setCreatedActivity(Activity createdActivity) {
		this.createdActivity = createdActivity;
	}

	public String getTechnique() {
		return technique;
	}

	public void setTechnique(String technique) {
		this.technique = technique;
	}
}
