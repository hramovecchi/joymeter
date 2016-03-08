package com.joymeter.dto;

/**
 * Created by hramovecchi on 06/03/2016.
 */
public class ActivityAction {

    private ActivityDTO activity;
    private SaveAction saveAction;
    private int position;

    public ActivityAction(ActivityDTO activity, SaveAction saveAction){
        this.setActivity(activity);
        this.setSaveAction(saveAction);
    }

    public ActivityAction(ActivityDTO activity, SaveAction update, int position) {
        this(activity, update);
        this.setPosition(position);
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

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public enum SaveAction {
        save,update
    }
}


