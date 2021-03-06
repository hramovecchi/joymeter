package com.joymeter.resource;

import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Response;

import org.joda.time.DateTime;
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
	public Response suggestActivity(
			@QueryParam("timeInMillis") @DefaultValue("now") final String timeInMillis){
		Session session = JoymeterContextHolder.get().getJoymeterSession();

		DateTime time = "now".equals(timeInMillis) ? DateTime.now() : new DateTime(Long.valueOf(timeInMillis));
		userService.suggestActivity(session.getUser(), session.getGcmToken(), time.getMillis());
		
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
