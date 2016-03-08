package com.joymeter.dto;

/**
 * Created by hramovecchi on 06/03/2016.
 */
public class ActivityAction {

    private ActivityDTO activity;
    private SaveAction saveAction;

    public ActivityAction(ActivityDTO activity, SaveAction saveAction){
        this.activity = activity;
        this.saveAction = saveAction;
    }

    public ActivityDTO getActivity() {
        return activity;
    }

    public void setActivity(ActivityDTO activity) {
        this.activity = activity;
    }

    public SaveAction getSaveAction() {
        return saveAction;
    }

    public void setSaveAction(SaveAction saveAction) {
        this.saveAction = saveAction;
    }

    public enum SaveAction {
        save,update
    }
}


