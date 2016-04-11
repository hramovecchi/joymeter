package com.joymeter.repository;

import java.util.List;

import com.joymeter.entity.User;

public interface UserRepository {
	boolean save(User user);
	List<User> getAll();
	User getById(long id);
	User getByFacebookAccessToken(String facebookAccessToken);
	User getByEmail(String email);
	boolean delete(User user);
	boolean update(User user);
}
