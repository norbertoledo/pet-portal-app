package com.norbertoledo.petportal.models;

public class Link {

    private String id;
    private String title;
    private String description;
    private String link;
    private boolean isActive;

    public Link(String id, String title, String description, String link, boolean isActive) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.link = link;
        this.isActive = isActive;
    }

    public Link() {
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

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }
}
