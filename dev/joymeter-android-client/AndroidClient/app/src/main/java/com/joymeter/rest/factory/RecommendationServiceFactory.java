package com.joymeter.rest.factory;

import com.joymeter.rest.RecommendationService;

/**
 * Created by hramovecchi on 12/07/2016.
 */
public class RecommendationServiceFactory {

    private static RecommendationService instance;

    public static RecommendationService getInstance(){
        if (instance == null){
            instance = JoymeterRestAdapter.getInstance().create(RecommendationService.class);
        }
        return instance;
    }
}
