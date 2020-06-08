package com.norbertoledo.petportal.ui.auth;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;
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
    private User user;
    private AuthViewModel authViewModel;
    private UserViewModel userViewModel;
    private NavController navController;
    private TextView authText;
    private ImageView authGif;
    private View view;


    public AuthFragment(){}

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_auth, container, false);

        authText = view.findViewById(R.id.authText);
        authGif = view.findViewById(R.id.authGif);
        Log.d(TAG, "****************** CARGO AUTH FRAGMENT ************** ");

        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();

        navController = Navigation.findNavController(getActivity(), R.id.nav_host_fragment);

        authViewModel = new ViewModelProvider(getActivity()).get(AuthViewModel.class);
        authViewModel.getAuthMessage().observe(getActivity(), new Observer<Integer>() {
            @Override
            public void onChanged(Integer message) {
                authText.setText(message);
            }

        });

        userViewModel = new ViewModelProvider(getActivity()).get(UserViewModel.class);

        Glide.with(this).load(R.drawable.loading_dog).into(authGif);

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        updateUI();
    }


    private void updateUI() {

        authViewModel.setAuthMessage( R.string.auth_text_validate );
        Log.d(TAG, "currentUser-> "+currentUser);
        if(currentUser != null) {
            getIdToken();
        }else{
            loadSignIn();
        }

    }

    private void getIdToken(){

        currentUser.getIdToken(true).addOnCompleteListener(new OnCompleteListener<GetTokenResult>() {
            public void onComplete(@NonNull Task<GetTokenResult> task) {
                if (task.isSuccessful()) {
                    authViewModel.setAuthMessage( R.string.auth_text_load );
                    String userToken = task.getResult().getToken();
                    Log.d(TAG, "****************** TOKEN "+userToken);
                    userViewModel.setUserToken(userToken);
                    loadUser();
                } else {
                    // Handle error -> task.getException();
                    authViewModel.setAuthMessage( R.string.auth_text_error );
                    Log.d(TAG, "Error verificar usuario-> "+task.getException());
                    loadSignIn();
                }
            }
        });
    }

    private void loadUser(){

        userViewModel.getUserData().observe(this, new Observer<User>() {

            @Override
            public void onChanged(User user) {
                Log.d(TAG, "****************** GET USER CHANGED ************** ");
                Log.d(TAG, "****************** USER "+user);
                if(user!=null){
                    Snackbar.make(view, "Hola "+ user.getName()+"!", Snackbar.LENGTH_LONG).show();
                    loadApp();
                }
            }
        });

    }


    private void loadApp(){
        navController.navigate(R.id.action_authFragment_to_homeFragment);
    }

    private void loadSignIn(){
        navController.navigate(R.id.action_authFragment_to_signInFragment);
    }

}
