package com.norbertoledo.petportal.models;

public class ReferencePlaces {

    private String category;
    private String color;

    public ReferencePlaces(String category, String color) {
        this.category = category;
        this.color = color;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }
}
