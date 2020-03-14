package com.norbertoledo.petportal.Interfaces;

import com.norbertoledo.petportal.Models.Links;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;

public interface PetPortalApi {



        //@GET("links")
        //Call<List<Links>> getLinks();

        @GET("links")
        Call<List<Links>> getLinks(@Header("Authorization") String authorization);

}
