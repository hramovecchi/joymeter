package com.joymeter.dto;

import com.google.gson.annotations.Expose;

import java.util.List;

/**
 * Created by PwifiUser on 10/06/2016.
 */
public class LevelOfJoyHistory {

    @Expose
    private List<LevelOfJoyRow> history;

    public LevelOfJoyHistory (){}

    public LevelOfJoyHistory(List<LevelOfJoyRow> history){
        this.history = history;
    }

    /**
     *
     * @return
     * The level of joy history
     */
    public List<LevelOfJoyRow> getHistory() {
        return history;
    }

    /**
     *
     * @param history
     * The level of joy history
     */
    public void setHistory(List<LevelOfJoyRow> history) {
        this.history = history;
    }
}
