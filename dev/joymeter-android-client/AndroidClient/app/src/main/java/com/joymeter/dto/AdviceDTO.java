package com.joymeter.dto;

import com.google.gson.annotations.Expose;

import java.io.Serializable;

/**
 * Created by PwifiUser on 11/07/2016.
 */
public class AdviceDTO implements Serializable {

    @Expose
    private Long id;
    @Expose
    private Long date;
    @Expose
    private Boolean accepted;
    @Expose
    private ActivityDTO suggestedActivity;
    @Expose
    private ActivityDTO createdActivity;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getDate() {
        return date;
    }

    public void setDate(Long date) {
        this.date = date;
    }

    public Boolean getAccepted() {
        return accepted;
    }

    public void setAccepted(Boolean accepted) {
        this.accepted = accepted;
    }

    public ActivityDTO getSuggestedActivity() {
        return suggestedActivity;
    }

    public void setSuggestedActivity(ActivityDTO suggestedActivity) {
        this.suggestedActivity = suggestedActivity;
    }

    public ActivityDTO getCreatedActivity() {
        return createdActivity;
    }

    public void setCreatedActivity(ActivityDTO createdActivity) {
        this.createdActivity = createdActivity;
    }
}
