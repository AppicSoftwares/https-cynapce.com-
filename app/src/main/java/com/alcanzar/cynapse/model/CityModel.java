package com.alcanzar.cynapse.model;
public class CityModel {
 private String city_id="";
 private String country_code="";
 private String state_code="";
 private String city_name="";
    private boolean selectedCheck=false;
 public CityModel(String city_id,String country_code,String state_code,String city_name){
     this.city_id =city_id;
     this.country_code =country_code;
     this.state_code =state_code;
     this.city_name=city_name;
    }


    public void setSelectedCheck(boolean selectedCheck) {
        this.selectedCheck = selectedCheck;
    }

    public boolean isSelectedCheck() {

        return selectedCheck;
    }
    public String getCity_id() {
        return city_id;
    }
    public void setCity_id(String city_id) {
        this.city_id = city_id;
    }
    public String getState_code() {
        return state_code;
    }
    public void setState_code(String state_code) {
        this.state_code = state_code;
    }
    public String getCountry_code() {
        return country_code;
    }
    public void setCountry_code(String country_code) {
        this.country_code = country_code;
    }
    public String getCity_name() {
     return city_name;
    }
    public void setCity_name(String city_name) {
        this.city_name = city_name;
    }
}
