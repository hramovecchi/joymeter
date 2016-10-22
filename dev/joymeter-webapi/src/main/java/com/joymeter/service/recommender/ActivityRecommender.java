package com.joymeter.service.recommender;

import com.joymeter.entity.Advice;
import com.joymeter.entity.User;

public interface ActivityRecommender {
	Advice suggestActivity(User user, long instant) throws Exception;
}
