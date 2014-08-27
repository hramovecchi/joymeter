package com.joymeter.entity.util;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.joymeter.entity.Activity;
import com.joymeter.entity.User;
import com.joymeter.service.ActivityService;
import com.joymeter.service.UserService;

public class ActivityDataLoader {
	private List<ActivityDBRow> activitys = new ArrayList<ActivityDBRow>();
	private ActivityService activityService;
	private UserService userService;
	
	public void loadData() {
		for (ActivityDBRow activity : activitys) {
			User user = userService.getById(activity.getUserId());
			
			Activity activityToStore= new Activity();
			activityToStore.setClassified(activity.isClassified());
			activityToStore.setDescription(activity.getDescription());
			activityToStore.setEndDate(activity.getEndDate());
			activityToStore.setLevelOfJoy(activity.getLevelOfJoy());
			activityToStore.setStartDate(activity.getStartDate());
			activityToStore.setSummary(activity.getSummary());
			activityToStore.setType(activity.getType());
			activityToStore.setUser(user);
			
			activityService.save(activityToStore);
		}
		activitys.clear();
		activitys = null;
	}

	public void setActivitys(List<ActivityDBRow> activities) {
		this.activitys = activities;
	}
	@Autowired
	public void setActivityService(ActivityService activityService) {
		this.activityService = activityService;
	}
	
	@Autowired
	public void setUserService(UserService userService) {
		this.userService = userService;
	}
	
	public void init() {
		loadData();
	}

}
