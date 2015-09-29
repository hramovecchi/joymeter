package com.joymeter.rest.factory;

import com.joymeter.rest.SessionService;

/**
 * Created by hramovecchi on 24/08/2015.
 */
public class SessionServiceFactory {

    private static SessionService instance;

    public static SessionService getInstance(){
        if (instance == null){
            instance =  JoymeterRestAdapter.getInstance().create(SessionService.class);
        }
        return instance;
    }
}
