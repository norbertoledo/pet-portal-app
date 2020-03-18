package com.norbertoledo.petportal.api;

import com.norbertoledo.petportal.models.Links;
import com.norbertoledo.petportal.models.User;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;

public interface IApiRequest {


        @GET("links")
        Call<List<Links>> getLinks(@Header("Authorization") String authorization);

        @POST("users")
        Call<User> createUser(@Header("Authorization") String authorization, @Body User userData);

}
