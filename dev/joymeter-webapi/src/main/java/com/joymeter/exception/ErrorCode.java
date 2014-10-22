package com.joymeter.exception;

public enum ErrorCode {

	INVALID_FACEBOOK_TOKEN(100,"Invalid Facebook Access token"),
	EXPIRED_FACEBOOK_TOKEN(101,"Expired Facebook Access token");

	private long errorCode;
	private String errorDescription;
	
	ErrorCode(long errorCode, String errorDescription){
		this.errorCode = errorCode;
		this.errorDescription = errorDescription;
	}
	
	public long getErrorCode(){
		return this.errorCode;
	}
	
	public String getErrorDescription(){
		return this.errorDescription;
	}
	
	public ErrorCode getByErrorCode(long errorCode){
		
		for(ErrorCode error : ErrorCode.values()){
			if(error.errorCode == errorCode){
				return error;
			}
		}
		return null;
	}

}
