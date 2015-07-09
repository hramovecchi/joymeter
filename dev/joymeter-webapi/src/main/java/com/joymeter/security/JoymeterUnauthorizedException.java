package com.joymeter.security;

import javax.ws.rs.WebApplicationException;

import com.joymeter.api.util.ResponseFactory;

public class JoymeterUnauthorizedException extends WebApplicationException {

	private static final long serialVersionUID = 3821073351845283937L;

	public JoymeterUnauthorizedException(){
		super(ResponseFactory.unauthorized());
	}
}
