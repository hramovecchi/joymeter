package com.joymeter.service;

import com.joymeter.entity.Advice;

public interface RecomendationService {
	
	Advice suggestActivity(long userId);

}
