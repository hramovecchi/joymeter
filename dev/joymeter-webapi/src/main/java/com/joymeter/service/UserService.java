package com.joymeter.service;

import java.util.List;

import com.joymeter.entity.User;

public interface UserService {
	boolean save(User user);
	List<User> getAll();
	User getById(int id);
	boolean delete(User user);
	boolean update(User user);
}
