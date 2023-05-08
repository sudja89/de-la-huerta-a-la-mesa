package com.example.delahuertaalamesa.sortViewsProducts;

public class Favorites {
    private int id_product;
    private int id_user;

    public Favorites(int id_product, int id_user) {
        this.id_product = id_product;
        this.id_user = id_user;
    }

    public int getId_product() {
        return id_product;
    }

    public void setId_product(int id_product) {
        this.id_product = id_product;
    }

    public int getId_user() {
        return id_user;
    }

    public void setId_user(int id_user) {
        this.id_user = id_user;
    }
}
