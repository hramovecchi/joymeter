package com.joymeter.rest.factory;

import com.joymeter.rest.ActivityService;

/**
 * Created by hramovecchi on 24/08/2015.
 */
public class ActivityServiceFactory {

    private static ActivityService instance = null;

    public static ActivityService getInstance(){
        if (instance == null){
            instance = JoymeterRestAdapter.getInstance().create(ActivityService.class);
        }
        return instance;
    }
}
