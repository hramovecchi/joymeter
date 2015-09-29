package com.joymeter.dto;

import com.google.gson.annotations.Expose;

/**
 * Created by hramovecchi on 10/08/2015.
 */
public class SignupRequestDTO {

    @Expose
    private String facebookAccessToken;
    @Expose
    private String gcmToken;

    public SignupRequestDTO(String fbAccessToken, String gcmToken){
        this.setFacebookAccessToken(fbAccessToken);
        this.setGcmToken(gcmToken);
    }

    /**
     *
     * @return
     * The facebookAccessToken
     */
    public String getFacebookAccessToken() {
        return facebookAccessToken;
    }

    /**
     *
     * @param facebookAccessToken
     * The facebookAccessToken
     */
    public void setFacebookAccessToken(String facebookAccessToken) {
        this.facebookAccessToken = facebookAccessToken;
    }

    /**
     *
     * @return
     * The gcmToken
     */
    public String getGcmToken() {
        return gcmToken;
    }

    /**
     *
     * @param gcmToken
     * The gcmToken
     */
    public void setGcmToken(String gcmToken) {
        this.gcmToken = gcmToken;
    }
}
