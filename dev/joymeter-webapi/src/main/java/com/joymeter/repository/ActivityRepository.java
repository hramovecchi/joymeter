package com.joymeter.repository;

import java.util.List;

import org.joda.time.DateTime;

import com.joymeter.entity.Activity;

public interface ActivityRepository {
	Activity save(Activity activity);
	List<Activity> getAllActivitiesByUserId(long userId);
	List<Activity> fetchActiveActivitiesByUserId(long userId);
	List<Activity> fetchDeletedActivitiesByUserId(long userId);
	List<Activity> getDayActivitiesByUserId(long userId, DateTime date);
	Activity getById(long id);
	boolean hardDelete(Activity activity);
	boolean delete(Activity activity);
	Activity update(Activity activity);
}
