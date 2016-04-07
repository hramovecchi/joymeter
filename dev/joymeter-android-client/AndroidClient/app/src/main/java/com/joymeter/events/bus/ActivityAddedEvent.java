package com.joymeter.events.bus;

import com.joymeter.dto.ActivityDTO;

/**
 * Created by hramovecchi on 22/03/2016.
 */
public class ActivityAddedEvent extends ActivityBaseEvent{

    public ActivityAddedEvent(ActivityDTO activity){
        super(activity);
    }
}
