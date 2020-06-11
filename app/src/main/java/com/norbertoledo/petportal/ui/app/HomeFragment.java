package com.norbertoledo.petportal.ui.app;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.norbertoledo.petportal.R;

public class HomeFragment extends Fragment {

    private View view;
    private NavController navController;
    private LinearLayout buttonServices, buttonPlaces, buttonTips, buttonLinks;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_home, container, false);

        return view;

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        navController = Navigation.findNavController(getActivity(), R.id.nav_host_fragment);

        buttonServices = view.findViewById(R.id.buttonServices);
        buttonPlaces = view.findViewById(R.id.buttonPlaces);
        buttonTips = view.findViewById(R.id.buttonTips);
        buttonLinks = view.findViewById(R.id.buttonLinks);


        buttonServices.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navController.navigate(R.id.action_global_servicesCategoryFragment);
            }
        });

        buttonPlaces.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navController.navigate(R.id.action_global_placesFragment);
            }
        });

        buttonTips.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navController.navigate(R.id.action_global_tipsFragment);
            }
        });

        buttonLinks.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navController.navigate(R.id.action_global_linksFragment);
            }
        });


    }
}
