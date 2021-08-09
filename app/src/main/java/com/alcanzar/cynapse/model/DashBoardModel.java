package com.alcanzar.cynapse.model;

public class DashBoardModel {
    //TODO: Fields
    private String id = "";
    private String image = "";
    private String time = "";
    private String msgType = "";

    //TODO: constructors
    public DashBoardModel(String id, String img) {
        this.id = id;
        this.image = img;


    }

    //TODO: properties
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
    public  String getTime()
    {
        return  time;
    }
}
