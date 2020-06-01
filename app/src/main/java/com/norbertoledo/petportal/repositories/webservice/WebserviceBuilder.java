package com.norbertoledo.petportal.repositories.webservice;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class WebserviceBuilder {

    private static Retrofit instance;
    //private static String url = "http://10.0.2.2:5001/pet-portal/us-central1/api/";
    private static String url = "https://us-central1-pet-portal.cloudfunctions.net/api/";



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
