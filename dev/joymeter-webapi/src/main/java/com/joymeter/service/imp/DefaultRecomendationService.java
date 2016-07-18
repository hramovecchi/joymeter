package com.joymeter.service.imp;

import java.util.Calendar;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.joymeter.entity.Activity;
import com.joymeter.entity.Advice;
import com.joymeter.entity.User;
import com.joymeter.repository.ActivityRepository;
import com.joymeter.repository.AdviceRepository;
import com.joymeter.service.DefaultAdviceActivityService;
import com.joymeter.service.RecomendationService;

@Service("recomendationService")
public class DefaultRecomendationService implements RecomendationService{

	@Autowired
	ActivityRepository activityRepository;
	
	@Autowired
	AdviceRepository adviceRepository;
	
	@Autowired
	DefaultAdviceActivityService defaultAdviceActService;
	
	private boolean defaultActivitiesStored = false;
	
	public Advice suggestActivity(User user) {
		return this.suggestActivityForUser(user);
	}
	
	private Advice suggestActivityForUser(User user) {
		List<Activity> activities = activityRepository.getAllActivitiesByUserId(user.getId());
		Activity activitytoSuggest;
		
		if (activities.isEmpty()){
			activitytoSuggest =  getDefaultActivity();
		} else {
			int index = (int)Math.floor(Math.random()*(0-(activities.size()))+(activities.size()));
			activitytoSuggest = activities.get(index);
		}
		
		long now = Calendar.getInstance().getTimeInMillis();
		
		Advice advice = new Advice();
		advice.setDate(now);
		advice.setSuggestedActivity(activitytoSuggest);
		advice.setUser(user);
		
		adviceRepository.save(advice);
		
		return advice;
	}
	
	private Activity getDefaultActivity() {
		if (!defaultActivitiesStored){
			for (Activity a: defaultAdviceActService.getActivities()){
				activityRepository.update(a);
			}
			defaultActivitiesStored = true;
		}
		int index = (int)Math.floor(Math.random()*
				(0-(defaultAdviceActService.getActivities().size()))+(defaultAdviceActService.getActivities().size()));
		return defaultAdviceActService.getActivities().get(index);
	}

}
