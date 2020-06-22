package com.norbertoledo.petportal.models;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class State {

    private String id;
    private String name;
    private Map<String, Double> latlng = new HashMap<String, Double>();
    private boolean isActive;

    public State(String id, String name, Map<String, Double> latlng, boolean isActive) {
        this.id = id;
        this.name = name;
        this.latlng = latlng;
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

    public Map<String, Double> getLatlng() {
        return latlng;
    }

    public void setLatlng(Map<String, Double> latlng) {
        this.latlng = latlng;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }
}
