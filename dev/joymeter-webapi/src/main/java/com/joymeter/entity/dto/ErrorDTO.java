package com.joymeter.entity.dto;

public class ErrorDTO {

	private ErrorCode code;
	private String message;
	
	public ErrorDTO(ErrorCode code) {
		this.code = code;
		this.message = code.getDescription();
	}
	
	public ErrorDTO(ErrorCode code, final String message) {
		this.code = code;
		this.message = message;
	}
	
	public ErrorCode getCode() {
		return code;
	}
	public void setCode(ErrorCode code) {
		this.code = code;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(final String message) {
		this.message = message;
	}	
}
