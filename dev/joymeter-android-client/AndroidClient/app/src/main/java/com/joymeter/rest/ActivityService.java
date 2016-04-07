package com.joymeter.rest;

import com.joymeter.dto.Activities;
import com.joymeter.dto.ActivityDTO;
import com.joymeter.dto.SyncupActions;

import retrofit.Callback;
import retrofit.ResponseCallback;
import retrofit.http.Body;
import retrofit.http.DELETE;
import retrofit.http.GET;
import retrofit.http.POST;
import retrofit.http.PUT;
import retrofit.http.Path;

/**
 * Created by hramovecchi on 10/08/2015.
 */
public interface ActivityService {

    String activityPath = "/api/activities";

    @GET(activityPath)
    void getActivities(Callback<Activities> callback);

    @GET(activityPath + "/{id}")
    void getActivity(@Path("id") long id, Callback<ActivityDTO> callback);

    @POST(activityPath)
    void addOrUpdateActivity(@Body ActivityDTO activity, Callback<ActivityDTO> callback);

    @PUT(activityPath + "/{id}")
    void updateActivity(@Path("id") long id, @Body ActivityDTO activity, Callback<ActivityDTO> callback);

    @DELETE(activityPath + "/{id}")
    void deleteActivity(@Path("id") long id, ResponseCallback callback);

    @POST(activityPath + "/syncup")
    void syncupToServer(@Body SyncupActions syncupActions, ResponseCallback callback);
}
