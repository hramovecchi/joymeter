package com.joymeter.entity.dto;

public enum ErrorCode {

	INVALID_FACEBOOK_TOKEN(100,"Invalid Facebook Access token."),
	EXPIRED_FACEBOOK_TOKEN(101,"Expired Facebook Access token.");

	private int code;
	private String description;
	
	ErrorCode(int code, String description){
		this.code = code;
		this.description = description;
	}
	
	public int getCode(){
		return this.code;
	}
	
	public String getDescription(){
		return this.description;
	}
	
	public ErrorCode getByCode(int code){
		
		for(ErrorCode error : ErrorCode.values()){
			if(error.code == code){
				return error;
			}
		}
		return null;
	}

}
