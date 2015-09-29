package com.joymeter.rest;

import com.joymeter.dto.SignupRequestDTO;
import com.joymeter.dto.SignupResponseDTO;

import retrofit.Callback;
import retrofit.http.Body;
import retrofit.http.POST;

/**
 * Created by PwifiUser on 10/08/2015.
 */
public interface SessionService {

    String sessionPath = "/api/sessions";

    @POST(sessionPath)
    void createUser(@Body SignupRequestDTO signupRequest, Callback<SignupResponseDTO> callback);

}
