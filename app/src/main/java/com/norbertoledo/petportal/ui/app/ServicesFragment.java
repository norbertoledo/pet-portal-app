package com.norbertoledo.petportal.ui.app;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.norbertoledo.petportal.R;
import com.norbertoledo.petportal.viewmodels.LocationViewModel;

public class ServicesFragment extends Fragment {


    private LocationViewModel locationViewModel;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_services, container, false);

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        locationViewModel = new ViewModelProvider(getActivity()).get(LocationViewModel.class);

        locationViewModel.getLocation().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(String location) {
                Log.d("SERVICES FRAGMENT -> ", location);
                Toast.makeText(getContext(), "SERVICE-> "+location, Toast.LENGTH_SHORT).show();
            }
        });

    }
}
