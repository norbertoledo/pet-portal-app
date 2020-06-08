package com.norbertoledo.petportal.viewmodels;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.norbertoledo.petportal.models.Tip;
import com.norbertoledo.petportal.repositories.TipRepo;
import com.norbertoledo.petportal.repositories.TipsRepo;

import java.util.ArrayList;
import java.util.List;

public class TipsViewModel extends ViewModel {

    private static final String TAG = "TIPS VIEWMODEL";

    private MutableLiveData<Tip> tip;
    private LiveData<List<Tip>> tips;
    private TipRepo tipRepo;
    private TipsRepo tipsRepo;
    private String selectedTipId;
    private ArrayList<Tip> historyTips = new ArrayList<>();
    private boolean isExist = false;


    public void initTips(String token){
        if(tips != null){
            return;
        }
        Log.d(TAG,"INIT TIPS: ");
        tipsRepo = TipsRepo.getInstance();
        tips = tipsRepo.getTipsRepo( token );
    }

    public LiveData<List<Tip>> getTips(){
        Log.d(TAG,"GET TIPS: ");
        if(tips == null){
            tips = new MutableLiveData<>();
        }
        return tips;
    }


    public LiveData<Tip> getTip(String token){
        isExist = false;
        for (int i = 0; i < historyTips.size(); i++) {
            if(historyTips.get(i).getId().equals( selectedTipId ) ){
                tip.setValue( historyTips.get(i) );
                Log.d(TAG, "GET EXIST TIP: ");
                isExist=true;
            }
        }

        if(!isExist) {
            Log.d(TAG, "GET NEW TIP: ");
            tipRepo = TipRepo.getInstance();
            tip = tipRepo.getTipRepo(token, selectedTipId);
        }
        return tip;
    }
    public LiveData<Tip> getResponseTip(){

        isExist=false;
        for (int i = 0; i < historyTips.size(); i++) {
            if(historyTips.get(i).getId().equals( tip.getValue().getId() ) ){
                historyTips.set(i, tip.getValue());
                isExist = true;
            }
        }
        if(!isExist){
            historyTips.add(tip.getValue());
        }
        return tip;
    }


    public String getSelectedTip() {
        return selectedTipId;
    }

    public void setSelectedTip(String tipId) {
        selectedTipId = tipId;
    }
}
