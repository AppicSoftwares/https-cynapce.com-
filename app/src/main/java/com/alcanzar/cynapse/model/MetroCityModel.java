package com.alcanzar.cynapse.model;

public class MetroCityModel {
    private String id_city = "";
    private String profile_category_name = "";
    private boolean status;

    //TODO: constructors

    public MetroCityModel() {

    }


    public String getProfile_category_name() {
        return profile_category_name;
    }

    public void setProfile_category_name(String profile_category_name) {
        this.profile_category_name = profile_category_name;
    }

    public boolean getStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public String getId_city() {
        return id_city;
    }

    public void setId_city(String id_city) {
        this.id_city = id_city;
    }
}
