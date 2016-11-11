package com.joymeter.events.bus;

/**
 * Created by hramovecchi on 16/06/2016.
 */
public class SuggestActivityEvent {

    private long datePicked;

    public SuggestActivityEvent(long datePicked){
        this.datePicked = datePicked;
    }

    public long getDatePicked(){
        return datePicked;
    }
}
