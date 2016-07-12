package com.joymeter.rest;

import com.joymeter.dto.AdviceDTO;

import retrofit.ResponseCallback;
import retrofit.http.Body;
import retrofit.http.GET;

/**
 * Created by hramovecchi on 09/08/2015.
 */
public interface RecommendationService {

    String recommendationPath = "/api/recommendations";

    @GET(recommendationPath + "/me/suggest")
    void suggestActivity(ResponseCallback callback);

    @GET(recommendationPath + "/me/suggest")
    void acceptAdvice(@Body AdviceDTO advice, ResponseCallback callback);
}
