package com.norbertoledo.petportal.repositories.webservice;

import com.norbertoledo.petportal.models.Link;
import com.norbertoledo.petportal.models.User;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;

public interface IWebservice {


        @GET("links")
        Call<List<Link>> getLinksApi(@Header("Authorization") String authorization);

        @POST("users")
        Call<User> createUser(@Header("Authorization") String authorization, @Body User userData);

}
