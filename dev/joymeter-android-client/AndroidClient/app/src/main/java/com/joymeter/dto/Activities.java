package com.joymeter.dto;

import com.google.gson.annotations.Expose;

import java.util.List;

/**
 * Created by PwifiUser on 25/08/2015.
 */
public class Activities {

    @Expose
    private List<ActivityDTO> activities;

    public Activities(){}

    public Activities(List<ActivityDTO> activities){
        this.activities = activities;
    }

    /**
     *
     * @return
     * The activities
     */
    public List<ActivityDTO> getActivities() {
        return activities;
    }

    /**
     *
     * @param activities
     * The activities
     */
    public void setActivities(List<ActivityDTO> activities) {
        this.activities = activities;
    }
}
