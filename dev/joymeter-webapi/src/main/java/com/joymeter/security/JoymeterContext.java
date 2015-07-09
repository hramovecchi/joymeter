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
}
