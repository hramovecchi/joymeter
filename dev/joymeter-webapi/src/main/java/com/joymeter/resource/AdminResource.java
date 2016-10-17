package com.joymeter.resource;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.core.Response;

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
	public Response adviceAllUsers(){
		adminService.suggestAllUsers();

		return Response.ok("{}").build();
	}
}
