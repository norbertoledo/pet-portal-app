package com.norbertoledo.petportal.models;

public class Link {

    private String nombre;
    private String link;

    public Link(String nombre, String link) {
        this.nombre = nombre;
        this.link = link;
    }

    public Link() {
    }

    public String getNombre() {
        return nombre;
    }

    public String getLink() {
        return link;
    }
}
