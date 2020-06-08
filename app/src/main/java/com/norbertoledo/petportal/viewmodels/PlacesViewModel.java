package com.norbertoledo.petportal.viewmodels;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.norbertoledo.petportal.models.Place;
import com.norbertoledo.petportal.repositories.PlacesRepo;

import java.util.List;

public class PlacesViewModel extends ViewModel {

    private static final String TAG = "PLACES VIEWMODEL";

    private LiveData<List<Place>> places;
    private PlacesRepo placesRepo;


    public void init(String token){

        if(places != null){
            return;
        }
        Log.d(TAG,"INIT PLACES: ");
        placesRepo = PlacesRepo.getInstance();
        places = placesRepo.getPlacesRepo( token );
    }

    public LiveData<List<Place>> getPlaces(){
        Log.d(TAG,"GET PLACES: ");
        if(places == null){
            places = new MutableLiveData<>();
        }
        return places;
    }
}
