package com.joymeter.events.bus;

import android.app.Activity;

import com.joymeter.dto.ActivityDTO;

/**
 * Created by hramovecchi on 22/03/2016.
 */
public class UpdateActivityEvent{

    private final int position;
    private final ActivityDTO activity;
    private final Activity view;

    public UpdateActivityEvent(Activity view, ActivityDTO activity, int position){
        this.view = view;
        this.activity = activity;
        this.position = position;
    }

    public int getPosition(){
        return position;
    }

    public ActivityDTO getActivity(){
        return activity;
    }

    public Activity getView() {return view ;}
}
