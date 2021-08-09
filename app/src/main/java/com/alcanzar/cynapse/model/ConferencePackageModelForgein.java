package com.alcanzar.cynapse.model;

public class ConferencePackageModelForgein {
    private String conference_pack_id="";
    private String conference_pack_day="";
    private String conference_pack_charge="";
    private String isSelect="0";

    public ConferencePackageModelForgein() {
    }

    public ConferencePackageModelForgein(String conference_pack_id, String conference_pack_day, String conference_pack_charge) {
        this.conference_pack_id = conference_pack_id;
        this.conference_pack_day = conference_pack_day;
        this.conference_pack_charge = conference_pack_charge;

    }

    public String getConference_pack_id() {
        return conference_pack_id;
    }

    public void setConference_pack_id(String conference_pack_id) {
        this.conference_pack_id = conference_pack_id;
    }

    public String getConference_pack_day() {
        return conference_pack_day;
    }

    public void setConference_pack_day(String conference_pack_day) {
        this.conference_pack_day = conference_pack_day;
    }

    public String getConference_pack_charge() {
        return conference_pack_charge;
    }

    public void setConference_pack_charge(String conference_pack_charge) {
        this.conference_pack_charge = conference_pack_charge;
    }

    public String getIsSelect() {
        return isSelect;
    }

    public void setIsSelect(String isSelect) {
        this.isSelect = isSelect;
    }
}

