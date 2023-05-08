package com.example.delahuertaalamesa.propertiesproducts;

public class Months {
    private int id_month;
    private String name_month;

    public Months(int id_month, String name_month) {
        this.id_month = id_month;
        this.name_month = name_month;
    }

    public int getId_month() {
        return id_month;
    }

    public void setId_month(int id_month) {
        this.id_month = id_month;
    }

    public String getName_month() {
        return name_month;
    }

    public void setName_month(String name_month) {
        this.name_month = name_month;
    }
}
