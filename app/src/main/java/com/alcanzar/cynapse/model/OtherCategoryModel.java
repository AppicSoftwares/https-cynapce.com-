package com.alcanzar.cynapse.model;

public class OtherCategoryModel {
    private String id="";
    private String status="";
    private String name="";
    private String type_id="";

    public OtherCategoryModel()
    {

    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name=name;
    }
    public String getType_id() {
        return type_id;
    }

    public void setType_id(String type_id) {
        this.type_id = type_id;
    }
}
