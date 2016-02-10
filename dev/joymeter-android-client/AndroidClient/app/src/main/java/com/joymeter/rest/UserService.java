package com.joymeter.rest;

import com.joymeter.dto.UserDTO;

import retrofit.Callback;
import retrofit.ResponseCallback;
import retrofit.http.Body;
import retrofit.http.GET;
import retrofit.http.PUT;

/**
 * Created by hramovecchi on 09/08/2015.
 */
public interface UserService {

    String userPath = "/api/users";

    @GET(userPath + "/me")
    void getUser(Callback<UserDTO> callback);

    @PUT(userPath + "/me")
    void updateUser(@Body UserDTO user, Callback<UserDTO> callback);

    @GET(userPath + "/me/suggest")
    void suggestActivity(ResponseCallback callback);
}
