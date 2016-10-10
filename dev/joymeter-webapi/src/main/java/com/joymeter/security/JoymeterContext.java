package com.joymeter.security;

import com.joymeter.entity.Session;

/**
 * Class that contains context information of the current request
 * 
 * @author hramovecchi
 */
public class JoymeterContext {
	private String sessionToken;
	private Session joymeterSession;
	private String adminUserName;
	private String adminPassword;

	public String getSessionToken() {
		return sessionToken;
	}

	public void setSessionToken(String sessionToken) {
		this.sessionToken = sessionToken;
	}

	public Session getJoymeterSession() {
		return joymeterSession;
	}

	public void setJoymeterSession(Session joymeterSession) {
		this.joymeterSession = joymeterSession;
	}

	public String getAdminUserName() {
		return adminUserName;
	}

	public void setAdminUserName(String adminUserName) {
		this.adminUserName = adminUserName;
	}

	public String getAdminPassword() {
		return adminPassword;
	}

	public void setAdminPassword(String adminPassword) {
		this.adminPassword = adminPassword;
	}
}
