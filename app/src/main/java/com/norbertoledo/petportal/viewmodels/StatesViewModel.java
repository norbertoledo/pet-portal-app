package com.norbertoledo.petportal.viewmodels;


import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.norbertoledo.petportal.models.State;
import com.norbertoledo.petportal.repositories.StatesRepo;
import com.norbertoledo.petportal.utils.GlobalVars;

import java.util.List;

public class StatesViewModel extends ViewModel {

    private static final String TAG = "STATES VIEWMODEL";

    private LiveData<List<State>> states;
    private StatesRepo statesRepo;


    public void init(){

        if(states != null){
            return;
        }
        statesRepo = StatesRepo.getInstance();
        states = statesRepo.getStatesRepo(GlobalVars.STATES_TOKEN);
    }

    public LiveData<List<State>> getStates(){
        if(states == null){
            states = new MutableLiveData<>();
        }
        return states;
    }
}
