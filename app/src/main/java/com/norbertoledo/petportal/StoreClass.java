package com.norbertoledo.petportal;

import android.app.Application;

public class StoreClass extends Application {

    private String email;
    private String userToken;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUserToken() {
        return userToken;
    }

    public void setUserToken(String userToken) {
        this.userToken = userToken;
    }
}
