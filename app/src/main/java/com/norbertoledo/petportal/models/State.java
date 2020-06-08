package com.norbertoledo.petportal.models;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class State {

    private String name;
    private List<String> cities;
    private Map<String, Double> latlng = new HashMap<String, Double>();

    public State(String name, List<String> cities, Map<String, Double> latlng) {
        this.name = name;
        this.cities = cities;
        this.latlng = latlng;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<String> getCities() {
        return cities;
    }

    public void setCities(List<String> cities) {
        this.cities = cities;
    }

    public Map<String, Double> getLatlng() {
        return latlng;
    }

    public void setLatlng(Map<String, Double> latlng) {
        this.latlng = latlng;
    }

}
