package com.joymeter.resource;

import java.util.List;

import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Response;

import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.joymeter.security.Admin;
import com.joymeter.service.AdminService;

@Component
@Path("/admin")
@Scope("request")
public class AdminResource {

	@Autowired
	AdminService adminService;

	@GET
	@Path("/weka/generate")
	@Admin
	public Response generateWekaModel(){
		adminService.generateWekaModels();
		
		return Response.ok("{}").build();
	}

	@GET
	@Path("/users/suggest")
	@Admin
	public Response adviceAllUsers(@QueryParam("users") final List<String> users,
			@QueryParam("timeInMillis") @DefaultValue("now") final String timeInMillis){
		DateTime time = "now".equals(timeInMillis) ? DateTime.now() : new DateTime(Long.valueOf(timeInMillis));
		adminService.suggestUsers(users, time.getMillis());

		return Response.ok("{}").build();
	}
}
