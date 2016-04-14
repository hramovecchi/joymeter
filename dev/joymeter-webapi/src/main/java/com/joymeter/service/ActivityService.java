package com.joymeter.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.joymeter.entity.Activities;
import com.joymeter.entity.Activity;
import com.joymeter.entity.User;
import com.joymeter.entity.dto.ActivityDTO;
import com.joymeter.entity.dto.SyncupActionDTO;
import com.joymeter.entity.dto.SyncupActions;
import com.joymeter.entity.util.ActivityUtils;
import com.joymeter.exception.ErrorCode;
import com.joymeter.repository.ActivityRepository;
import com.joymeter.security.JoymeterBadRequestException;
import com.joymeter.security.JoymeterUnauthorizedException;

@Service("activityService")
public class ActivityService {
	
	@Autowired
	ActivityRepository activityRepository;
	
	@Autowired
	LevelOfJoyService levelOfJoyService;
	
	public Activities getActivities(User user) {
		
		List<Activity> activities = activityRepository.getByUserId(user.getId());
		
		return new Activities(activities);
	}
	
	public Activity addActivity(User owner, ActivityDTO activityDTO) {

		Activity activity = new Activity();
		activity = ActivityUtils.mappedToActivity(activity, activityDTO);
		activity.setUser(owner);
		
		levelOfJoyService.updateHistoryNewActivity(activity);
		activityRepository.save(activity);
		
		return activity;
	}
	
	public Activity getActivity(long activityId, User owner) {
		
		Activity activity = activityRepository.getById(activityId);
		
		if (activity == null){
			throw new JoymeterBadRequestException(ErrorCode.INVALID_ACTIVITY);
		}
		if (owner.getId() != activity.getUser().getId()){
			throw new JoymeterUnauthorizedException();
		}
		
		return activity;
	}
	
	public void deleteActivity(long activityId, User owner) {
		
		Activity activity = activityRepository.getById(activityId);
		
		if (activity == null) {
			throw new JoymeterBadRequestException(ErrorCode.INVALID_ACTIVITY);
		}
		
		if (owner.getId() != activity.getUser().getId()){
			throw new JoymeterUnauthorizedException();
		}
		activityRepository.delete(activity);
		levelOfJoyService.updateHistoryDeleteActivity(activity);
	}
	
	public Activity updateActivity(long activityId, User owner, ActivityDTO activityDTO) {
		
		Activity activity = activityRepository.getById(activityId);
		if (activity == null) {
			throw new JoymeterBadRequestException(ErrorCode.INVALID_ACTIVITY);
		}
		
		if (owner.getId() != activity.getUser().getId()){
			throw new JoymeterUnauthorizedException();
		}
		
		activity = ActivityUtils.mappedToActivity(activity, activityDTO);
		
		activityRepository.update(activity);
		levelOfJoyService.updateHistoryUpdateActivity(activity);
		
		return activity;
	}

	public void syncup(SyncupActions syncupActions, User owner) {
		for (SyncupActionDTO action: syncupActions.getSyncupActions()){
			switch (action.getSyncupActionMethod()){
			case save:
				addActivity(owner, action.getActivity());
				break;
			case update:
				updateActivity(action.getActivity().getId(), owner, action.getActivity());
				break;
			case delete:
				deleteActivity(action.getActivity().getId(), owner);
				break;
			}
		}
		
	}

}
