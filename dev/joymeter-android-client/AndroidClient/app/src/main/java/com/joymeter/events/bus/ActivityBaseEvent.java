package com.joymeter.events.bus;

import com.joymeter.dto.ActivityDTO;

/**
 * Created by PwifiUser on 25/03/2016.
 */
public class ActivityBaseEvent {

    private ActivityDTO activity;

    public ActivityBaseEvent(ActivityDTO activity){
        this.activity = activity;
    }

    public ActivityDTO getActivity(){
        return activity;
    }
}
