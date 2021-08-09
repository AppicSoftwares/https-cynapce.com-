package com.alcanzar.cynapse.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;

public class MarketFilterModal implements Serializable {


    private String dealId="",hospitalType="",practiceType="",tieUpFor="",tieIpSeeking="",tieUpWith="",name="",city=""
            ,specializations="",dealType="",distance="",categoryType="";

    public MarketFilterModal() {

    }

    public String getCategoryType() {
        return categoryType;
    }

    public void setCategoryType(String categoryType) {
        this.categoryType = categoryType;
    }



    public String getDealId() {
        return dealId;
    }

    public void setDealId(String dealId) {
        this.dealId = dealId;
    }

    public String getHospitalType() {
        return hospitalType;
    }

    public void setHospitalType(String hospitalType) {
        this.hospitalType = hospitalType;
    }

    public String getPracticeType() {
        return practiceType;
    }

    public void setPracticeType(String practiceType) {
        this.practiceType = practiceType;
    }

    public String getTieUpFor() {
        return tieUpFor;
    }

    public void setTieUpFor(String tieUpFor) {
        this.tieUpFor = tieUpFor;
    }

    public String getTieIpSeeking() {
        return tieIpSeeking;
    }

    public void setTieIpSeeking(String tieIpSeeking) {
        this.tieIpSeeking = tieIpSeeking;
    }

    public String getTieUpWith() {
        return tieUpWith;
    }

    public void setTieUpWith(String tieUpWith) {
        this.tieUpWith = tieUpWith;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getSpecializations() {
        return specializations;
    }

    public void setSpecializations(String specializations) {
        this.specializations = specializations;
    }

    public String getDealType() {
        return dealType;
    }

    public void setDealType(String dealType) {
        this.dealType = dealType;
    }

    public String getDistance() {
        return distance;
    }

    public void setDistance(String distance) {
        this.distance = distance;
    }


}
