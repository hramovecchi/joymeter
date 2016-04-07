package com.joymeter.events.bus;

import android.app.Activity;

import com.joymeter.dto.ActivityDTO;

/**
 * Created by hramovecchi on 22/03/2016.
 */
public class AddActivityEvent {

    private final ActivityDTO activity;
    private final Activity view;

    public AddActivityEvent(Activity view, ActivityDTO activity){
        this.activity = activity;
        this.view = view;
    }

    public ActivityDTO getActivity(){
        return activity;
    }

    public Activity getView() {return view; }
}
