package com.norbertoledo.petportal.ui.auth;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GetTokenResult;
import com.norbertoledo.petportal.R;
import com.norbertoledo.petportal.ui.app.MainActivity;

import com.norbertoledo.petportal.models.User;

public class AuthFragment extends Fragment {

    private static final String TAG = "AUTH";

    private FirebaseAuth mAuth;
    private User userStore;
    private NavController navController;

    public AuthFragment(){}

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_auth, container, false);

        userStore = User.getInstance();
        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();
        navController = Navigation.findNavController(getActivity(), R.id.nav_host_auth);

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        updateUI(mAuth.getCurrentUser());

    }


    private void updateUI(FirebaseUser currentUser) {

        Log.d(TAG, "currentUser-> "+currentUser);
        if(currentUser != null) {
            getIdToken(currentUser);
        }else{
            loadSignIn();
        }

    }

    private void getIdToken(FirebaseUser currentUser){
        //FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        currentUser.getIdToken(true).addOnCompleteListener(new OnCompleteListener<GetTokenResult>() {
            public void onComplete(@NonNull Task<GetTokenResult> task) {
                if (task.isSuccessful()) {
                    String userToken = task.getResult().getToken();

                    userStore.setToken(userToken);
                    loadApp();

                } else {
                    // Handle error -> task.getException();
                }
            }
        });
    }

    private void loadApp(){
        Intent intent = new Intent(getContext(), MainActivity.class);
        startActivity(intent);
        getActivity().finish();
    }

    private void loadSignIn(){
        navController.navigate(R.id.action_fragment_auth_to_fragment_signin);
    }

}
