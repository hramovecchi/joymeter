package com.joymeter.resource;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.Response;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.joymeter.entity.Advice;
import com.joymeter.entity.Session;
import com.joymeter.security.JoymeterContextHolder;
import com.joymeter.security.RequiresAuthentication;
import com.joymeter.service.UserService;

@Component
@Path("/recommendations")
@Scope("request")
public class RecomendationResource {
	
	@Autowired
	private UserService userService;
	
	@GET
	@Path("/me/suggest")
	@RequiresAuthentication
	public Response suggestActivity(){
		Session session = JoymeterContextHolder.get().getJoymeterSession();

		userService.suggestActivity(session.getUser(), session.getGcmToken());
		
		return Response.ok("{}").build();
    }
	
	@POST
	@Path("/me/accept")
	@RequiresAuthentication
	public Response acceptRecomendation(Advice acceptedAdvice){
		Session session = JoymeterContextHolder.get().getJoymeterSession();
		
		userService.acceptAdvice(session.getUser(),acceptedAdvice);
		
		return Response.ok("{}").build();
	}

}
