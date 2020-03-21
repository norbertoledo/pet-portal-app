package com.norbertoledo.petportal.repositories.webservice;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.norbertoledo.petportal.models.Link;
import com.norbertoledo.petportal.models.User;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class Webservice {
    private static final String TAG = "WEBSERVICE";
    private static Webservice instance;
    private IWebservice Iws;

    public static Webservice getInstance(){
        if(instance == null){
            instance = new Webservice();
        }
        return instance;
    }

    public Webservice(){
        Iws = WebserviceBuilder.getInstance().create(IWebservice.class);
    }

    public LiveData<List<Link>> getLinksWs(String token){
        final MutableLiveData<List<Link>> listLink = new MutableLiveData<>();
        Iws.getLinksApi(token).enqueue(new Callback<List<Link>>() {
            @Override
            public void onResponse(Call<List<Link>> call, Response<List<Link>> response) {

                if(response.body() != null){
                    listLink.setValue( response.body() );
                }
            }

            @Override
            public void onFailure(Call<List<Link>> call, Throwable t) {
                listLink.setValue(null);
            }
        });
        return listLink;
    }


}
