package com.joymeter.entity.dto;

import java.io.Serializable;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class SignUpRequestDTO  implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -5452100383034657287L;
	
	private String facebookAccessToken;

	public String getFacebookAccessToken() {
		return facebookAccessToken;
	}

	public void setFacebookAccessToken(String facebookAccessToken) {
		this.facebookAccessToken = facebookAccessToken;
	}
}
