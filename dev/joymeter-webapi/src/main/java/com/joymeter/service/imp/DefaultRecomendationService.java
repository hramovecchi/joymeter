package com.joymeter.service.imp;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.joymeter.entity.Activity;
import com.joymeter.repository.ActivityRepository;
import com.joymeter.service.RecomendationService;

@Service("recomendationService")
public class DefaultRecomendationService implements RecomendationService{

	@Autowired
	ActivityRepository activityRepository;
	
	public Activity suggestActivity(long userId) {
		return activityRepository.suggestActivity(userId);
	}

}
