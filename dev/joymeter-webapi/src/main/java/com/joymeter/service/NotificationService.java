package com.joymeter.service;

public interface NotificationService {
	
	void sendNotificationMessage(String userGCMID, String message);
}
