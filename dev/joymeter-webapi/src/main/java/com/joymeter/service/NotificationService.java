package com.joymeter.service;

import com.joymeter.entity.Activity;

public interface NotificationService {
	
	void sendNotificationMessage(String userGCMID, Activity activityToSuggest);
}
