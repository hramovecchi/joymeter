package com.joymeter.entity;

import java.io.Serializable;

import javax.persistence.CascadeType;
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

@Entity
@Table(name = "SESSION")
@NamedQueries( { 
	@NamedQuery(name = "Session.findAllByUser", query = "SELECT s FROM Session s WHERE s.user.id=:userID"),
	@NamedQuery(name = "Session.findSessionBySessionToken", query = "SELECT s FROM Session s WHERE s.session_token=:sessionToken"),
	@NamedQuery(name = "Session.deleteSessionByUserIdAndGcm", query = "DELETE FROM Session s WHERE s.user.id =:userID AND s.gcm_token=:gcmToken")})
public class Session implements Serializable{
	
	private static final long serialVersionUID = 7753016265168879373L;
	
	@Id 
	@GeneratedValue(strategy = GenerationType.AUTO)
	@JsonIgnore
	private long id;
	

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "user_id")
	private User user;
	
	private String session_token;
	private String gcm_token;
	
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
	
	public String getSessionToken() {
		return session_token;
	}
	
	public void setSessionToken(String sessionToken) {
		this.session_token = sessionToken;
	}
	
	public String getGcmToken() {
		return gcm_token;
	}
	
	public void setGcmToken(String gcmToken) {
		this.gcm_token = gcmToken;
	}
}
