package com.joymeter.security;

import javax.ws.rs.WebApplicationException;

import com.joymeter.api.util.ResponseFactory;
import com.joymeter.exception.ErrorCode;

public class JoymeterBadRequestException extends WebApplicationException {

	private static final long serialVersionUID = 3821073351845283937L;

	public JoymeterBadRequestException(ErrorCode code){
		super(ResponseFactory.badRequest(code));
	}
}
