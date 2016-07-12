package com.joymeter.dto;

import com.google.gson.annotations.Expose;

/**
 * Created by hramovecchi on 10/06/2016.
 */
public class LevelOfJoyRow {

    @Expose
    private long milliseconds;

    @Expose
    private double level;

    @Expose
    private String dateString;

    public long getMilliseconds() {
        return milliseconds;
    }

    public void setMilliseconds(long milliseconds) {
        this.milliseconds = milliseconds;
    }

    public double getLevel() {
        return level;
    }

    public void setLevel(double level) {
        this.level = level;
    }

    public String getDateString() {
        return dateString;
    }

    public void setDateString(String dateString) {
        this.dateString = dateString;
    }
}
