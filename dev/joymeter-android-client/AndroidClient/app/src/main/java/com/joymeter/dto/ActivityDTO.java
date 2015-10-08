package com.joymeter.dto;

import com.google.gson.annotations.Expose;

/**
 * Created by hramovecchi on 10/08/2015.
 */
public class ActivityDTO {
    @Expose
    private Long id;
    @Expose
    private String type;
    @Expose
    private String summary;
    @Expose
    private String description;
    @Expose
    private Boolean classified;
    @Expose
    private Integer levelOfJoy;
    @Expose
    private Long startDate;
    @Expose
    private Long endDate;

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
     * The type
     */
    public String getType() {
        return type;
    }

    /**
     *
     * @param type
     * The type
     */
    public void setType(String type) {
        this.type = type;
    }

    /**
     *
     * @return
     * The summary
     */
    public String getSummary() {
        return summary;
    }

    /**
     *
     * @param summary
     * The summary
     */
    public void setSummary(String summary) {
        this.summary = summary;
    }

    /**
     *
     * @return
     * The description
     */
    public String getDescription() {
        return description;
    }

    /**
     *
     * @param description
     * The description
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     *
     * @return
     * The classified
     */
    public Boolean getClassified() {
        return classified;
    }

    /**
     *
     * @param classified
     * The classified
     */
    public void setClassified(Boolean classified) {
        this.classified = classified;
    }

    /**
     *
     * @return
     * The levelOfJoy
     */
    public Integer getLevelOfJoy() {
        return levelOfJoy;
    }

    /**
     *
     * @param levelOfJoy
     * The levelOfJoy
     */
    public void setLevelOfJoy(Integer levelOfJoy) {
        this.levelOfJoy = levelOfJoy;
    }

    /**
     *
     * @return
     * The startDate
     */
    public Long getStartDate() {
        return startDate;
    }

    /**
     *
     * @param startDate
     * The startDate
     */
    public void setStartDate(Long startDate) {
        this.startDate = startDate;
    }

    /**
     *
     * @return
     * The endDate
     */
    public Long getEndDate() {
        return endDate;
    }

    /**
     *
     * @param endDate
     * The endDate
     */
    public void setEndDate(Long endDate) {
        this.endDate = endDate;
    }
}
