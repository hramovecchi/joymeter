package com.joymeter.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.joymeter.entity.Advice;
import com.joymeter.entity.HistoricalLevel;
import com.joymeter.entity.User;
import com.joymeter.entity.dto.ActivityDTO;
import com.joymeter.entity.dto.UserDTO;
import com.joymeter.entity.util.UserUtils;
import com.joymeter.repository.AdviceRepository;
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
	RecommendationService recommendationService;

	@Autowired
	ActivityService activityService;

	@Autowired
	AdviceRepository adviceRepository;

	public User updateUser(User user, UserDTO userDTO) {
		User updatedUser = UserUtils.mappedToUser(user, userDTO);
		userRepository.update(updatedUser);
		return updatedUser;
	}

	public void suggestActivity(User user, String gsmToken){
		Advice advice = recommendationService.suggestActivity(user);
		notificationService.sendNotificationMessage(gsmToken, advice);
	}
	
	public HistoricalLevel getLevelOfJoy(User user, int days) {
		return new HistoricalLevel(levelOfJoyService.getLastEntriesByUser(user, days));
	}

	public void acceptAdvice(User owner, Advice acceptedAdvice) {
		ActivityDTO activityToAdd = new ActivityDTO(acceptedAdvice.getCreatedActivity());
		activityService.addActivity(owner, activityToAdd);

		acceptedAdvice.setUser(owner);
		acceptedAdvice.setAccepted(Boolean.TRUE);
		adviceRepository.update(acceptedAdvice);
	}
}
