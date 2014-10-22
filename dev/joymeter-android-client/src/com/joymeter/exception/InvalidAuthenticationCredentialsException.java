package com.joymeter.exception;

/**
 * @author cesarroman
 *
 */
public class InvalidAuthenticationCredentialsException extends RuntimeException {

	public InvalidAuthenticationCredentialsException() {
		super();
	}

	public InvalidAuthenticationCredentialsException(String detailMessage,
			Throwable throwable) {
		super(detailMessage, throwable);
	}

	public InvalidAuthenticationCredentialsException(String detailMessage) {
		super(detailMessage);
	}

	public InvalidAuthenticationCredentialsException(Throwable throwable) {
		super(throwable);
	}

}
