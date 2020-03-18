package com.norbertoledo.petportal.models;

public class User {

    private static User instance;

    private String email;
    private String name;
    private String token;
    private String city;

    public static User getInstance(){
        if(instance == null){
            instance = new User();
        }
        return instance;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }


    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
