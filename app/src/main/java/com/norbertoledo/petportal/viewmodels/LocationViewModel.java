package com.norbertoledo.petportal.viewmodels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class LocationViewModel extends ViewModel {

    private static final String TAG = "LOCATION VIEWMODEL";

    private MutableLiveData<String> location;


    public void setLocation(String newLocation){
        if(location==null){
            location = new MutableLiveData<>();
        }
        location.setValue(newLocation);
    }

    public LiveData<String> getLocation(){
        if(location==null){
            location = new MutableLiveData<>();
        }
        return location;
    }

}
