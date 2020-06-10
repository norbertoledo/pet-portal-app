package com.norbertoledo.petportal.ui.auth;

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
import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.norbertoledo.petportal.R;

public class SignInFragment extends Fragment {

    private static final String TAG = "SIGN IN";

    private View view;
    private FirebaseAuth mAuth;
    private EditText signinEmail, signinPassword;
    private Button signinBtnLogin, signinBtnRegister;
    private NavController navController;
    private String email;
    private String password;

    public SignInFragment(){}

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_signin, container, false);

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();

        signinEmail = view.findViewById(R.id.signinEmail);
        signinPassword = view.findViewById(R.id.signinPassword);
        signinBtnLogin = view.findViewById(R.id.signinBtnLogin);
        signinBtnRegister = view.findViewById(R.id.signinBtnRegister);

        navController = Navigation.findNavController(getActivity(), R.id.nav_host_fragment);

        signinBtnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validateForm();
            }
        });

        signinBtnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gotoSignUp();
            }
        });

    }


    //Metodo que llamamos al hacer click en el boton de login
    private void validateForm() {
        if (!signinEmail.getText().toString().isEmpty() || !signinPassword.getText().toString().isEmpty()){
            signIn();
        } else {
            Snackbar.make(view, R.string.form_validate_error, Snackbar.LENGTH_LONG).show();
        }
    }

    private void signIn(){

        email = signinEmail.getText().toString();
        password = signinPassword.getText().toString();

        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Log.d(TAG, "signInWithEmail:success");
                            gotoAuth();
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithEmail:failure", task.getException());
                            Snackbar.make(view, "¡Ooops! "+ task.getException().getMessage(), Snackbar.LENGTH_LONG).show();
                        }
                    }
                });
    }

    private void gotoSignUp(){ navController.navigate(R.id.action_signInFragment_to_signUpFragment); }

    private void gotoAuth(){
        navController.navigate(R.id.action_global_authFragment);
    }

}
