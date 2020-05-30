package com.norbertoledo.petportal.repositories.webservice;

import android.app.Application;
import android.util.Log;

import androidx.annotation.MainThread;
import androidx.arch.core.util.Function;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;
import androidx.lifecycle.ViewModelStoreOwner;

import com.norbertoledo.petportal.MainActivity;
import com.norbertoledo.petportal.models.Link;
import com.norbertoledo.petportal.models.User;
import com.norbertoledo.petportal.viewmodels.UserViewModel;

import java.util.ArrayList;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class Webservice {

    private static final String TAG = "WEBSERVICE";
    private static Webservice instance;
    private IWebservice Iws;
    private boolean res;
    private UserViewModel userViewModel;


    public static Webservice getInstance(){
        if(instance == null){
            instance = new Webservice();
        }
        return instance;
    }


    private Webservice(){
        Iws = WebserviceBuilder.getInstance().create(IWebservice.class);

    }


    // GET LINKS
    public LiveData<List<Link>> getLinksWs(String token){
        final MutableLiveData<List<Link>> listLink = new MutableLiveData<>();


        Iws.getLinksApi(token).enqueue(new Callback<List<Link>>() {
            @Override
            public void onResponse(Call<List<Link>> call, Response<List<Link>> response) {
                Log.d(TAG, "RESPONSE CODE OK: "+String.valueOf(response.code()));

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

    // GET USER
    public MutableLiveData<User> getUserWs(String token){
        final MutableLiveData<User> user = new MutableLiveData<>();


        Iws.getUserApi(token).enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                Log.d(TAG, "RESPONSE CODE OK: "+String.valueOf(response.code()));
                if(response.body() != null){
                    user.setValue( response.body() );
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                Log.d(TAG, "RESPONSE BODY ERROR: "+t.getMessage());
                user.setValue(null);
            }
        });
        return user;
    }

    // UPDATE USER

    public MutableLiveData<User> updateUserWs(String token, final User user){
    final MutableLiveData<User> userUpdated = new MutableLiveData<>();

        Iws.updateUserApi(token, user).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

                if(String.valueOf(response.code()).equals("200")){
                    Log.d(TAG, "RESPONSE CODE OK: "+String.valueOf(response.code()));
                    userUpdated.setValue(user);
                }else{
                    Log.d(TAG, "RESPONSE CODE ERROR: "+String.valueOf(response.code()));
                }

            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.d(TAG, "RESPONSE BODY ERROR: "+t.getMessage());
            }
        });
    return userUpdated;
    }

}
