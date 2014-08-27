package com.joymeter.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

@Entity
@Table(name = "USER")
@NamedQueries({ 
		@NamedQuery(name = "User.findAll", query = "SELECT u FROM User u"),
		@NamedQuery(name = "User.findByFacebookAccessToken", query = "SELECT u FROM User u WHERE u.facebook_access_token=:facebookAccessToken"),
		@NamedQuery(name = "User.findByEmail", query = "SELECT u FROM User u WHERE u.email=:email")})
public class User implements Serializable{
	
	private static final long serialVersionUID = -1776298606636882629L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long id;
	
	private String full_name;
	private String email;
	private String facebook_access_token;
	private long creation_date;
	
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public String getFullName() {
		return full_name;
	}
	public void setFullName(String fullName) {
		this.full_name = fullName;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getFacebookAccessToken() {
		return facebook_access_token;
	}
	public void setFacebookAccessToken(String facebookAccessToken) {
		this.facebook_access_token = facebookAccessToken;
	}
	public long getCreationDate() {
		return creation_date;
	}
	public void setCreationDate(long creationDate) {
		this.creation_date = creationDate;
	}
}
