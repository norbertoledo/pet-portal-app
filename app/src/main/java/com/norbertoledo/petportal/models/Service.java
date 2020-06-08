package com.norbertoledo.petportal.models;

import java.util.HashMap;
import java.util.Map;

public class Service {
    private String id;
    private String category;
    private String state;
    private String name;
    private String description;
    private String thumb;
    private String image;
    private String address;
    private int phone;
    private String website;
    private Map<String, Double> latlng = new HashMap<String, Double>();

    public Service(String id, String category, String state, String name, String description, String thumb, String image, String address, int phone, String website, Map<String, Double> latlng) {
        this.id = id;
        this.category = category;
        this.state = state;
        this.name = name;
        this.description = description;
        this.thumb = thumb;
        this.image = image;
        this.address = address;
        this.phone = phone;
        this.website = website;
        this.latlng = latlng;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getThumb() {
        return thumb;
    }

    public void setThumb(String thumb) {
        this.thumb = thumb;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public int getPhone() {
        return phone;
    }

    public void setPhone(int phone) {
        this.phone = phone;
    }

    public String getWebsite() {
        return website;
    }

    public void setWebsite(String website) {
        this.website = website;
    }

    public Map<String, Double> getLatlng() {
        return latlng;
    }

    public void setLatlng(Map<String, Double> latlng) {
        this.latlng = latlng;
    }
}
