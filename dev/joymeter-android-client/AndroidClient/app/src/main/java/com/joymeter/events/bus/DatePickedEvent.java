package com.joymeter.events.bus;

/**
 * Created by hramo on 11-Nov-16.
 */
public class DatePickedEvent {

    private long timeInMillis;

    public DatePickedEvent(long timeInMillis){
        this.timeInMillis = timeInMillis;
    }

    public long getTimeInMillis(){
        return timeInMillis;
    }
}
