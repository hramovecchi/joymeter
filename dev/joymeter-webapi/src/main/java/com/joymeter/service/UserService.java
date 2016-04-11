package com.joymeter.service;

import java.util.Calendar;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.joymeter.entity.Activity;
import com.joymeter.entity.HistoricalLevel;
import com.joymeter.entity.User;
import com.joymeter.entity.dto.UserDTO;
import com.joymeter.entity.util.ActivityUtils;
import com.joymeter.entity.util.UserUtils;
import com.joymeter.repository.UserRepository;

@Service("userService")
public class UserService {
	
	@Autowired
	UserRepository userRepository;
	
	@Autowired
	NotificationService notificationService;
	
	@Autowired
	LevelOfJoyService levelOfJoyService;
	
	@Autowired
	RecomendationService recomendationService;
	
	public User updateUser(User user, UserDTO userDTO) {
		
		User updatedUser = UserUtils.mappedToUser(user, userDTO);
		userRepository.update(updatedUser);
		return updatedUser;
	}
	
	public void suggestActivity(long userId, String gsmToken){

		Activity aux = new Activity();
		aux.clone(recomendationService.suggestActivity(userId));
		
		long now = Calendar.getInstance().getTimeInMillis();
		
		aux.setStartDate(now);
		aux.setEndDate(now + ActivityUtils.durationToSuggest());
		
		notificationService.sendNotificationMessage(gsmToken, aux);
    }
	
	public HistoricalLevel getLevelOfJoy(User user, int days) {
		
		return new HistoricalLevel(levelOfJoyService.getLastEntriesByUser(user, days));
	}

}
