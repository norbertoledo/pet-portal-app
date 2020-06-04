package com.norbertoledo.petportal.repositories;

import androidx.lifecycle.LiveData;

import com.norbertoledo.petportal.models.Tip;
import com.norbertoledo.petportal.repositories.webservice.Webservice;

import java.util.List;

public class TipsRepo {

    private static final String TAG = "TIPS REPO";

    private static TipsRepo instance;

    private LiveData<List<Tip>> listTips;
    private Boolean conn = true;
    private Webservice ws;

    public static TipsRepo getInstance(){
        if( instance == null ){
            instance = new TipsRepo();
        }
        return instance;
    }


    public LiveData<List<Tip>> getTipsRepo(final String token){

        if (conn){
            // Load data from webservice
            ws = Webservice.getInstance();
            listTips = ws.getTipsWs(token);
        }else{
            // Load data from cache
            listTips = null;
        }

        return listTips;

    }
}
