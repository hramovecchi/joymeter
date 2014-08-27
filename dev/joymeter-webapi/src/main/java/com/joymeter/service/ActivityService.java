package com.joymeter.service;

import java.util.List;

import com.joymeter.entity.Activity;

public interface ActivityService {
	boolean save(Activity activity);
	List<Activity> getByUserId(long userId);
	Activity getById(long id);
	boolean delete(Activity activity);
	boolean update(Activity activity);
}
