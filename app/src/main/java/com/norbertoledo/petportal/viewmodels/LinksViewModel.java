package com.norbertoledo.petportal.viewmodels;

import android.util.Log;
import android.widget.Toast;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.norbertoledo.petportal.models.Link;
import com.norbertoledo.petportal.models.User;
import com.norbertoledo.petportal.repositories.LinksRepo;

import java.util.List;

public class LinksViewModel extends ViewModel {

    private static final String TAG = "LINKS VIEWMODEL";

    private LiveData<List<Link>> mLinks;
    private LinksRepo linksRepo;


    public void init(String token){

        if(mLinks != null){
            return;
        }
        Log.d(TAG,"INIT LINKS: ");
        linksRepo = LinksRepo.getInstance();
        mLinks = linksRepo.getLinksRepo( token );
    }

    public LiveData<List<Link>> getLinksVM(){
        // Buscar listado en el repositorio
        Log.d(TAG,"GET LINKS: ");
        return mLinks;
    }


}
