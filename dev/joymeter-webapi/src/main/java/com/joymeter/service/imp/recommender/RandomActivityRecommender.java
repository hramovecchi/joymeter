package com.joymeter.service.imp.recommender;

import java.util.Calendar;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.joymeter.entity.Activity;
import com.joymeter.entity.Advice;
import com.joymeter.entity.User;
import com.joymeter.repository.ActivityRepository;
import com.joymeter.service.DefaultAdviceActivityService;
import com.joymeter.service.recommender.ActivityRecommender;

@Service("randomActivityRecommender")
public class RandomActivityRecommender implements ActivityRecommender{

	@Autowired
	ActivityRepository activityRepository;	
	
	@Autowired
	DefaultAdviceActivityService defaultAdviceActService;
	
	private boolean defaultActivitiesStored = false;
	
	public Advice suggestActivity(User user) {
		return this.suggestActivityForUser(user);
	}
	
	private Advice suggestActivityForUser(User user) {
		List<Activity> activities = activityRepository.getAllActivitiesByUserId(user.getId());
		activities.addAll(getDefaultActivities());
		
		Activity activitytoSuggest;
		int index = (int)Math.floor(Math.random()*(0-(activities.size()))+(activities.size()));
		activitytoSuggest = activities.get(index);

		Advice advice = new Advice();
		advice.setSuggestedActivity(activitytoSuggest);
		advice.setUser(user);
		
		return advice;
	}
	
	private List<Activity> getDefaultActivities() {
		if (!defaultActivitiesStored){
			for (Activity a: defaultAdviceActService.getActivities()){
				activityRepository.update(a);
			}
			defaultActivitiesStored = true;
		}
		
		return defaultAdviceActService.getActivities();
	}

}
