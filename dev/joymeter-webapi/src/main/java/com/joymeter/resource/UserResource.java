package com.joymeter.resource;


import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.joymeter.entity.Session;
import com.joymeter.entity.User;
import com.joymeter.entity.dto.UserDTO;
import com.joymeter.security.JoymeterContextHolder;
import com.joymeter.security.RequiresAuthentication;
import com.joymeter.service.UserService;

@Component
@Path("/users")
@Scope("request")
public class UserResource{
	
	@Autowired
	UserService userService;

	private Logger log = Logger.getLogger(this.getClass());

	/*
	 */
	@GET
	@Path("/me")
	@Produces(MediaType.APPLICATION_JSON)
	@RequiresAuthentication
	public Response getUser() {
		log.info("getUser entered");
		
		User user = JoymeterContextHolder.get().getJoymeterSession().getUser();
		
		return Response.ok(user).build();
	}

	/*
	 */
	@PUT
	@Path("/me")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	@RequiresAuthentication
	public Response updateUser(UserDTO userDTO) {
		log.info("updateUser entered");
		
		User user = JoymeterContextHolder.get().getJoymeterSession().getUser();
		
		return Response.ok(userService.updateUser(user, userDTO)).build();
	}

	/*
	 */
	@GET
	@Path("/me/loj")
	@Produces(MediaType.APPLICATION_JSON)
	@RequiresAuthentication
	public Response getLevelOfJoy(@QueryParam("days") int days) {
		
		log.info("getLevelOfJoy entered");
		
		User user = JoymeterContextHolder.get().getJoymeterSession().getUser();

		return Response.ok(userService.getLevelOfJoy(user, days)).build();
	}
}
