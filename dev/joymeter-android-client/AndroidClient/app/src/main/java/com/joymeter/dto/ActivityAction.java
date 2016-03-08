package com.joymeter.dto;

import android.app.Activity;

/**
 * Created by hramovecchi on 06/03/2016.
 */
public class ActivityAction {

    private Activity view;
    private ActivityDTO activity;
    private SaveAction saveAction;
    private int position;

    public ActivityAction(Activity view, ActivityDTO activity, SaveAction saveAction){
        this.setView(view);
        this.setActivity(activity);
        this.setSaveAction(saveAction);
    }

    public ActivityAction(Activity view, ActivityDTO activity, SaveAction update, int position) {
        this(view, activity, update);
        this.setPosition(position);
    }

    public Activity getView() {return view; }

    public void setView(Activity view){this.view = view; }

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


