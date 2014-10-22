package com.joymeter.exception;

public class RemoteServerFailedException extends Exception {

	private String errorCode;
	
	public RemoteServerFailedException() {
		super();
	}

	public RemoteServerFailedException(String detailMessage,
			Throwable throwable) {
		super(detailMessage, throwable);
	}

	public RemoteServerFailedException(String detailMessage) {
		super(detailMessage);
	}

	public RemoteServerFailedException(Throwable throwable) {
		super(throwable);
	}

	public String getErrorCode() {
		return errorCode;
	}

	public void setErrorCode(String errorCode) {
		this.errorCode = errorCode;
	}
	
}
