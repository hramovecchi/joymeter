package com.joymeter.dto;

import com.google.gson.annotations.Expose;

/**
 * Created by hramovecchi on 10/08/2015.
 */
public class SignupResponseDTO {

    @Expose
    private UserDTO user;

    @Expose
    private String gcmToken;

    @Expose
    private String sessionToken;

    public UserDTO getUser() {
        return user;
    }

    public void setUser(UserDTO user) {
        this.user = user;
    }

    public String getGcmToken() {
        return gcmToken;
    }

    public void setGcmToken(String gcmToken) {
        this.gcmToken = gcmToken;
    }

    public String getSessionToken() {
        return sessionToken;
    }

    public void setSessionToken(String sessionToken) {
        this.sessionToken = sessionToken;
    }
}
