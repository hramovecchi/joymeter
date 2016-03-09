package com.joymeter.events.bus;

import com.squareup.otto.Bus;
import com.squareup.otto.ThreadEnforcer;

/**
 * Created by hramovecchi on 03/03/2016.
 */
public class EventsBus {

    private static Bus instance = null;

    private EventsBus(){}

    public static Bus getInstance(){
        if (instance == null){
            instance = new Bus(ThreadEnforcer.ANY);
        }
        return instance;
    }
}
