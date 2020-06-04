package com.norbertoledo.petportal.repositories;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.norbertoledo.petportal.models.Tip;
import com.norbertoledo.petportal.repositories.webservice.Webservice;

import java.util.List;

public class TipRepo {

    private static final String TAG = "TIP REPO";

    private static TipRepo instance;

    private MutableLiveData<Tip> tip;
    private Boolean conn = true;
    private Webservice ws;

    public static TipRepo getInstance(){
        if( instance == null ){
            instance = new TipRepo();
        }
        return instance;
    }


    public MutableLiveData<Tip> getTipRepo(final String token, String id){

        if (conn){
            // Load data from webservice
            ws = Webservice.getInstance();
            tip = ws.getTipWs(token, id);
        }else{
            // Load data from cache
            tip = null;
        }

        return tip;

    }
}
