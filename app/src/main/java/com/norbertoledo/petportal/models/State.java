package com.norbertoledo.petportal.models;

import java.util.List;

public class State {

    String name;
    List<String> cities;

    public State(String name, List<String> cities) {
        this.name = name;
        this.cities = cities;
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
}
