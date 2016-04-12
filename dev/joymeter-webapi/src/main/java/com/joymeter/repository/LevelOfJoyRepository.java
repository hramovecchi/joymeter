package com.joymeter.repository;

import java.util.List;

import org.joda.time.DateTime;

import com.joymeter.entity.LevelOfJoy;
import com.joymeter.entity.User;

public interface LevelOfJoyRepository {
	LevelOfJoy save(LevelOfJoy activity);
	List<LevelOfJoy> getByUser(User user);
	LevelOfJoy getByUserDate(User user, DateTime date);
	List<LevelOfJoy> getLastEntriesByUser(User user, int days);
	LevelOfJoy getLastByUser(User user);
	LevelOfJoy getPreviousByUser(User user, DateTime date);
	LevelOfJoy getById(long id);
	boolean delete(LevelOfJoy activity);
	LevelOfJoy update(LevelOfJoy activity);
}
