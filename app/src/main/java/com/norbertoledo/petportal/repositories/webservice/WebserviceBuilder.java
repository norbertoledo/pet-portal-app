package com.norbertoledo.petportal.repositories.webservice;

import com.norbertoledo.petportal.utils.GlobalVars;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class WebserviceBuilder {

    private static Retrofit instance;
    private static String url = GlobalVars.API_URL;

    // Singleton pattern
    public static Retrofit getInstance(){
        if(instance == null ){
            OkHttpClient okHttpClient = new OkHttpClient.Builder().build();
            instance = new Retrofit.Builder()
                    .baseUrl(url)
                    .addConverterFactory(GsonConverterFactory.create())
                    .client(okHttpClient)
                    .build();
        }
        return instance;
    }
}
