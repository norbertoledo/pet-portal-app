package com.norbertoledo.petportal.repositories;


import androidx.lifecycle.MutableLiveData;

import com.norbertoledo.petportal.models.User;
import com.norbertoledo.petportal.repositories.webservice.Webservice;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;


public class UserRepo {

    private static final String TAG = "USER REPO";

    public static UserRepo instance;
    private MutableLiveData<User> user;
    private Boolean conn = true;
    private Webservice ws;

    public static UserRepo getInstance() {
        if (instance == null) {
            instance = new UserRepo();
        }
        return instance;
    }


    public MutableLiveData<User> getUserRepo(final String token) {

        if (conn) {
            // Load data from webservice
            ws = Webservice.getInstance();
            user = ws.getUserWs(token);
        } else {
            // Load data from cache / Local DB
            user = null;
        }

        return user;
    }

    public MutableLiveData<User> newUserRepo(String token, User user) {

        if (conn) {
            // Load data from webservice
            ws = Webservice.getInstance();
            return ws.newUserWs(token, user);
        } else {
            // Load data from cache / Local DB
            return null;
        }

    }

    public MutableLiveData<User> updateUserRepo(final String token, final User user) {

        if(conn){
            // Load data from webservice
            ws = Webservice.getInstance();
            return ws.updateUserWs(token, user);
        }else{
            // Load data from cache / Local DB
            return null;
        }

    }

    public MutableLiveData<User> updateUserImageRepo(final String token, MultipartBody.Part requestImage, RequestBody imageData) {

        if(conn){
            // Load data from webservice
            ws = Webservice.getInstance();
            return ws.updateUserImageWs(token, requestImage, imageData);
        }else{
            // Load data from cache / Local DB
            return null;
        }

    }
}