package com.joymeter.events.bus;

import com.joymeter.dto.ActivityDTO;

/**
 * Created by hramovecchi on 22/03/2016.
 */
public class ActivityUpdatedEvent extends ActivityBaseEvent{

    private final int position;

    public ActivityUpdatedEvent(ActivityDTO activity, int position){
        super(activity);
        this.position = position;
    }

    public int getPosition() {
        return position;
    }
}
