package com.joymeter.rest;

import com.joymeter.dto.UserDTO;

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
    UserDTO getUser(@Path("id") long userId);

    @PUT(userPath + "/{id}")
    void updateUser(@Path("id") long userId, @Body UserDTO user);

}
