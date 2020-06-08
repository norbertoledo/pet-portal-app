package com.norbertoledo.petportal.viewmodels;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.norbertoledo.petportal.models.ServicesCategory;
import com.norbertoledo.petportal.repositories.ServicesCategoryRepo;

import java.util.List;

public class ServicesCategoryViewModel extends ViewModel {

    private static final String TAG = "SERVICES CAT VIEWMODEL";

    private MutableLiveData<ServicesCategory> category;
    private LiveData<List<ServicesCategory>> categories;
    private ServicesCategoryRepo servicesCategoryRepo;
    private String selectedCategoryId;

    public void initServicesCategory(String token){
        if(categories != null){
            return;
        }
        Log.d(TAG,"INIT SERVICES CATEGORIES");
        servicesCategoryRepo = ServicesCategoryRepo.getInstance();
        categories = servicesCategoryRepo.getServicesCategoryRepo( token );
    }

    public LiveData<List<ServicesCategory>> getServicesCategory(){
        Log.d(TAG,"GET SERVICES CATEGORIES");
        if(categories == null){
            categories = new MutableLiveData<>();
        }
        return categories;
    }

    public String getSelectedCategory() {
        return selectedCategoryId;
    }

    public void setSelectedCategory(String categoryId) {
        selectedCategoryId = categoryId;
    }

}
