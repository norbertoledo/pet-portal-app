package com.norbertoledo.petportal.viewmodels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.norbertoledo.petportal.models.Link;
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
        linksRepo = LinksRepo.getInstance();
        mLinks = linksRepo.getLinksRepo( token );
    }

    public LiveData<List<Link>> getLinksVM(){
        return mLinks;
    }


}
