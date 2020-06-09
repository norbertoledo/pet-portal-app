package com.norbertoledo.petportal.repositories;

import androidx.lifecycle.LiveData;

import com.norbertoledo.petportal.models.Service;
import com.norbertoledo.petportal.repositories.webservice.Webservice;

import java.util.List;

public class ServicesRepo {
    private static final String TAG = "SERVICES REPO";

    private static ServicesRepo instance;

    private LiveData<List<Service>> services;
    private Boolean conn = true;
    private Webservice ws;

    public static ServicesRepo getInstance(){
        if( instance == null ){
            instance = new ServicesRepo();
        }
        return instance;
    }


    public LiveData<List<Service>> getServicesRepo(final String token, String state, String category){

        if (conn){
            // Load data from webservice
            ws = Webservice.getInstance();
            services = ws.getServicesWs(token, state, category);
        }else{
            // Load data from cache
            services = null;
        }

        return services;

    }
}
