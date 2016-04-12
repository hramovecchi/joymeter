package com.joymeter.repository;

import java.util.List;

import org.joda.time.DateTime;

import com.joymeter.entity.Activity;

public interface ActivityRepository {
	Activity save(Activity activity);
	List<Activity> getByUserId(long userId);
	List<Activity> getDayActivitiesByUserId(long userId, DateTime date);
	Activity getById(long id);
	boolean delete(Activity activity);
	Activity update(Activity activity);
	Activity suggestActivity(long userID);
}
