package com.joymeter.events.bus;

import com.joymeter.dto.ActivityDTO;

import java.util.List;

/**
 * Created by hramovecchi on 22/03/2016.
 */
public class ActivitiesLoadedEvent {

    private final List<ActivityDTO> activities;

    public ActivitiesLoadedEvent(List<ActivityDTO> activities){
        this.activities = activities;
    }
    public List<ActivityDTO> getActivities(){
        return activities;
    }
}
