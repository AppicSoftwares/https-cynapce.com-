package com.alcanzar.cynapse.model;

public class MedicalProfileModel {

    private String id = "";
    private String profile_category_name= "";
    private boolean status;

    //TODO: constructors
    public MedicalProfileModel(String id,String profile_category_name){
        this.id=id;
        this.profile_category_name=profile_category_name;

    }
    public MedicalProfileModel()
    {

    }
    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
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
}
