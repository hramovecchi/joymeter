package com.joymeter.service;

import com.joymeter.entity.Advice;
import com.joymeter.entity.User;

public interface RecomendationService {
	
	Advice suggestActivity(User user);

}
