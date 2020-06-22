package com.norbertoledo.petportal.models;

import java.util.Date;

public class Tip {
    private String id;
    private String title;
    private String caption;
    private String description;
    private String image;
    private String thumb;
    private String postedAt;
    private boolean isActive;

    public Tip(String id, String title, String caption, String description, String image, String thumb, String postedAt, boolean isActive) {
        this.id = id;
        this.title = title;
        this.caption = caption;
        this.description = description;
        this.image = image;
        this.thumb = thumb;
        this.postedAt = postedAt;
        this.isActive = isActive;
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

    public String getCaption() {
        return caption;
    }

    public void setCaption(String caption) {
        this.caption = caption;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getThumb() {
        return thumb;
    }

    public void setThumb(String thumb) {
        this.thumb = thumb;
    }

    public String getPostedAt() {
        return postedAt;
    }

    public void setPostedAt(String postedAt) {
        this.postedAt = postedAt;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }
}
