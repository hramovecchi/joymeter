package com.joymeter.resource;

import java.util.List;

import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.core.UriInfo;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.joymeter.entity.User;
import com.joymeter.service.UserService;

@Component
@Path("/users")
@Scope("request")
public class UserResourceWs implements UserResource {
	@Autowired
	UserService userService;
	@Context 
	UriInfo ui;
	private Logger log = Logger.getLogger(this.getClass());

	/*
	 */
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Response getUsers() {
		log.info("getUsers entered");

		StringBuffer jsonBuffer = new StringBuffer("{ \"users\": [");
		List<User> users = userService.getAll();
		boolean first = true;
		for (User user : users) {
			if (first)
				first = false;
			else
				jsonBuffer.append(",");
			jsonBuffer.append(getJsonUserObject(user));
		}
		jsonBuffer.append("]}");
		log.info("Sending: \n\n" + jsonBuffer.toString() + "\n\n");
		
		return Response.ok(jsonBuffer.toString()).build();
	}

	/*
	 */
	@GET
	@Path("/{id}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getUser(@PathParam("id") String id) {
		log.info("getUser entered");
		log.info("Hit getUser");
		User user = userService.getById(Integer.parseInt(id));
		if (user!= null){
			String result = (String) getJsonUserObject(user);
			log.info("Sending: \n\n" + result + "\n\n");
			return Response.ok(result.toString()).build();
		} else {
			return Response.status(Status.BAD_REQUEST).entity("User not found with the given id").build();
		}
	}

	/*
	 */
	@PUT
	@Path("/{id}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response updateUser(@PathParam("id") String id,
			@QueryParam("name") String name, @QueryParam("email") String email,
			@QueryParam("facebook_account") String facebookAccount, @QueryParam("sesion_token") String sesionToken,
			@QueryParam("creation_date") String creationDate) {
		log.info("updateUser entered");
		log.info("name: " + name);
		log.info("email: " + email);
		log.info("facebook_account: " + facebookAccount);
		log.info("sesion_token : " + sesionToken);
		log.info("creation_date : " + creationDate);
		log.info("id : " + id);

		if (id!=null){
			User user = userService.getById(Integer.parseInt(id));
			if (user != null) {
				log.info("It has an ID");
				log.info("We found the user with that id");
				user.setName(name);
				user.setEmail(email);
				user.setFacebookAccount(facebookAccount);
				user.setSesionToken(sesionToken);
				user.setCreationDate(Long.valueOf(creationDate));
				userService.update(user);
				return Response.ok("").build();
			} else {
				return Response.status(Status.BAD_REQUEST).entity("User not found with the given id").build();
			}
		} else {
			return Response.status(Status.BAD_REQUEST).entity("id not given").build();
		}

		
	}

	/*
	 */
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	public Response addUser(@QueryParam("name") String name, @QueryParam("email") String email, 
			@QueryParam("facebook_account") String facebookAccount, @QueryParam("sesion_token") String sesionToken,
			@QueryParam("creation_date") String creationDate){
		log.info("addUser entered");

		log.info("name: " + name);
		log.info("email: " + email);
		log.info("facebook_account: " + facebookAccount);
		log.info("sesion_token : " + sesionToken);
		log.info("creation_date : " + creationDate);

		User user = new User();
		user.setName(name);
		user.setEmail(email);
		user.setFacebookAccount(facebookAccount);
		user.setSesionToken(sesionToken);
		user.setCreationDate(Long.valueOf(creationDate));
		userService.save(user);
		
		return Response.ok("").build();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.joymeter.rest.UserResource#deleteUser(java.lang.String)
	 */

	@DELETE
	@Path("/{id}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response deleteUser(@PathParam("id") String id) {
		if(id!=null) {
			deleteUserById(id);
		}
		
		MultivaluedMap<String, String> queryParams = ui.getQueryParameters();
		List<String> ids = queryParams.get("id");
		if(ids == null) {
			log.info("\n\nThe ids is null");
		} else {
			for (String currentid : ids) {
				deleteUserById(currentid);
			}
		}
		return Response.ok("").build();
	}

	private void deleteUserById(String id) {
		log.info("Delete Id: " + id);
		User user = userService.getById(Integer.parseInt(id));
		if (user == null) {
			log.info("Null was returned for ID: " + id);
		} else {
			userService.delete(user);
		}
	}

	private Object getJsonUserObject(User user) {
		StringBuffer buffer = new StringBuffer("{\"id\": \"" + user.getId());
		buffer.append("\",\"name\": \"").append(user.getName());
		buffer.append("\",\"email\": \"").append(user.getEmail());
		buffer.append("\",\"facebook_account\": \"").append(user.getFacebookAccount());
		buffer.append("\",\"sesion_token\": \"").append(user.getSesionToken());
		buffer.append("\",\"creation_date\": \"").append(user.getCreationDate());
		buffer.append("\"}");
		return buffer.toString();
	}
}
