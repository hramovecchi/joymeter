package com.joymeter.entity.util;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.joymeter.entity.User;
import com.joymeter.service.UserService;

public class UserDataLoader {
	private List<User> users = new ArrayList<User>();
	private UserService userService;
	
	public void loadData() {
		for (User user : users) {
			userService.save(user);
		}
		users.clear();
		users = null;
	}

	public void setUsers(List<User> users) {
		this.users = users;
	}
	@Autowired
	public void setUserService(UserService userService) {
		this.userService = userService;
	}
	
	public void init() {
		loadData();
	}

}
