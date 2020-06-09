package com.norbertoledo.petportal.ui.app;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.norbertoledo.petportal.R;
import com.norbertoledo.petportal.models.ServicesCategory;
import com.norbertoledo.petportal.utils.Loader;
import com.norbertoledo.petportal.utils.ServicesCategoryAdapter;
import com.norbertoledo.petportal.viewmodels.ServicesCategoryViewModel;
import com.norbertoledo.petportal.viewmodels.ServicesViewModel;
import com.norbertoledo.petportal.viewmodels.UserViewModel;

import java.util.List;

public class ServicesCategoryFragment extends Fragment implements ServicesCategoryAdapter.OnItemClickListener {

    private View view;
    private NavController navController;
    private RecyclerView servicesCategoryRecyclerView;
    private RecyclerView.LayoutManager manager;
    private UserViewModel userViewModel;
    private ServicesCategoryViewModel servicesCategoryViewModel;
    private ServicesViewModel servicesViewModel;
    private List<ServicesCategory> categoriesList;
    private ServicesCategoryAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_services_category, container, false);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        navController = Navigation.findNavController(getActivity(), R.id.nav_host_fragment);

        servicesCategoryRecyclerView = view.findViewById(R.id.servicesCategoryRecyclerView);

        int gridColumnCount = getResources().getInteger(R.integer.services_category_grid_column_count);

        // Grid
        manager = new GridLayoutManager(getActivity(), gridColumnCount);

        servicesCategoryRecyclerView.setLayoutManager(manager);

        userViewModel = new ViewModelProvider(getActivity()).get(UserViewModel.class);
        servicesCategoryViewModel = new ViewModelProvider(getActivity()).get(ServicesCategoryViewModel.class);
        servicesViewModel = new ViewModelProvider(getActivity()).get(ServicesViewModel.class);
        servicesCategoryViewModel.initServicesCategory(userViewModel.getUserToken().getValue());

        Loader.show(getActivity(), R.id.servicesCategoryFragment, R.string.loader_message_load);



        servicesCategoryViewModel.getServicesCategory().observe(getViewLifecycleOwner(), new Observer<List<ServicesCategory>>() {
            @Override
            public void onChanged(List<ServicesCategory> servicesCategories) {
                if(servicesCategories!=null){
                    Loader.hide();
                    setView();
                }
            }
        });


    }

    private void setView(){
        categoriesList = servicesCategoryViewModel.getServicesCategory().getValue();

        adapter = new ServicesCategoryAdapter(getActivity(), R.layout.item_list_services_category, categoriesList, this);

        servicesCategoryRecyclerView.setAdapter(adapter);
    }

    @Override
    public void onItemClick(int position) {
        servicesViewModel.setSelectedCategoryName(categoriesList.get(position).getName());
        servicesViewModel.setSelectedCategoryColor(categoriesList.get(position).getColor());
        navController.navigate(R.id.action_servicesCategoryFragment_to_servicesFragment);
    }
}
