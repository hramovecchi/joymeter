package com.joymeter.resource;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.joymeter.entity.Session;
import com.joymeter.entity.User;
import com.joymeter.entity.dto.UserDTO;
import com.joymeter.entity.util.UserUtils;
import com.joymeter.security.JoymeterContextHolder;
import com.joymeter.security.JoymeterUnauthorizedException;
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
	@Path("/{id}")
	@Produces(MediaType.APPLICATION_JSON)
	@RequiresAuthentication
	public Response getUser(@PathParam("id") long userId) {
		log.info("getUser entered");
		
		User user = JoymeterContextHolder.get().getJoymeterSession().getUser();
		
		if (userId != user.getId()){
			throw new JoymeterUnauthorizedException();
		}
		
		return Response.ok(user).build();
	}

	/*
	 */
	@PUT
	@Path("/{id}")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	@RequiresAuthentication
	public Response updateUser(@PathParam("id") long userId, UserDTO userDTO) {
		log.info("updateUser entered");
		
		User user = JoymeterContextHolder.get().getJoymeterSession().getUser();
		
		if (!user.getEmail().equals(userDTO.getEmail())
				|| userId != user.getId()) {
			throw new JoymeterUnauthorizedException();
		}

		user = UserUtils.mappedToUser(user, userDTO);
		userService.update(user);
		return Response.ok(user).build();
	}
}
