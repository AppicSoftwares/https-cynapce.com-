package com.alcanzar.cynapse.model;
public class GetCategoryModel {
    private String id_category = "";
    private String category_name = "";

    public GetCategoryModel(String id_category, String category_name) {
        this.id_category = id_category;
        this.category_name = category_name;

    }
    public String getId_category() {
        return id_category;
    }
    public void setId_category(String id_category) {
        this.id_category = id_category;
    }
    public String getCategory_name() {
        return category_name;
    }
    public void setCategory_name(String category_name) {
        this.category_name = category_name;
    }

}
