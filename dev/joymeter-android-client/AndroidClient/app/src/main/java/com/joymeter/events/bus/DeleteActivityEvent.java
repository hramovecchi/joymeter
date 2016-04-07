package com.joymeter.events.bus;

import com.joymeter.dto.ActivityDTO;

/**
 * Created by hramovecchi on 22/03/2016.
 */
public class DeleteActivityEvent {

    private final ActivityDTO activity;

    public DeleteActivityEvent(ActivityDTO activity){
        this.activity = activity;
    }

    public ActivityDTO getActivity(){
        return activity;
    }
}
