package com.joymeter.service;

import com.joymeter.entity.Advice;

public interface NotificationService {
	
	void sendNotificationMessage(String userGCMID, Advice adviceToSuggest);
}
