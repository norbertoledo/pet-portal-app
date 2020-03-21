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
import com.google.firebase.auth.UserProfileChangeRequest;
import com.norbertoledo.petportal.R;
import com.norbertoledo.petportal.repositories.webservice.WebserviceBuilder;
import com.norbertoledo.petportal.ui.app.MainActivity;
import com.norbertoledo.petportal.repositories.webservice.IWebservice;
import com.norbertoledo.petportal.repositories.webservice.Webservice;
import com.norbertoledo.petportal.models.User;


import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SignUpFragment extends Fragment {

    private static final String TAG = "SIGN UP";

    private FirebaseAuth mAuth;
    private User userStore;
    private EditText signupEmail, signupPassword, signupConfirmPassword, signupName;
    private Button signupBtnLogin, signupBtnRegister;
    private NavController navController;


    public SignUpFragment(){}

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_signup, container, false);

        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();

        signupEmail = view.findViewById(R.id.signupEmail);
        signupPassword = view.findViewById(R.id.signupPassword);
        signupConfirmPassword = view.findViewById(R.id.signupConfirmPassword);
        signupName = view.findViewById(R.id.signupName);
        signupBtnLogin = view.findViewById(R.id.signupBtnLogin);
        signupBtnRegister = view.findViewById(R.id.signupBtnRegister);

        navController = Navigation.findNavController(getActivity(), R.id.nav_host_auth);




        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        signupBtnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navController.navigate(R.id.action_fragment_signup_to_fragment_signin);
            }
        });

        signupBtnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                formValidate();
            }
        });

    }

    //En este metodo verificamos si los campos no estan vacios
    private void formValidate() {
        if (!signupEmail.getText().toString().isEmpty() || !signupPassword.getText().toString().isEmpty() || !signupName.getText().toString().isEmpty()){
            //Si los campos no estan vacios  las contraseñas conciden mostramos el toast de registro
            if (arePasswordEquals()){
                signUp();
            } else {
                Toast.makeText(getContext(), "Las contrasenas no coinciden", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(getContext(), "Has llenado todos los campos?", Toast.LENGTH_SHORT).show();
        }
    }

    //Funcion que verifica si ambas contraseñas son iguales
    private boolean arePasswordEquals() {
        return signupPassword.getText().toString().equals(signupConfirmPassword.getText().toString());
    }

    private void signUp(){
        //Toast.makeText(getActivity().getApplicationContext(), "Bienvenido has sido registrado", Toast.LENGTH_SHORT).show();

        String email = signupEmail.getText().toString();
        String password = signupPassword.getText().toString();
//        String name = signupName.getText().toString();

        Log.d(TAG, email);
        Log.d(TAG, password);

        //progressBar.setVisibility(View.VISIBLE);

        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "createUserWithEmail:success");

                            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                            updateUserProfile(user);

                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "createUserWithEmail:failure", task.getException());
                            Toast.makeText(getContext(), task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                            updateUI(null);
                        }

                        // ...
                    }
                });


    }

    private void updateUserProfile(FirebaseUser user){

        UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                .setDisplayName(signupName.getText().toString()).build();

        user.updateProfile(profileUpdates)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Log.d(TAG, "User profile updated.");

                            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

                            userStore = User.getInstance();

                            userStore.setEmail(user.getEmail());
                            userStore.setName(user.getDisplayName());
                            userStore.setCity("Alicante City");


                            updateUI(user);
                        }
                    }
                });
    }



    private void updateUI(FirebaseUser currentUser) {
        if(currentUser != null) {
            getIdToken(currentUser);
        }else{
            Toast.makeText(getContext(), "Error de registro.", Toast.LENGTH_SHORT).show();
        }
    }

    private void getIdToken(FirebaseUser currentUser){
        //FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        currentUser.getIdToken(true).addOnCompleteListener(new OnCompleteListener<GetTokenResult>() {
            public void onComplete(@NonNull Task<GetTokenResult> task) {
                if (task.isSuccessful()) {
                    String userToken = task.getResult().getToken();
                    // Send token to your backend via HTTPS
                    // ...

                    userStore.setToken(userToken);
                    createUserDB(userToken);

                } else {
                    Toast.makeText(getContext(), "No se pudo obtener TOKEN.", Toast.LENGTH_SHORT).show();
                    // Handle error -> task.getException();
                }
            }
        });
    }





    // Esto deberia estar en UserRepository con un metodo createUserDB
    private void createUserDB(String userToken){

        IWebservice iApi = WebserviceBuilder.getInstance().create(IWebservice.class);

        Call<User> call = iApi.createUser(userToken, userStore);

        call.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if(!response.isSuccessful()){
                    Toast.makeText(getContext(), "Not successful "+response.code(), Toast.LENGTH_SHORT).show();
                    return;
                }

                //Toast.makeText(getContext(), "TODO OK: "+response.body(), Toast.LENGTH_SHORT).show();
                Log.d(TAG, "TODO OK: "+response.body());
                loadApp();

            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                Toast.makeText(getContext(), "Failure: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }




    private void loadApp(){
        Intent intent = new Intent(getContext(), MainActivity.class);
        startActivity(intent);
        getActivity().finish();
    }
}
