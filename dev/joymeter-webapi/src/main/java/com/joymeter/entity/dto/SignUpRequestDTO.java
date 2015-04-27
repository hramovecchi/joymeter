package com.joymeter.entity.dto;

import java.io.Serializable;

public class SignUpRequestDTO  implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -5452100383034657287L;
	
	private String facebookAccessToken;
	private String gcmToken;

	public String getFacebookAccessToken() {
		return facebookAccessToken;
	}

	public void setFacebookAccessToken(String facebookAccessToken) {
		this.facebookAccessToken = facebookAccessToken;
	}

	public String getGcmToken() {
		return gcmToken;
	}

	public void setGcmToken(String gcmToken) {
		this.gcmToken = gcmToken;
	}
}
