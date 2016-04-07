package com.joymeter.service.mapper;

import com.joymeter.dao.SyncupAction;
import com.joymeter.dao.SyncupActivity;
import com.joymeter.dao.UserActivity;
import com.joymeter.dto.ActivityDTO;
import com.joymeter.dto.SyncupActionDTO;
import com.joymeter.service.JActivityService.SyncupActionMethod;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by hramovecchi on 06/04/2016.
 */
public class ActivityMapper {

    public static List<ActivityDTO> mapFromDB(List<UserActivity> activities){
        List<ActivityDTO> activityList = new ArrayList<ActivityDTO>();
        for (UserActivity activity:activities){
            activityList.add(mapFromDB(activity));
        }
        return  activityList;
    }

    public static ActivityDTO mapFromDB(UserActivity userActivity){
        ActivityDTO activityDTO = new ActivityDTO();
        activityDTO.setId(userActivity.getId());
        activityDTO.setStartDate(userActivity.getStartDate());
        activityDTO.setClassified(userActivity.getClassified());
        activityDTO.setEndDate(userActivity.getEndDate());
        activityDTO.setDescription(userActivity.getDescription());
        activityDTO.setLevelOfJoy(userActivity.getLevelOfJoy());
        activityDTO.setSummary(userActivity.getSummary());
        activityDTO.setType(userActivity.getType());

        return activityDTO;
    }

    public static List<UserActivity> mapToDB(List<ActivityDTO> activities){
        List<UserActivity> activityList = new ArrayList<UserActivity>();
        for (ActivityDTO activity:activities){
            activityList.add(mapToDB(activity));
        }
        return  activityList;
    }

    public static UserActivity mapToDB(ActivityDTO activity){
        UserActivity userActivity = new UserActivity();
        userActivity.setId(activity.getId());
        userActivity.setStartDate(activity.getStartDate());
        userActivity.setClassified(activity.isClassified());
        userActivity.setEndDate(activity.getEndDate());
        userActivity.setDescription(activity.getDescription());
        userActivity.setLevelOfJoy(activity.getLevelOfJoy());
        userActivity.setSummary(activity.getSummary());
        userActivity.setType(activity.getType());

        return userActivity;
    }

    public static SyncupActivity mapToSyncupActivity(ActivityDTO activityToUpdate) {
        SyncupActivity syncupActivity = new SyncupActivity();
        syncupActivity.setId(activityToUpdate.getId());
        syncupActivity.setStartDate(activityToUpdate.getStartDate());
        syncupActivity.setClassified(activityToUpdate.isClassified());
        syncupActivity.setEndDate(activityToUpdate.getEndDate());
        syncupActivity.setDescription(activityToUpdate.getDescription());
        syncupActivity.setLevelOfJoy(activityToUpdate.getLevelOfJoy());
        syncupActivity.setSummary(activityToUpdate.getSummary());
        syncupActivity.setType(activityToUpdate.getType());

        return syncupActivity;
    }

    public static SyncupActionDTO maptoSyncupActionDTO(SyncupAction syncupAction) {
        SyncupActionDTO syncupActionDTO = new SyncupActionDTO(
                mapToActivityDTO(syncupAction.getSyncupActivity()),
                SyncupActionMethod.valueOf(syncupAction.getAction()));

        return syncupActionDTO;
    }

    public static ActivityDTO mapToActivityDTO(SyncupActivity syncupActivity) {
        ActivityDTO activityDTO = new ActivityDTO();
        activityDTO.setId(syncupActivity.getId());
        activityDTO.setStartDate(syncupActivity.getStartDate());
        activityDTO.setClassified(syncupActivity.getClassified());
        activityDTO.setEndDate(syncupActivity.getEndDate());
        activityDTO.setDescription(syncupActivity.getDescription());
        activityDTO.setLevelOfJoy(syncupActivity.getLevelOfJoy());
        activityDTO.setSummary(syncupActivity.getSummary());
        activityDTO.setType(syncupActivity.getType());

        return activityDTO;
    }

    public static List<SyncupActionDTO> maptoSyncupActions(List<SyncupAction> activitiesToSyncup) {
        List<SyncupActionDTO> syncupActions = new ArrayList<SyncupActionDTO>();
        for (SyncupAction syncupAction: activitiesToSyncup){
            syncupActions.add(ActivityMapper.maptoSyncupActionDTO(syncupAction));
        }

        return syncupActions;
    }
}
