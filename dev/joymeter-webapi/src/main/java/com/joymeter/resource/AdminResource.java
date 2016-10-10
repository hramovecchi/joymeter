package com.joymeter.resource;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.core.Response;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.joymeter.security.Admin;

@Component
@Path("/admin")
@Scope("request")
public class AdminResource {
	
	@GET
	@Path("/weka/generate")
	@Admin
	public Response generateWekaModel(){
		//TODO for each user, trigger build weka model
		
		return Response.ok("{}").build();
	}

}
