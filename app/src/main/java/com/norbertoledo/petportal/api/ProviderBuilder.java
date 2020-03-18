package com.norbertoledo.petportal.api;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ProviderBuilder {

    private static Retrofit instance;
    // Singleton pattern
    public static Retrofit getInstance(){
        if(instance == null ){
            instance = new Retrofit.Builder()
                    .baseUrl("https://pet-portal.web.app/api/")
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return instance;
    }
}
