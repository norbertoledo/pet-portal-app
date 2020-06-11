package com.norbertoledo.petportal.ui.app;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.norbertoledo.petportal.R;
import com.norbertoledo.petportal.models.User;
import com.norbertoledo.petportal.viewmodels.UserViewModel;

public class HomeFragment extends Fragment {

    private View view;
    private NavController navController;
    private LinearLayout buttonServices, buttonPlaces, buttonTips, buttonLinks;
    private TextView homeTitle;
    private UserViewModel userViewModel;

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

        homeTitle = view.findViewById(R.id.homeTitle);
        buttonServices = view.findViewById(R.id.buttonServices);
        buttonPlaces = view.findViewById(R.id.buttonPlaces);
        buttonTips = view.findViewById(R.id.buttonTips);
        buttonLinks = view.findViewById(R.id.buttonLinks);

        userViewModel = new ViewModelProvider(getActivity()).get(UserViewModel.class);

        userViewModel.getUserData().observe(getViewLifecycleOwner(), new Observer<User>() {
            @Override
            public void onChanged(User user) {
                if(user!=null){
                    homeTitle.setText( getString(R.string.home_text_title, user.getName() ) );
                }
            }
        });


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
