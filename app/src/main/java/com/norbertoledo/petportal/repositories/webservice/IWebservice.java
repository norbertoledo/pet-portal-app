package com.norbertoledo.petportal.repositories.webservice;

import com.norbertoledo.petportal.models.Link;
import com.norbertoledo.petportal.models.Place;
import com.norbertoledo.petportal.models.ServicesCategory;
import com.norbertoledo.petportal.models.State;
import com.norbertoledo.petportal.models.Tip;
import com.norbertoledo.petportal.models.User;

import java.util.List;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Part;
import retrofit2.http.Path;

public interface IWebservice {



        @GET("services/categories")
        Call<List<ServicesCategory>> getServicesCategoryApi(
                @Header("Authorization") String authorization
        );

        @GET("places")
        Call<List<Place>> getPlacesApi(
                @Header("Authorization") String authorization
        );

        @GET("tips")
        Call<List<Tip>> getTipsApi(
                @Header("Authorization") String authorization
        );

        @GET("tips/{id}")
        Call<Tip> getTipApi(
                @Header("Authorization") String authorization,
                @Path("id") String id
        );

        @GET("states")
        Call<List<State>> getStatesApi(
                @Header("Authorization") String authorization
        );

        @GET("links")
        Call<List<Link>> getLinksApi(
                @Header("Authorization") String authorization
        );


        @GET("users")
        Call<User> getUserApi(
                @Header("Authorization") String authorization
        );


        @POST("users")
        Call<User> createUserApi(
                @Header("Authorization") String authorization,
                @Body User userData
        );


        @PUT("users")
        Call<ResponseBody> updateUserApi(
                @Header("Authorization") String authorization,
                @Body User userData
        );


        @Multipart
        @POST("uploads")
        Call<User> updateUserImageApi(
                @Header("Authorization") String authorization,
                @Part MultipartBody.Part image,
                @Part("imageData") RequestBody imageData
        );

}
