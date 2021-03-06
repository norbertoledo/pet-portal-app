package com.norbertoledo.petportal.viewmodels;


import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.norbertoledo.petportal.models.Service;
import com.norbertoledo.petportal.repositories.ServicesRepo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ServicesViewModel extends ViewModel {

    private static final String TAG = "SERVICES VIEWMODEL";

    private LiveData<List<Service>> services;
    private ServicesRepo servicesRepo;
    private MutableLiveData<Service> selectedService;
    private String selectedState;
    private String selectedCategoryName;
    private String selectedCategoryColor;
    private boolean isExist = false;
    private ArrayList<Map<String, String>> historyStateCategory = new ArrayList<Map<String, String>>();
    private ArrayList<LiveData<List<Service>>> historyResults = new ArrayList<>();


    public void initServices(String token){

        isExist = false;

        for (int i = 0; i < historyStateCategory.size(); i++) {
            if(
                    historyStateCategory.get(i).get("state").equals( selectedState )
                    && historyStateCategory.get(i).get("category").equals( selectedCategoryName )
            ){
                services = historyResults.get(i);
                isExist=true;
            }
        }

        if(!isExist) {
            servicesRepo = ServicesRepo.getInstance();
            services = servicesRepo.getServicesRepo( token, selectedState, selectedCategoryName );
        }

    }

    public LiveData<List<Service>> getServices(){

        if(services == null){
            services = new MutableLiveData<List<Service>>();
            return services;
        }

        isExist=false;
        Map<String, String> newMap = new HashMap<>();
        for (int i = 0; i < historyStateCategory.size(); i++) {

            if(
                    historyStateCategory.get(i).get("state").equals( selectedState )
                    && historyStateCategory.get(i).get("category").equals( selectedCategoryName )
            ){
                newMap.put("state", selectedState);
                newMap.put("category", selectedCategoryName);
                historyStateCategory.set(i, newMap);
                historyResults.set(i, services);
                isExist = true;
            }

        }

        if(!isExist){
            newMap.put("state", selectedState);
            newMap.put("category", selectedCategoryName);
            historyResults.add(services);
            historyStateCategory.add(newMap);
        }

        return services;

    }

    public MutableLiveData<Service> getSelectedService() {
        return selectedService;
    }

    public void setSelectedService(Service service) {
        if(selectedService==null){
            selectedService = new MutableLiveData<>();
        }
        selectedService.setValue( service );
    }

    public String getSelectedCategoryName() {
        return selectedCategoryName;
    }

    public void setSelectedCategoryName(String selectedCategoryName) {
        this.selectedCategoryName = selectedCategoryName;
    }

    public String getSelectedCategoryColor() {
        return selectedCategoryColor;
    }

    public void setSelectedCategoryColor(String selectedCategoryColor) {
        this.selectedCategoryColor = selectedCategoryColor;
    }

    public String getSelectedState() {
        return selectedState;
    }

    public void setSelectedState(String selectedState) {
        this.selectedState = selectedState;
    }
}
