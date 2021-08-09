package com.alcanzar.cynapse.model;
public class ConferenceTypeModel {
 private String conference_type_id="";

 private String conference_type_name="";

    public ConferenceTypeModel() {
    }

    public String getConference_type_id() {
        return conference_type_id;
    }

    public void setConference_type_id(String conference_type_id) {
        this.conference_type_id = conference_type_id;
    }

    public String getConference_type_name() {
        return conference_type_name;
    }

    public void setConference_type_name(String conference_type_name) {
        this.conference_type_name = conference_type_name;
    }

    public ConferenceTypeModel(String conference_type_id, String conference_type_name) {
        this.conference_type_id = conference_type_id;
        this.conference_type_name = conference_type_name;
    }
}
