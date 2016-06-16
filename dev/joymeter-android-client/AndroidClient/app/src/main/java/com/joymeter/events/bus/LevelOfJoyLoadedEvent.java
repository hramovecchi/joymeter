package com.joymeter.events.bus;

import com.joymeter.dto.LevelOfJoyRow;

import java.util.List;

/**
 * Created by hramovecchi on 16/06/2016.
 */
public class LevelOfJoyLoadedEvent {

    private List<LevelOfJoyRow> levelOfJoyHistory;

    public LevelOfJoyLoadedEvent(List<LevelOfJoyRow> levelOfJoyHistory) {
        this.levelOfJoyHistory = levelOfJoyHistory;
    }

    public List<LevelOfJoyRow> getHistory(){
        return levelOfJoyHistory;
    }
}
