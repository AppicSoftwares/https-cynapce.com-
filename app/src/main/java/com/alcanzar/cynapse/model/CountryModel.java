package com.alcanzar.cynapse.model;
public class CountryModel {
   private String country_code ="";
   private String country_name="";
   //TODO: constructor
    public CountryModel(String country_code,String country_name){
        this.country_code = country_code;
        this.country_name = country_name;
        }
    public String getCountry_code() {
        return country_code;
    }
    public void setCountry_code(String country_code) {
        this.country_code = country_code;
    }
    public String getCountry_name() {
        return country_name;
    }
    public void setCountry_name(String country_name) {
        this.country_name = country_name;
    }
}
