package com.norbertoledo.petportal.ui.auth;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GetTokenResult;
import com.norbertoledo.petportal.R;

import com.norbertoledo.petportal.models.User;
import com.norbertoledo.petportal.viewmodels.AuthViewModel;
import com.norbertoledo.petportal.viewmodels.UserViewModel;

public class AuthFragment extends Fragment {

    private static final String TAG = "AUTH";

    private FirebaseAuth mAuth;
    private FirebaseUser currentUser;
    private AuthViewModel authViewModel;
    private UserViewModel userViewModel;
    private NavController navController;
    private TextView authText;
    private ImageView authGif;
    private View view;
    private String userToken;


    public AuthFragment(){}

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_auth, container, false);

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        authText = view.findViewById(R.id.authText);
        authGif = view.findViewById(R.id.authGif);

        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();

        navController = Navigation.findNavController(getActivity(), R.id.nav_host_fragment);

        authViewModel = new ViewModelProvider(getActivity()).get(AuthViewModel.class);
        userViewModel = new ViewModelProvider(getActivity()).get(UserViewModel.class);

        authViewModel.getAuthMessage().observe(getActivity(), new Observer<Integer>() {
            @Override
            public void onChanged(Integer message) {
                authText.setText(message);
            }
        });

        Glide.with(this).load(R.drawable.loading_dog).into(authGif);

        updateUI();
    }


    private void updateUI() {

        authViewModel.setAuthMessage( R.string.auth_text_validate );

        if(currentUser != null) {
            getIdToken();
        }else{
            gotoSignIn();
        }

    }

    private void getIdToken(){

        currentUser.getIdToken(true).addOnCompleteListener(new OnCompleteListener<GetTokenResult>() {
            public void onComplete(@NonNull Task<GetTokenResult> task) {
                if (task.isSuccessful()) {
                    authViewModel.setAuthMessage( R.string.auth_text_load );
                    userToken = task.getResult().getToken();
                    userViewModel.setUserToken(userToken);
                    loadUser();
                } else {
                    authViewModel.setAuthMessage( R.string.auth_text_error );
                    gotoSignIn();
                }
            }
        });
    }

    private void loadUser(){

        userViewModel.getUserData().observe(getViewLifecycleOwner(), new Observer<User>() {
            @Override
            public void onChanged(User user) {
                if(user!=null){
                    gotoApp();
                }
            }
        });

    }


    private void gotoApp(){
        navController.navigate(R.id.action_authFragment_to_homeFragment);
    }

    private void gotoSignIn(){
        navController.navigate(R.id.action_authFragment_to_signInFragment);
    }

}
