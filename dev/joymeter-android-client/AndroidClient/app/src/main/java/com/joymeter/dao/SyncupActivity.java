package com.joymeter.dao;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT. Enable "keep" sections if you want to edit. 
/**
 * Entity mapped to table "SYNCUP_ACTIVITY".
 */
public class SyncupActivity {

    private Long id;
    private String type;
    private String summary;
    private String description;
    private Boolean classified;
    private Integer levelOfJoy;
    private Long startDate;
    private Long endDate;

    public SyncupActivity() {
    }

    public SyncupActivity(Long id) {
        this.id = id;
    }

    public SyncupActivity(Long id, String type, String summary, String description, Boolean classified, Integer levelOfJoy, Long startDate, Long endDate) {
        this.id = id;
        this.type = type;
        this.summary = summary;
        this.description = description;
        this.classified = classified;
        this.levelOfJoy = levelOfJoy;
        this.startDate = startDate;
        this.endDate = endDate;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Boolean getClassified() {
        return classified;
    }

    public void setClassified(Boolean classified) {
        this.classified = classified;
    }

    public Integer getLevelOfJoy() {
        return levelOfJoy;
    }

    public void setLevelOfJoy(Integer levelOfJoy) {
        this.levelOfJoy = levelOfJoy;
    }

    public Long getStartDate() {
        return startDate;
    }

    public void setStartDate(Long startDate) {
        this.startDate = startDate;
    }

    public Long getEndDate() {
        return endDate;
    }

    public void setEndDate(Long endDate) {
        this.endDate = endDate;
    }

}
