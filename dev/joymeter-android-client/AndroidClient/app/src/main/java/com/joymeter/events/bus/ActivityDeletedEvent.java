package com.joymeter.events.bus;

import com.joymeter.dto.ActivityDTO;

/**
 * Created by hramovecchi on 22/03/2016.
 */
public class ActivityDeletedEvent extends  ActivityBaseEvent{

    public ActivityDeletedEvent(ActivityDTO activityDTO) {
        super(activityDTO);
    }

}
