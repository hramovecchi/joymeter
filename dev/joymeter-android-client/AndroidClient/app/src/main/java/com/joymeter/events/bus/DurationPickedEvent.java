package com.joymeter.events.bus;

/**
 * Created by hramo on 11-Nov-16.
 */
public class DurationPickedEvent {

    private int hours;
    private int minutes;

    public DurationPickedEvent(int hours, int minutes){
        this.hours = hours;
        this.minutes = minutes;
    }

    public int getHours(){
        return hours;
    }

    public int getMinutes() {
        return minutes;
    }
}
