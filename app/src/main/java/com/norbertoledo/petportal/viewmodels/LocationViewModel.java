package com.norbertoledo.petportal.viewmodels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.norbertoledo.petportal.models.State;

public class LocationViewModel extends ViewModel {

    private static final String TAG = "LOCATION VIEWMODEL";


    private MutableLiveData<State> location;


    public void setLocation(State newLocation){
        if(location==null){
            location = new MutableLiveData<>();
        }
        location.setValue(newLocation);
    }

    public LiveData<State> getLocation(){
        if(location==null){
            location = new MutableLiveData<>();
        }
        return location;
    }

    public void resetLocation(){
        location = null;
    }


}
