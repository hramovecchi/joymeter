package com.joymeter.entity;

import java.util.List;

/**
 * Simple POJO that represents a list of users.
 * 
 */
public class Users {

	private List<User> users;

	public Users() {
	}

	public Users(List<User> users) {
		this.users = users;
	}

	public List<User> getUsers() {
		return users;
	}

}
