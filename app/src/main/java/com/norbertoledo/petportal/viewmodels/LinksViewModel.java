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
    private User user;



    public void init(){

        if(mLinks != null){
            return;
        }

        user = User.getInstance();
        linksRepo = new LinksRepo();
        mLinks = linksRepo.getLinksRepo( user.getToken() );
    }

    public LiveData<List<Link>> getLinksVM(){

        // Buscar listado en el repositorio
        return mLinks;
    }


}
