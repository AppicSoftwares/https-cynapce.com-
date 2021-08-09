package com.alcanzar.cynapse.model;

public class AddPackageModal {

    private String packageDetails,id;
    boolean colorClick;

    public boolean isColorClick() {
        return colorClick;
    }

    public void setColorClick(boolean colorClick) {
        this.colorClick = colorClick;
    }

    private int price;

    public String getPackageDetails() {
        return packageDetails;
    }

    public void setPackageDetails(String packageDetails) {
        this.packageDetails = packageDetails;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
