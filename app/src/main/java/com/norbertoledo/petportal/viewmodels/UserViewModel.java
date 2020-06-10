package com.norbertoledo.petportal.viewmodels;

import android.util.Log;

import androidx.annotation.Nullable;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.bumptech.glide.signature.ObjectKey;
import com.norbertoledo.petportal.models.User;
import com.norbertoledo.petportal.repositories.UserRepo;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;


public class UserViewModel extends ViewModel {
    private static final String TAG = "USER VIEWMODEL";

    private MutableLiveData<String> userToken;
    private MutableLiveData<User> userData;
    private MutableLiveData<User> newUserResponse;
    private MutableLiveData<User> updateResponse;
    private MutableLiveData<User> updateImageResponse;
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
    }

    public void setUserData(@Nullable User user){
        userData.setValue(user);
    }

    // New User
    public void newUser(String token, User user){
        if(userData==null){
            userRepo = UserRepo.getInstance();
            newUserResponse = userRepo.newUserRepo( token, user );
        }
    }

    public LiveData<User> newUserResponse(){
        if(newUserResponse == null){
            newUserResponse = new MutableLiveData<>();
        }
        return newUserResponse;
    }

    // Get User
    public LiveData<User> getUserData(){
        if(userData==null){
            userRepo = UserRepo.getInstance();
            userData = userRepo.getUserRepo(getUserToken().getValue());
        }
        return userData;
    }


    // Update User Data
    public void updateUserData(final User user){
        userRepo = UserRepo.getInstance();
        updateResponse = userRepo.updateUserRepo( getUserToken().getValue(), user );
    }

    public LiveData<User> updateUserDataResponse(){
        if(updateResponse==null){
            updateResponse = new MutableLiveData<User>();
        }
        return updateResponse;
    }

    public void clearUpdateResponse(){
        updateResponse.setValue(null);
    }



    // Update User Image
    public void updateUserImage(MultipartBody.Part requestImage, RequestBody imageData){
        userRepo = UserRepo.getInstance();
        updateImageResponse = userRepo.updateUserImageRepo( getUserToken().getValue(), requestImage, imageData );
    }

    public LiveData<User> updateUserImageResponse(){
        if(updateImageResponse==null){
            updateImageResponse = new MutableLiveData<User>();
        }
        return updateImageResponse;
    }

    public void clearUpdateImageResponse(){
        updateImageResponse.setValue(null);
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





