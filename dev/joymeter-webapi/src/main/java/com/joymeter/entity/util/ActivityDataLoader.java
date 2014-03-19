package com.joymeter.entity.util;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.joymeter.entity.Activity;
import com.joymeter.service.ActivityService;

public class ActivityDataLoader {
	private List<Activity> activitys = new ArrayList<Activity>();
	private ActivityService activityService;
	
	public void loadData() {
		for (Activity activity : activitys) {
			activityService.save(activity);
		}
		activitys.clear();
		activitys = null;
	}

	public void setActivitys(List<Activity> activities) {
		this.activitys = activities;
	}
	@Autowired
	public void setActivityService(ActivityService activityService) {
		this.activityService = activityService;
	}
	
	public void init() {
		loadData();
	}

}
