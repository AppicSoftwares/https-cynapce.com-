package com.alcanzar.cynapse.model;
public class StateModel {
    private String country_code = "";
    private String state_code = "";
    private String state_name = "";
    private boolean selectedCheck=false;

    public boolean isSelectedCheck() {
        return selectedCheck;
    }

    public void setSelectedCheck(boolean selectedCheck) {
        this.selectedCheck = selectedCheck;
    }



    public StateModel(String country_code, String state_code, String state_name) {
        this.country_code = country_code;
        this.state_code = state_code;
        this.state_name = state_name;
    }
    public String getCountry_code() {
        return country_code;
    }
    public void setCountry_code(String country_code) {
        this.country_code = country_code;
    }
    public String getState_code() {
        return state_code;
    }
    public void setState_code(String state_code) {
        this.state_code = state_code;
    }
    public String getState_name() {
        return state_name;
    }
    public void setState_name(String state_name) {
        this.state_name = state_name;
    }

}
