package com.joymeter.rest.factory;

import com.joymeter.rest.UserService;

/**
 * Created by hramovecchi on 24/08/2015.
 */
public class UserServiceFactory {

    private static UserService instance;

    public static UserService getInstance(){
        if (instance == null){
            instance = JoymeterRestAdapter.getInstance().create(UserService.class);
        }
        return instance;
    }
}
