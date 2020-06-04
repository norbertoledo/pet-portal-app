package com.norbertoledo.petportal.viewmodels;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.norbertoledo.petportal.models.State;
import com.norbertoledo.petportal.repositories.StatesRepo;

import java.util.List;

public class StatesViewModel extends ViewModel {

    private static final String TAG = "STATES VIEWMODEL";

    private LiveData<List<State>> states;
    private StatesRepo statesRepo;


    public void init(){

        if(states != null){
            return;
        }
        Log.d(TAG,"INIT STATES: ");
        statesRepo = StatesRepo.getInstance();
        states = statesRepo.getStatesRepo( "onlygetstateswithoutauth" );
    }

    public LiveData<List<State>> getStates(){
        Log.d(TAG,"GET STATES: ");
        if(states == null){
            states = new MutableLiveData<>();
        }
        return states;
    }
}
