package com.norbertoledo.petportal.models;

import java.util.HashMap;
import java.util.Map;

public class Place {

    private String id;
    private String title;
    private String caption;
    private String description;
    private String category;
    private String color;
    private String image;
    private Map<String, Double> latlng = new HashMap<String, Double>();

    public Place(String id, String title, String caption, String description, String category, Map<String, Double> latlng, String color, String image) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.category = category;
        this.latlng = latlng;
        this.color = color;
        this.caption = caption;
        this.image = image;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
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

    public Map<String, Double> getLatlng() {
        return latlng;
    }

    public void setLatlng(Map<String, Double> latlng) {
        this.latlng = latlng;
    }

    public String getCaption() {
        return caption;
    }

    public void setCaption(String caption) {
        this.caption = caption;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
