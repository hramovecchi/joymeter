package com.joymeter.resource;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import com.joymeter.entity.dto.SignUpRequestDTO;
import com.joymeter.service.SessionService;

@Component
@Path("/sessions")
@Scope("request")
public class SessionResource {
	
	@Autowired
	SessionService sessionService;

	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public Response signUp(SignUpRequestDTO signUpRequestDTO){
		if ((signUpRequestDTO != null) &&
				!StringUtils.isEmpty(signUpRequestDTO.getFacebookAccessToken())){
			return Response.ok(sessionService.signUp(signUpRequestDTO)).build();
		}
		
		return Response.status(Status.BAD_REQUEST).build();
	}
}
