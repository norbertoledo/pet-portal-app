package com.norbertoledo.petportal.Interface;

import com.norbertoledo.petportal.Model.Links;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;

public interface PetPortalApi {



        //@GET("links")
        //Call<List<Links>> getLinks();

        @GET("links")
        Call<List<Links>> getPosts(@Header("Authorization") String authorization);

}
