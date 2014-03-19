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
@Table(name = "USER")
@NamedQueries( { @NamedQuery(name = "User.findAll", query = "SELECT u FROM User u") })
public class User implements Serializable{
	
	private static final long serialVersionUID = -1776298606636882629L;
	
	@Id 
	@GeneratedValue(strategy = GenerationType.AUTO)
	private int id;
	private String name;
	private String email;
	private String facebookAccount;
	private String sesionToken;
	private long creationDate;
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getFacebookAccount() {
		return facebookAccount;
	}
	public void setFacebookAccount(String facebookAccount) {
		this.facebookAccount = facebookAccount;
	}
	public String getSesionToken() {
		return sesionToken;
	}
	public void setSesionToken(String sesionToken) {
		this.sesionToken = sesionToken;
	}
	public long getCreationDate() {
		return creationDate;
	}
	public void setCreationDate(long creationDate) {
		this.creationDate = creationDate;
	}
	
}
