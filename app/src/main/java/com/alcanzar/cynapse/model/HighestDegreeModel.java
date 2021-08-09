package com.alcanzar.cynapse.model;
public class HighestDegreeModel {
    private String degree_id = "";
    private String degree_name="";
    //TODO: constructors
    public HighestDegreeModel(String degree_id,String degree_name){
           this.degree_id = degree_id;
           this.degree_name = degree_name;
    }
    public String getDegree_id() {
        return degree_id;
    }
    public void setDegree_id(String degree_id) {
        this.degree_id = degree_id;
    }
    public String getDegree_name() {
        return degree_name;
    }
    public void setDegree_name(String degree_name) {
        this.degree_name = degree_name;
    }
}
