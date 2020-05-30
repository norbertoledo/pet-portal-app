package com.norbertoledo.petportal.repositories;

import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import com.norbertoledo.petportal.models.Link;
import com.norbertoledo.petportal.repositories.webservice.IWebservice;
import com.norbertoledo.petportal.repositories.webservice.Webservice;

import java.util.List;


public class LinksRepo {

    private static final String TAG = "LINKS REPO";

    private static LinksRepo instance;

    private IWebservice Iws;
    private LiveData<List<Link>> listLink;
    private Boolean conn = true;
    private Webservice ws;

    public static LinksRepo getInstance(){
        if( instance == null ){
            instance = new LinksRepo();
        }
        return instance;
    }



    public LiveData<List<Link>> getLinksRepo(final String token){


        if (conn){
            // Load data from webservice
            ws = Webservice.getInstance();
            listLink = ws.getLinksWs(token);
        }else{
            // Load data from cache
            listLink = null;
        }

        return listLink;


    }

}
