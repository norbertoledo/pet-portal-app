package com.norbertoledo.petportal.viewmodels;


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
    private String selectedCategoryName;
    private String selectedCategoryColor;

    public void initServicesCategory(String token){
        if(categories != null){
            return;
        }
        servicesCategoryRepo = ServicesCategoryRepo.getInstance();
        categories = servicesCategoryRepo.getServicesCategoryRepo( token );
    }

    public LiveData<List<ServicesCategory>> getServicesCategory(){
        if(categories == null){
            categories = new MutableLiveData<>();
        }
        return categories;
    }

    public String getSelectedCategoryName() {
        return selectedCategoryName;
    }

    public void setSelectedCategoryName(String categoryName) {
        selectedCategoryName = categoryName;
    }

    public String getSelectedCategoryColor() {
        return selectedCategoryColor;
    }

    public void setSelectedCategoryColor(String categoryColor) {
        selectedCategoryColor = categoryColor;
    }

}
