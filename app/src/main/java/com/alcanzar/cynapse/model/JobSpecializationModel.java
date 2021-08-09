package com.alcanzar.cynapse.model;

public class JobSpecializationModel  {
    private String specialization_id = "";
    private String profile_category_id="";
    private String specialization_name="";
    private boolean status;
    //TODO: constructors
    public JobSpecializationModel(String specialization_id,String profile_category_id,String specialization_name){
        this.specialization_id=specialization_id;
        this.profile_category_id=profile_category_id;
        this.specialization_name=specialization_name;
    }
    public JobSpecializationModel()
    {

    }
    public String getSpecialization_id() {
        return specialization_id;
    }
    public void setSpecialization_id(String specialization_id) {
        this.specialization_id = specialization_id;
    }
    public String getProfile_category_id() {
        return profile_category_id;
    }
    public void setProfile_category_id(String profile_category_id) {
        this.profile_category_id = profile_category_id;
    }
    public String getSpecialization_name() {
        return specialization_name;
    }
    public void setSpecialization_name(String specialization_name) {
        this.specialization_name = specialization_name;
    }
    public boolean getStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }
}
