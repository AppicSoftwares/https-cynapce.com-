package com.alcanzar.cynapse.model;

import java.io.Serializable;

public class JobDetailsModel implements Serializable {
    private String medical_profile_id = "";
    private String job_title_id = "";
    private String job_id = "";
    private String detail_id = "";
    private String recommend = "";

    public String getMedical_profile_id() {
        return medical_profile_id;
    }

    public void setMedical_profile_id(String medical_profile_id) {
        this.medical_profile_id = medical_profile_id;
    }

    public String getJob_title_id() {
        return job_title_id;
    }

    public void setJob_title_id(String job_title_id) {
        this.job_title_id = job_title_id;
    }

    public String getJob_id() {
        return job_id;
    }

    public void setJob_id(String job_id) {
        this.job_id = job_id;
    }

    public String getDetail_id() {
        return detail_id;
    }

    public void setDetail_id(String detail_id) {
        this.detail_id = detail_id;
    }

    public String getRecommend() {
        return recommend;
    }

    public void setRecommend(String recommend) {
        this.recommend = recommend;
    }

    public JobDetailsModel(String medical_profile_id, String job_title_id, String job_id, String detail_id, String recommend) {
        this.medical_profile_id = medical_profile_id;
        this.job_title_id = job_title_id;
        this.job_id = job_id;
        this.detail_id = detail_id;
        this.recommend = recommend;
    }

    public JobDetailsModel() {
    }
}
