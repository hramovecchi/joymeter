package com.joymeter.dto;

import com.google.gson.annotations.Expose;
import com.joymeter.service.JActivityService.SyncupActionMethod;

import java.io.Serializable;

/**
 * Created by hramovecchi on 06/03/2016.
 */
public class SyncupActionDTO implements Serializable {

    @Expose
    private ActivityDTO activity;

    @Expose
    private SyncupActionMethod syncupActionMethod;

    public SyncupActionDTO(ActivityDTO activity, SyncupActionMethod syncupActionMethod){
        this.setActivity(activity);
        this.setSyncupActionMethod(syncupActionMethod);
    }

    public ActivityDTO getActivity() {
        return activity;
    }

    public void setActivity(ActivityDTO activity) {
        this.activity = activity;
    }

    public SyncupActionMethod getSyncupActionMethod() {
        return syncupActionMethod;
    }

    public void setSyncupActionMethod(SyncupActionMethod syncupActionMethod) {
        this.syncupActionMethod = syncupActionMethod;
    }
}


