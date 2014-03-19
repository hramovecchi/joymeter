package com.joymeter.service;

import java.util.List;

import com.joymeter.entity.Activity;

public interface ActivityService {
	boolean save(Activity activity);
	List<Activity> getAll(int userId);
	Activity getById(int id);
	boolean delete(Activity activity);
	boolean update(Activity activity);
}
