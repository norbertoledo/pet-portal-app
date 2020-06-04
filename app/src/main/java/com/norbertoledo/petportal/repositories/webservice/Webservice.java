package com.norbertoledo.petportal.repositories.webservice;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.norbertoledo.petportal.models.Link;
import com.norbertoledo.petportal.models.State;
import com.norbertoledo.petportal.models.Tip;
import com.norbertoledo.petportal.models.User;
import com.norbertoledo.petportal.viewmodels.UserViewModel;

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

    // GET TIPS
        final MutableLiveData<List<Tip>> listTips = new MutableLiveData<>();
    public LiveData<List<Tip>> getTipsWs(String token){


        Iws.getTipsApi(token).enqueue(new Callback<List<Tip>>() {
            @Override
            public void onResponse(Call<List<Tip>> call, Response<List<Tip>> response) {
                Log.d(TAG, "RESPONSE TIPS CODE OK: "+String.valueOf(response.code()));

                if(response.body() != null){
                    listTips.setValue( response.body() );
                }
            }

            @Override
            public void onFailure(Call<List<Tip>> call, Throwable t) {
                listTips.setValue(null);
            }
        });
        return listTips;
    }

    // GET TIP
        final MutableLiveData<Tip> tip = new MutableLiveData<>();
    public MutableLiveData<Tip> getTipWs(String token, String id){
        Log.d(TAG, "ENVIO ID ->: "+id);

        Iws.getTipApi(token, id).enqueue(new Callback<Tip>() {
            @Override
            public void onResponse(Call<Tip> call, Response<Tip> response) {
                Log.d(TAG, "RESPONSE TIP CODE OK: "+String.valueOf(response.code()));

                if(response.body() != null){
                    tip.setValue( response.body() );
                }
            }

            @Override
            public void onFailure(Call<Tip> call, Throwable t) {
                tip.setValue(null);
            }
        });
        return tip;
    }

    // GET STATES
        final MutableLiveData<List<State>> listStates = new MutableLiveData<>();
    public LiveData<List<State>> getStatesWs(String token){


        Iws.getStatesApi(token).enqueue(new Callback<List<State>>() {
            @Override
            public void onResponse(Call<List<State>> call, Response<List<State>> response) {
                Log.d(TAG, "RESPONSE STATES CODE OK: "+String.valueOf(response.code()));

                if(response.body() != null){
                    listStates.setValue( response.body() );
                }
            }

            @Override
            public void onFailure(Call<List<State>> call, Throwable t) {
                listStates.setValue(null);
            }
        });
        return listStates;
    }


    // GET LINKS
        final MutableLiveData<List<Link>> listLink = new MutableLiveData<>();
    public LiveData<List<Link>> getLinksWs(String token){


        Iws.getLinksApi(token).enqueue(new Callback<List<Link>>() {
            @Override
            public void onResponse(Call<List<Link>> call, Response<List<Link>> response) {
                Log.d(TAG, "RESPONSE LINKS CODE OK: "+String.valueOf(response.code()));

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
        final MutableLiveData<User> user = new MutableLiveData<>();
    public MutableLiveData<User> getUserWs(String token){


        Iws.getUserApi(token).enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                Log.d(TAG, "RESPONSE GET USER CODE OK: "+String.valueOf(response.code()));
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
                    Log.d(TAG, "RESPONSE UPDATE USER CODE OK: "+String.valueOf(response.code()));
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
