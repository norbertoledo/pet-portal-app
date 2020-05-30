package com.norbertoledo.petportal.repositories;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.norbertoledo.petportal.models.User;
import com.norbertoledo.petportal.repositories.webservice.IWebservice;
import com.norbertoledo.petportal.repositories.webservice.Webservice;
import com.norbertoledo.petportal.repositories.webservice.WebserviceBuilder;
import com.norbertoledo.petportal.viewmodels.UserViewModel;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UserRepo {

    private static final String TAG = "USER REPO";

    public static UserRepo instance;
    private MutableLiveData<User> user;
    private Boolean conn = true;
    private Webservice ws;
    private boolean res;
    private IWebservice Iws;

    public static UserRepo getInstance() {
        if (instance == null) {
            instance = new UserRepo();
        }
        return instance;
    }

    public UserRepo() {
        //Iws = WebserviceBuilder.getInstance().create(IWebservice.class);
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

    public MutableLiveData<User> newUserRepo() {

        if (conn) {
            // Load data from webservice
            user = new MutableLiveData<User>();
        } else {
            // Load data from cache / Local DB
            user = null;
        }
        return user;
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
}