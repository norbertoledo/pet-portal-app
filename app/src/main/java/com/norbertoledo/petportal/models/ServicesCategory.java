package com.norbertoledo.petportal.models;

public class ServicesCategory {

    private String id;
    private String name;
    private String image;
    private String color;
    private boolean isActive;

    public ServicesCategory(String id, String name, String image, String color, boolean isActive) {
        this.id = id;
        this.name = name;
        this.image = image;
        this.color = color;
        this.isActive = isActive;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }
}
