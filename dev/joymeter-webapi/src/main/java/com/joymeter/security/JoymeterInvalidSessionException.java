package com.joymeter.security;

import javax.ws.rs.WebApplicationException;

import com.joymeter.api.util.ResponseFactory;

public class JoymeterInvalidSessionException extends WebApplicationException {

	private static final long serialVersionUID = -3151221780357679704L;

	public JoymeterInvalidSessionException(){
		super(ResponseFactory.invalidSession());
	}
}
