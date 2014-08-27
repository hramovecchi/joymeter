package com.joymeter.resource;

import javax.ws.rs.core.Response;

import com.joymeter.entity.dto.UserDTO;


public interface UserResource {
	Response getUsers();
	Response getUser(String id);
	Response updateUser(UserDTO user);
	Response addUser(UserDTO user);
	Response deleteUser(String id);

}