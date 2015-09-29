package com.joymeter.dto;

import com.google.gson.annotations.Expose;

/**
 * Created by hramovecchi on 10/08/2015.
 */
public class UserDTO {

    @Expose
    private Long id;
    @Expose
    private String email;
    @Expose
    private Long creationDate;
    @Expose
    private String facebookAccessToken;
    @Expose
    private String fullName;

    /**
     *
     * @return
     * The id
     */
    public Long getId() {
        return id;
    }

    /**
     *
     * @param id
     * The id
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     *
     * @return
     * The email
     */
    public String getEmail() {
        return email;
    }

    /**
     *
     * @param email
     * The email
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     *
     * @return
     * The creationDate
     */
    public Long getCreationDate() {
        return creationDate;
    }

    /**
     *
     * @param creationDate
     * The creationDate
     */
    public void setCreationDate(Long creationDate) {
        this.creationDate = creationDate;
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
     * The fullName
     */
    public String getFullName() {
        return fullName;
    }

    /**
     *
     * @param fullName
     * The fullName
     */
    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

}
