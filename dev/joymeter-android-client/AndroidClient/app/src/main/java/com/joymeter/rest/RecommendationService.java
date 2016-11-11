package com.joymeter.rest;

import com.joymeter.dto.AdviceDTO;

import retrofit.ResponseCallback;
import retrofit.http.Body;
import retrofit.http.GET;
import retrofit.http.POST;
import retrofit.http.Query;

/**
 * Created by hramovecchi on 09/08/2015.
 */
public interface RecommendationService {

    String recommendationPath = "/api/recommendations";

    @GET(recommendationPath + "/me/suggest")
    void suggestActivity(@Query("timeInMillis")long timeInMillis, ResponseCallback callback);

    @POST(recommendationPath + "/me/accept")
    void acceptAdvice(@Body AdviceDTO advice, ResponseCallback callback);
}
