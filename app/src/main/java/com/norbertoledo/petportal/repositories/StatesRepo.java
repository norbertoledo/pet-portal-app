package com.norbertoledo.petportal.repositories;

import androidx.lifecycle.LiveData;


import com.norbertoledo.petportal.models.State;
import com.norbertoledo.petportal.repositories.webservice.IWebservice;
import com.norbertoledo.petportal.repositories.webservice.Webservice;

import java.util.List;

public class StatesRepo {

    private static final String TAG = "STATES REPO";

    private static StatesRepo instance;

    private IWebservice Iws;
    private LiveData<List<State>> listStates;
    private Boolean conn = true;
    private Webservice ws;

    public static StatesRepo getInstance(){
        if( instance == null ){
            instance = new StatesRepo();
        }
        return instance;
    }


    public LiveData<List<State>> getStatesRepo(final String token){


        if (conn){
            // Load data from webservice
            ws = Webservice.getInstance();
            listStates = ws.getStatesWs(token);
        }else{
            // Load data from cache
            listStates = null;
        }

        return listStates;


    }
}
