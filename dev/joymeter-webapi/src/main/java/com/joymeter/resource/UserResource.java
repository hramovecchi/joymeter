package com.joymeter.resource;

import javax.ws.rs.core.Response;


public interface UserResource {
	Response getUsers();
	Response getUser(String id);
	Response updateUser(String id, String name, String email, String facebookAccountName, String sesionToken, String creationDate);
	Response addUser(String name, String email, String facebookAccountName, String sesionToken, String creationDate);
	Response deleteUser(String id);

}