package com.joymeter.service.imp;

import java.util.Calendar;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.joymeter.entity.Activity;
import com.joymeter.entity.Advice;
import com.joymeter.repository.ActivityRepository;
import com.joymeter.repository.AdviceRepository;
import com.joymeter.service.RecomendationService;

@Service("recomendationService")
public class DefaultRecomendationService implements RecomendationService{

	@Autowired
	ActivityRepository activityRepository;
	
	@Autowired
	AdviceRepository adviceRepository;
	
	public Advice suggestActivity(long userId) {
		return this.suggestActivityForUser(userId);
	}
	
	private Advice suggestActivityForUser(long userID) {
		List<Activity> activities = activityRepository.getAllActivitiesByUserId(userID);
		Activity activitytoSuggest;
		
		if (activities.isEmpty()){
			activitytoSuggest =  getDefaultActivity();
		} else {
			int index = (int)Math.floor(Math.random()*(0-(activities.size()))+(activities.size()+1));
			activitytoSuggest = activities.get(index);
		}
		
		long now = Calendar.getInstance().getTimeInMillis();
		
		Advice advice = new Advice();
		advice.setDate(now);
		advice.setSuggestedActivity(activitytoSuggest);
		advice.setUser(activitytoSuggest.getUser());
		
		adviceRepository.save(advice);
		
		return advice;
	}
	
	private Activity getDefaultActivity() {
		Activity a = new Activity();
		a.setClassified(Boolean.FALSE);
		a.setDescription("Birra con amigos");
		a.setLevelOfJoy(5);
		a.setSummary("Cerveza en Antares");
		a.setType("Recleación, Salida");
		return a;
	}

}
