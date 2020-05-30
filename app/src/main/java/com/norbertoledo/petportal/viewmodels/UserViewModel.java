package com.norbertoledo.petportal.viewmodels;

import android.app.Activity;
import android.app.Application;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.bumptech.glide.signature.ObjectKey;
import com.norbertoledo.petportal.MainActivity;
import com.norbertoledo.petportal.models.User;
import com.norbertoledo.petportal.repositories.UserRepo;
import com.norbertoledo.petportal.repositories.webservice.IWebservice;
import com.norbertoledo.petportal.repositories.webservice.Webservice;
import com.norbertoledo.petportal.repositories.webservice.WebserviceBuilder;

import java.util.concurrent.ExecutionException;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UserViewModel extends ViewModel {
    private static final String TAG = "USER VIEWMODEL";

    private MutableLiveData<String> userToken;
    private MutableLiveData<User> userData;
    private MutableLiveData<String> message;
    private MutableLiveData<User> updateResponse;
    private MutableLiveData<ObjectKey> imageProfileSignature;

    private UserRepo userRepo;




    public LiveData<String> getUserToken(){
        if(userToken==null){
            userToken = new MutableLiveData<String>();
        }
        return userToken;
    }

    public void setUserToken(String token) {
        if(userToken==null){
            userToken = new MutableLiveData<String>();
        }
        userToken.setValue(token);
    }

    public void resetUserData(){
        userData = null;
        message = null;
        //clearMessage();
    }

    public void setUserData(@Nullable User user){
        Log.d(TAG,"SET USER DATA: "+user);
        userData.setValue(user);
    }


    public LiveData<User> getUserData(){

        if(userData==null){
            Log.d(TAG,"GET USER DATA (SOY NULL Y BUSCO NUEVA INSTANCIA: "+userData);
            userRepo = UserRepo.getInstance();
            userData = userRepo.getUserRepo(getUserToken().getValue());
        }

        Log.d(TAG,"GET USER DATA: "+userData);
        return userData;
    }


    public LiveData<User> newUser(){
        if(userData==null){
            userRepo = UserRepo.getInstance();
            userData = userRepo.newUserRepo();
        }
        return userData;
    }


    public void updateUserData(final User user){

        userRepo = UserRepo.getInstance();
        updateResponse = userRepo.updateUserRepo( getUserToken().getValue(), user );

    }

    public LiveData<User> updateUserDataResponse(){
        if(updateResponse==null){
            updateResponse = new MutableLiveData<User>();
        }
        Log.d(TAG,"LLAMO A USER DATA RESPONSE: "+updateResponse.getValue());
        return updateResponse;

    }

    public void clearUpdateResponse(){
        updateResponse.setValue(null);
    }



    public MutableLiveData<String> getMessages(){
        if(message==null){
            message = new MutableLiveData<String>();
        }
        Log.d(TAG,"LLAMO A GET MESSAGE: "+message.getValue());
        return message;
    }
    public void setMessage(String msg){
        message.setValue(msg);
    }

    public void clearMessage() {
        message.setValue(null);
    }


    public void setImageProfileSignature(ObjectKey signature){
        if(imageProfileSignature==null){
            imageProfileSignature = new MutableLiveData<>();
        }
        imageProfileSignature.setValue(signature);
    }

    public ObjectKey getImageProfileSignature(){
        if(imageProfileSignature==null){
            imageProfileSignature = new MutableLiveData<>();
        }
        return imageProfileSignature.getValue();
    }


}





