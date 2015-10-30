package com.joymeter.rest;

import com.joymeter.dto.UserDTO;

import retrofit.Callback;
import retrofit.http.Body;
import retrofit.http.GET;
import retrofit.http.PUT;
import retrofit.http.Path;

/**
 * Created by hramovecchi on 09/08/2015.
 */
public interface UserService {

    String userPath = "/api/users";

    @GET(userPath + "/{id}")
    void getUser(@Path("id") long userId, Callback<UserDTO> callback);

    @PUT(userPath + "/{id}")
    void updateUser(@Path("id") long userId, @Body UserDTO user, Callback<UserDTO> callback);

}
