package com.joymeter.service;

import java.util.List;

import com.joymeter.entity.Activity;

public interface ActivityService {
	Activity save(Activity activity);
	List<Activity> getByUserId(long userId);
	List<Activity> getDayActivitiesByUserId(long userId, long startday, long endday);
	Activity getById(long id);
	boolean delete(Activity activity);
	Activity update(Activity activity);
	Activity suggestActivity(long userID);
}
