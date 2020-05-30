package com.norbertoledo.petportal.viewmodels;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class AuthViewModel extends ViewModel {
    private static final String TAG = "AUTH VIEWMODEL";

    private MutableLiveData<Integer> authMessage;
    public MutableLiveData<Integer> getAuthMessage(){
        if(authMessage==null){
            authMessage = new MutableLiveData<>();
        }
        return authMessage;
    }

    public void setAuthMessage(int message){
        authMessage.setValue(message);
    }

}
