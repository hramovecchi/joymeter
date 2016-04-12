package com.joymeter.service;

import com.joymeter.entity.Activity;

public interface RecomendationService {
	
	Activity suggestActivity(long userId);

}
