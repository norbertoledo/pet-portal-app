package com.norbertoledo.petportal.repositories;

import androidx.lifecycle.LiveData;

import com.norbertoledo.petportal.models.Place;
import com.norbertoledo.petportal.repositories.webservice.Webservice;

import java.util.List;

public class PlacesRepo {
    private static final String TAG = "PLACES REPO";

    private static PlacesRepo instance;

    private LiveData<List<Place>> listPlaces;
    private Boolean conn = true;
    private Webservice ws;

    public static PlacesRepo getInstance(){
        if( instance == null ){
            instance = new PlacesRepo();
        }
        return instance;
    }


    public LiveData<List<Place>> getPlacesRepo(final String token){

        if (conn){
            // Load data from webservice
            ws = Webservice.getInstance();
            listPlaces = ws.getPlacesWs(token);
        }else{
            // Load data from cache
            listPlaces = null;
        }

        return listPlaces;

    }
}
