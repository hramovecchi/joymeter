package com.joymeter.entity.util;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.joymeter.entity.Activity;
import com.joymeter.entity.User;
import com.joymeter.repository.ActivityRepository;
import com.joymeter.repository.UserRepository;

public class ActivityDataLoader {
	private List<ActivityDBRow> activitys = new ArrayList<ActivityDBRow>();
	private ActivityRepository activityRepository;
	private UserRepository userRepository;
	
	public void loadData() {
		for (ActivityDBRow activity : activitys) {
			User user = userRepository.getById(activity.getUserId());
			
			Activity activityToStore= new Activity();
			activityToStore.setClassified(activity.isClassified());
			activityToStore.setDescription(activity.getDescription());
			activityToStore.setEndDate(activity.getEndDate());
			activityToStore.setLevelOfJoy(activity.getLevelOfJoy());
			activityToStore.setStartDate(activity.getStartDate());
			activityToStore.setSummary(activity.getSummary());
			activityToStore.setType(activity.getType());
			activityToStore.setUser(user);
			
			activityRepository.save(activityToStore);
		}
		activitys.clear();
		activitys = null;
	}

	public void setActivitys(List<ActivityDBRow> activities) {
		this.activitys = activities;
	}
	@Autowired
	public void setActivityRepository(ActivityRepository activityRepository) {
		this.activityRepository = activityRepository;
	}
	
	@Autowired
	public void setUserRepository(UserRepository userRepository) {
		this.userRepository = userRepository;
	}
	
	public void init() {
		loadData();
	}

}
