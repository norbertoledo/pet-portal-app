package com.norbertoledo.petportal.ui.app;

import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.norbertoledo.petportal.MainActivity;
import com.norbertoledo.petportal.R;
import com.norbertoledo.petportal.models.Service;
import com.norbertoledo.petportal.models.State;
import com.norbertoledo.petportal.utils.Loader;
import com.norbertoledo.petportal.utils.LocationDialog;
import com.norbertoledo.petportal.utils.ServicesAdapter;
import com.norbertoledo.petportal.viewmodels.LocationViewModel;
import com.norbertoledo.petportal.viewmodels.ServicesViewModel;
import com.norbertoledo.petportal.viewmodels.UserViewModel;

import java.util.List;

public class ServicesFragment extends Fragment implements ServicesAdapter.OnItemClickListener{

    private UserViewModel userViewModel;
    private ServicesViewModel servicesViewModel;
    private LocationViewModel locationViewModel;
    private NavController navController;
    private RecyclerView servicesRecyclerView;
    private List<Service>services;
    private RecyclerView.LayoutManager manager;
    private ServicesAdapter adapter;
    private View servicesView;
    private TextView servicesErrorText;
    private TextView servicesTextViewLocation;
    private Button servicesButtonLocation;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_services, container, false);

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        navController = Navigation.findNavController(getActivity(), R.id.nav_host_fragment);

        userViewModel = new ViewModelProvider(getActivity()).get(UserViewModel.class);
        servicesViewModel = new ViewModelProvider(getActivity()).get(ServicesViewModel.class);
        locationViewModel = new ViewModelProvider(getActivity()).get(LocationViewModel.class);


        servicesView = view.findViewById(R.id.servicesView);
        servicesRecyclerView = view.findViewById(R.id.servicesRecyclerView);
        servicesErrorText = view.findViewById(R.id.servicesErrorText);
        servicesTextViewLocation = view.findViewById(R.id.servicesTextViewLocation);
        servicesButtonLocation = view.findViewById(R.id.servicesButtonLocation);

        int gridColumnCount = getResources().getInteger(R.integer.services_grid_column_count);

        // Grid
        manager = new GridLayoutManager(getActivity(), gridColumnCount);

        servicesRecyclerView.setLayoutManager(manager);

        servicesButtonLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LocationDialog.show(getActivity(), getActivity());
            }
        });


        Loader.show(getActivity(), R.id.servicesFragment, R.string.loader_message_load);

        listenLocation();

    }

    public void listenLocation(){
        locationViewModel.getLocation().observe(getViewLifecycleOwner(), new Observer<State>() {
            @Override
            public void onChanged(State location) {
                if(location!=null){
                    //Toast.makeText(getContext(), "SERVICES-> "+location.getName(), Toast.LENGTH_SHORT).show();
                    servicesViewModel.setSelectedState(location.getName());
                    servicesViewModel.initServices(userViewModel.getUserToken().getValue(), location.getName(), servicesViewModel.getSelectedCategoryName());
                    listenServices();
                }
            }
        });
    }

    public void listenServices(){
        servicesViewModel.getServices().observe(getViewLifecycleOwner(), new Observer<List<Service>>() {
            @Override
            public void onChanged(List<Service> services) {
                if(services!=null) {
                    Loader.hide();
                    setView();
                }
            }
        });
    }

    public void setView(){

        int bgColor = Color.parseColor(servicesViewModel.getSelectedCategoryColor());
        servicesView.setBackgroundColor(bgColor);

        servicesTextViewLocation.setText(getString(R.string.services_text_search_in)+ " " + servicesViewModel.getSelectedState());

        services = servicesViewModel.getServices().getValue();


        if(services.size()==0){
            servicesErrorText.setVisibility(View.VISIBLE);
/*
            servicesErrorText.setText(Html.fromHtml(
                    "<b>...Oops!</b><br><br>"+
                            "No disponemos de "+servicesViewModel.getSelectedCategoryName()+" en "+servicesViewModel.getSelectedState()+".  "+
                            "Intenta otra combinación.<br><br>"+
                            "<b>Si ofreces este servicio es tu oportunidad para ser el único en la zona!</b><br>"+
                            "<i>Contáctanos a servicios@petportal.com</i>"
            ));
*/
            servicesErrorText.setText(Html.fromHtml(
                    getString(R.string.services_no_items_to_display, servicesViewModel.getSelectedCategoryName(), servicesViewModel.getSelectedState())
            ));





        }else{
            servicesErrorText.setVisibility(View.GONE);
            servicesErrorText.setText("");
        }
        adapter = new ServicesAdapter(getActivity(), R.layout.item_list_services, services, this);
        servicesRecyclerView.setAdapter(adapter);

    }

    @Override
    public void onItemClick(int position) {
        servicesViewModel.setSelectedService(services.get(position));
        navController.navigate(R.id.action_servicesFragment_to_serviceFragment);
    }
}
