package com.alcanzar.cynapse.model;
public class PackagesModel {
 private String package_id="";
 private String package_name="";
 private String package_total_price="";
 private String package_status="";
 private String package_sync_time="";

    public PackagesModel(String package_id, String package_name, String package_total_price, String package_status, String package_sync_time) {
        this.package_id = package_id;
        this.package_name = package_name;
        this.package_total_price = package_total_price;
        this.package_status = package_status;
        this.package_sync_time = package_sync_time;
    }

    public PackagesModel() {
    }

    public String getPackage_id() {
        return package_id;
    }

    public void setPackage_id(String package_id) {
        this.package_id = package_id;
    }

    public String getPackage_name() {
        return package_name;
    }

    public void setPackage_name(String package_name) {
        this.package_name = package_name;
    }

    public String getPackage_total_price() {
        return package_total_price;
    }

    public void setPackage_total_price(String package_total_price) {
        this.package_total_price = package_total_price;
    }

    public String getPackage_status() {
        return package_status;
    }

    public void setPackage_status(String package_status) {
        this.package_status = package_status;
    }

    public String getPackage_sync_time() {
        return package_sync_time;
    }

    public void setPackage_sync_time(String package_sync_time) {
        this.package_sync_time = package_sync_time;
    }
}
