package com.norbertoledo.petportal.utils;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.norbertoledo.petportal.R;
import com.norbertoledo.petportal.models.State;
import com.norbertoledo.petportal.viewmodels.LocationViewModel;
import com.norbertoledo.petportal.viewmodels.StatesViewModel;

import java.util.ArrayList;
import java.util.List;


public class LocationDialogFragment extends Fragment {

    private TextView title;
    private Button btnOk, btnDismiss;
    private ListView listView;
    private LocationViewModel locationViewModel;
    private StatesViewModel statesViewModel;
    private List<String> arrStates;
    private ArrayAdapter<String> adapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_location_dialog, container, false);
/*
        title = view.findViewById(R.id.locationTitle);
        listView = view.findViewById(R.id.locationListView);
        btnOk = view.findViewById(R.id.locationOk);
        btnDismiss = view.findViewById(R.id.locationDismiss);

        arrStates = new ArrayList<>();


        locationViewModel = new ViewModelProvider(getActivity()).get(LocationViewModel.class);

        // States ViewModel
        statesViewModel = new ViewModelProvider(getActivity()).get(StatesViewModel.class);
        statesViewModel.init();
        statesViewModel.getStates().observe(getActivity(), new Observer<List<State>>() {
            @Override
            public void onChanged(List<State> states) {
                if(states!= null){

                    int size = states.size();



                    for( int i=0; i<size; i++ ){
                        arrStates.add(states.get(i).getName());
                    }

                    adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, arrStates);

                    listView.setAdapter(adapter);
                }
            }
        });


        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                locationViewModel.setLocation(arrStates.get(position));
                LocationDialog.hide();
            }
        });
*/
        return view;
    }



}
