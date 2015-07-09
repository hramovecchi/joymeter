package com.joymeter.api.util;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import com.joymeter.exception.ErrorCode;
import com.joymeter.exception.ErrorDTO;

public class ResponseFactory {

	private ResponseFactory(){}
	
	public static Response badRequest(ErrorCode code) {
		return Response.status(Status.BAD_REQUEST)
				.entity(new ErrorDTO(code))
				.type(MediaType.APPLICATION_JSON).build();
	}
	
	public static Response unauthorized(){
		return Response.status(Status.UNAUTHORIZED)
				.entity(new ErrorDTO(ErrorCode.UNAUTHORIZED))
				.type(MediaType.APPLICATION_JSON).build();
	}
	
	public static Response invalidSession(){
		return Response.status(Status.BAD_REQUEST)
				.entity(new ErrorDTO(ErrorCode.INVALID_SESSION))
				.type(MediaType.APPLICATION_JSON).build();
	}
}
