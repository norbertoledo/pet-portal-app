package com.norbertoledo.petportal.repositories;

import androidx.lifecycle.LiveData;

import com.norbertoledo.petportal.models.ServicesCategory;
import com.norbertoledo.petportal.repositories.webservice.Webservice;

import java.util.List;

public class ServicesCategoryRepo {

    private static final String TAG = "SERVICES CAT REPO";

    private static ServicesCategoryRepo instance;

    private LiveData<List<ServicesCategory>> categories;
    private Boolean conn = true;
    private Webservice ws;

    public static ServicesCategoryRepo getInstance(){
        if( instance == null ){
            instance = new ServicesCategoryRepo();
        }
        return instance;
    }


    public LiveData<List<ServicesCategory>> getServicesCategoryRepo(final String token){

        if (conn){
            // Load data from webservice
            ws = Webservice.getInstance();
            categories = ws.getServicesCategoryWs(token);
        }else{
            // Load data from cache
            categories = null;
        }

        return categories;

    }
}
