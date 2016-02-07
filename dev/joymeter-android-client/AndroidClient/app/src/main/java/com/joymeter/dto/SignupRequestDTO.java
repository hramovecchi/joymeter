package com.joymeter.dto;

import com.google.gson.annotations.Expose;

import java.io.Serializable;

/**
 * Created by hramovecchi on 10/08/2015.
 */
public class SignupRequestDTO implements Serializable{

    @Expose
    private String facebookAccessToken;
    @Expose
    private String gcmToken;
    @Expose
    private String deviceId;

    public SignupRequestDTO(String fbAccessToken, String gcmToken, String deviceId){
        this.setFacebookAccessToken(fbAccessToken);
        this.setGcmToken(gcmToken);
        this.setDeviceId(deviceId);
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

    /**
     *
     * @return
     * The deviceId
     */
    public String getDeviceId() {
        return deviceId;
    }

    /**
     *
     * @param deviceId
     * The deviceId
     */
    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }
}
