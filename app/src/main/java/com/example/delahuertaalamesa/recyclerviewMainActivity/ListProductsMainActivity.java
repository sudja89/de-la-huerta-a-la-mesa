package com.example.delahuertaalamesa.recyclerviewMainActivity;

public class ListProductsMainActivity {
    private int id_product;
    private String picture_name;
    private String common_name;
    private String submit;
    private String properties;
    private String production;
    private String curiosities;
    private int picture;
    private boolean favorite;

    public ListProductsMainActivity(int id_product, String picture_name, String common_name, String submit, String properties, String production, String curiosities, int picture) {
        this.id_product = id_product;
        this.picture_name = picture_name;
        this.common_name = common_name;
        this.submit = submit;
        this.properties = properties;
        this.production = production;
        this.curiosities = curiosities;
        this.picture = picture;
    }

    public int getId_product() {
        return id_product;
    }

    public void setId_product(int id_product) {
        this.id_product = id_product;
    }

    public String getPicture_name() {
        return picture_name;
    }

    public void setPicture_name(String picture_name) {
        this.picture_name = picture_name;
    }

    public String getCommon_name() {
        return common_name;
    }

    public void setCommon_name(String common_name) {
        this.common_name = common_name;
    }

    public String getSubmit() {
        return submit;
    }

    public void setSubmit(String submit) {
        this.submit = submit;
    }

    public String getProperties() {
        return properties;
    }

    public void setProperties(String properties) {
        this.properties = properties;
    }

    public String getProduction() {
        return production;
    }

    public void setProduction(String production) {
        this.production = production;
    }

    public String getCuriosities() {
        return curiosities;
    }

    public void setCuriosities(String curiosities) {
        this.curiosities = curiosities;
    }

    public int getPicture() {
        return picture;
    }

    public void setPicture(int picture) {
        this.picture = picture;
    }

    public boolean isFavorite() {
        return favorite;
    }

    public void setFavorite(boolean favorite) {
        this.favorite = favorite;
    }

}
