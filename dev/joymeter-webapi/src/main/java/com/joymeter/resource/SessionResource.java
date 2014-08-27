package com.joymeter.resource;

import javax.ws.rs.core.Response;

import com.joymeter.entity.dto.SignUpRequestDTO;


public interface SessionResource {
	Response signUp(SignUpRequestDTO signUpRequestDTO);
	Response autenticate(String sessionToken);
}