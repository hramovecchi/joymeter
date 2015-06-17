package com.joymeter.security;

/**
 * Class that contains context information of the current request
 * 
 * @author hramovecchi
 */
public class JoymeterContext {
	private String sessionToken;

	public String getSessionToken() {
		return sessionToken;
	}

	public void setSessionToken(String sessionToken) {
		this.sessionToken = sessionToken;
	}
}
