package com.joymeter.service;

import java.util.List;

import com.joymeter.entity.LevelOfJoy;
import com.joymeter.entity.User;

public interface LevelOfJoyService {
	LevelOfJoy save(LevelOfJoy activity);
	List<LevelOfJoy> getByUser(User user);
	List<LevelOfJoy> getLastEntriesByUser(User user, int days);
	LevelOfJoy getLastByUser(User user);
	LevelOfJoy getById(long id);
	boolean delete(LevelOfJoy activity);
	LevelOfJoy update(LevelOfJoy activity);
}
