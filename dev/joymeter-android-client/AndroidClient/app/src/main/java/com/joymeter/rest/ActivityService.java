package com.joymeter.rest;

import com.joymeter.dto.Activities;
import com.joymeter.dto.ActivityDTO;

import retrofit.Callback;
import retrofit.http.Body;
import retrofit.http.DELETE;
import retrofit.http.GET;
import retrofit.http.POST;
import retrofit.http.PUT;
import retrofit.http.Path;
import retrofit.http.Query;

/**
 * Created by hramovecchi on 10/08/2015.
 */
public interface ActivityService {

    String activityPath = "/api/activities";

    @GET(activityPath)
    void getActivities(@Query("user_id") long userId, Callback<Activities> callback);

    @GET(activityPath + "/{id}")
    ActivityDTO getActivity(@Path("id") long id);

    @POST(activityPath)
    void addActivity(@Body ActivityDTO activity);

    @PUT(activityPath + "/{id}")
    void updateActivity(@Body ActivityDTO activity);

    @DELETE(activityPath + "/{id}")
    void deleteActivity();

}
