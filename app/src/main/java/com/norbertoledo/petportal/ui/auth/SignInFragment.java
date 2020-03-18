package com.norbertoledo.petportal.ui.auth;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GetTokenResult;
import com.norbertoledo.petportal.R;
import com.norbertoledo.petportal.activities.MainActivity;

import com.norbertoledo.petportal.models.User;

public class SignInFragment extends Fragment {

    private static final String TAG = "SIGN IN";

    private FirebaseAuth mAuth;
    private User userStore;



    private EditText signinEmail, signinPassword;
    private Button signinBtnLogin, signinBtnRegister;
    private NavController navController;

    public SignInFragment(){}

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_signin, container, false);

        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();

        signinEmail = view.findViewById(R.id.signinEmail);
        signinPassword = view.findViewById(R.id.signinPassword);
        signinBtnLogin = view.findViewById(R.id.signinBtnLogin);
        signinBtnRegister = view.findViewById(R.id.signinBtnRegister);

        navController = Navigation.findNavController(getActivity(), R.id.nav_host_auth);

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        signinBtnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                formValidate();
            }
        });

        signinBtnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navController.navigate(R.id.action_fragment_signin_to_fragment_signup);
            }
        });

    }


    //Metodo que llamamos al hacer click en el boton de login
    private void formValidate() {
        if (!signinEmail.getText().toString().isEmpty() || !signinPassword.getText().toString().isEmpty()){
            signIn();
        } else {
            Toast.makeText(getContext(), R.string.form_validate_error, Toast.LENGTH_SHORT).show();
        }
    }

    private void signIn(){

        String email = signinEmail.getText().toString();
        String password = signinPassword.getText().toString();

        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Log.d(TAG, "signInWithEmail:success");
                            updateUI(mAuth.getCurrentUser());
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithEmail:failure", task.getException());
                            updateUI(null);
                        }

                    }
                });
    }

    private void updateUI(FirebaseUser currentUser) {
        if(currentUser != null) {
            Toast.makeText(getContext(), R.string.signin_message_success, Toast.LENGTH_SHORT).show();
            getIdToken(currentUser);
        }else{
            Toast.makeText(getContext(), R.string.signin_message_error, Toast.LENGTH_SHORT).show();
        }
    }

    private void getIdToken(FirebaseUser currentUser){
        currentUser.getIdToken(true).addOnCompleteListener(new OnCompleteListener<GetTokenResult>() {
            public void onComplete(@NonNull Task<GetTokenResult> task) {
                if (task.isSuccessful()) {
                    String userToken = task.getResult().getToken();

                    Toast.makeText(getContext(), R.string.user_message_token_success, Toast.LENGTH_SHORT).show();
                    userStore = User.getInstance();
                    userStore.setToken(userToken);
                    loadApp();

                } else {
                    Toast.makeText(getContext(), R.string.user_message_token_error, Toast.LENGTH_SHORT).show();
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
}
