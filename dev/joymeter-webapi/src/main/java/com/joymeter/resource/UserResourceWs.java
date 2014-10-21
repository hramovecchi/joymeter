package com.joymeter.resource;

import java.util.List;

import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
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

import com.joymeter.entity.User;
import com.joymeter.entity.Users;
import com.joymeter.entity.dto.UserDTO;
import com.joymeter.service.UserService;

@Component
@Path("/users")
@Scope("request")
public class UserResourceWs implements UserResource {
	@Autowired
	UserService userService;

	private Logger log = Logger.getLogger(this.getClass());

	/*
	 */
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Response getUsers() {
		log.info("getUsers entered");
		List<User> users = userService.getAll();
		
		return Response.ok(new Users(users)).build();
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
			log.info("Sending: \n\n" + user + "\n\n");
			return Response.ok(user).build();
		} else {
			return Response.status(Status.BAD_REQUEST).entity("User not found with the given id").build();
		}
	}

	/*
	 */
	@PUT
	@Produces(MediaType.APPLICATION_JSON)
	public Response updateUser(UserDTO userDTO) {
		log.info("updateUser entered");
		User storedUser = userService.getByEmail(userDTO.getEmail());

		if (storedUser != null){
				log.info("Existing user from the DB, updating...");
				log.info("full_name: " + userDTO.getFullName());
				log.info("email: " + userDTO.getEmail());
				log.info("facebook_access_token: " + userDTO.getFacebookAccessToken());
				log.info("creation_date : " + userDTO.getCreationDate());
				storedUser.setFullName(userDTO.getFullName());
				storedUser.setEmail(userDTO.getEmail());
				storedUser.setFacebookAccessToken(userDTO.getFacebookAccessToken());
				storedUser.setCreationDate(userDTO.getCreationDate());
				userService.update(storedUser);
				return Response.ok(userDTO).build();
		} else {
			return Response.status(Status.BAD_REQUEST).entity("user don't exist").build();
		}

		
	}

	/*
	 */
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	public Response addUser(UserDTO userDTO){
		log.info("addUser entered");

		log.info("full_name: " + userDTO.getFullName());
		log.info("email: " + userDTO.getEmail());
		log.info("facebook_access_token: " + userDTO.getFacebookAccessToken());
		log.info("creation_date : " + userDTO.getCreationDate());

		User user = new User();
		user.setFullName(userDTO.getFullName());
		user.setEmail(userDTO.getEmail());
		user.setFacebookAccessToken(userDTO.getFacebookAccessToken());
		user.setCreationDate(userDTO.getCreationDate());
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

}
