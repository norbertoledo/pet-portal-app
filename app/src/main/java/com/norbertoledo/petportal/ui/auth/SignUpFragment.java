package com.norbertoledo.petportal.ui.auth;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;


import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GetTokenResult;
import com.norbertoledo.petportal.R;
import com.norbertoledo.petportal.models.State;
import com.norbertoledo.petportal.repositories.webservice.WebserviceBuilder;
import com.norbertoledo.petportal.repositories.webservice.IWebservice;
import com.norbertoledo.petportal.models.User;
import com.norbertoledo.petportal.utils.Loader;
import com.norbertoledo.petportal.viewmodels.StatesViewModel;
import com.norbertoledo.petportal.viewmodels.UserViewModel;


import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SignUpFragment extends Fragment {

    private static final String TAG = "SIGN UP";

    private FirebaseAuth mAuth;
    private FirebaseUser currentUser;
    private User newUser;
    private UserViewModel userViewModel;
    private EditText signupEmail, signupPassword, signupConfirmPassword, signupName;
    private Button signupBtnLogin, signupBtnRegister;
    private Spinner signupSpinner;
    private ArrayAdapter<String> adapter;
    private NavController navController;
    private String email;
    private String password;
    private String confirmPassword;
    private String name;
    private String city;
    private StatesViewModel statesViewModel;
    private View view;

    public SignUpFragment(){}

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_signup, container, false);

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();

        signupEmail = view.findViewById(R.id.signupEmail);
        signupPassword = view.findViewById(R.id.signupPassword);
        signupConfirmPassword = view.findViewById(R.id.signupConfirmPassword);
        signupName = view.findViewById(R.id.signupName);
        signupBtnLogin = view.findViewById(R.id.signupBtnLogin);
        signupBtnRegister = view.findViewById(R.id.signupBtnRegister);
        signupSpinner = view.findViewById(R.id.signupSpinner);

        navController = Navigation.findNavController(getActivity(), R.id.nav_host_fragment);

        statesViewModel = new ViewModelProvider(getActivity()).get(StatesViewModel.class);
        userViewModel = new ViewModelProvider(getActivity()).get(UserViewModel.class);

        statesViewModel.init();
        statesViewModel.getStates().observe(getActivity(), new Observer<List<State>>() {
            @Override
            public void onChanged(List<State> states) {

                if(states!= null){

                    int size = states.size();

                    ArrayList<String> arr = new ArrayList<>();

                    for( int i=0; i<size; i++ ){
                        arr.add(states.get(i).getName());
                    }

                    // Create an ArrayAdapter using the string array and a default spinner layout
                    adapter = new ArrayAdapter<String>(
                            getActivity(),
                            android.R.layout.simple_spinner_item,
                            arr
                    );

                    // Specify the layout to use when the list of choices appears
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

                    // Apply the adapter to the spinner
                    signupSpinner.setAdapter(adapter);

                    if(Loader.isVisible()){
                        Loader.hide();
                    };

                }else{
                    Loader.show(getActivity(), R.id.signUpFragment, R.string.loader_message_load);
                }
            }
        });

        signupBtnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gotoSignIn();
            }
        });

        signupBtnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validateForm();
            }
        });

    }

    //En este metodo verificamos si los campos no estan vacios
    private void validateForm() {

        email = signupEmail.getText().toString();
        password = signupPassword.getText().toString();
        confirmPassword = signupConfirmPassword.getText().toString();
        name = signupName.getText().toString();
        city = signupSpinner.getSelectedItem().toString();

        if (!email.isEmpty() || !password.isEmpty() || !name.isEmpty()){
            if (arePasswordEquals()){
                createUserFirebase();
            } else {
                Snackbar.make(view, R.string.signup_error_confirm_password, Snackbar.LENGTH_LONG).show();
            }
        } else {
            Snackbar.make(view, R.string.signup_error_required_field, Snackbar.LENGTH_LONG).show();
        }
    }

    //Funcion que verifica si ambas contraseñas son iguales
    private boolean arePasswordEquals() {
        return password.equals(confirmPassword);
    }

    private void createUserFirebase(){

        Loader.show(getActivity(), R.id.signupFragment, R.string.signup_process);

        mAuth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    currentUser = mAuth.getCurrentUser();
                } else {
                    String error = getString(R.string.signup_error)+" "+task.getException().getMessage();
                    Snackbar.make(view, error, Snackbar.LENGTH_LONG).show();
                }
                updateUI();
                }
            });
    }



    private void updateUI() {
        if(currentUser != null) {
            getIdToken();
        }else{
            Snackbar.make(view, R.string.firebase_error_user, Snackbar.LENGTH_LONG).show();
        }
    }

    private void getIdToken(){

        currentUser.getIdToken(true).addOnCompleteListener(new OnCompleteListener<GetTokenResult>() {
            public void onComplete(@NonNull Task<GetTokenResult> task) {
                if (task.isSuccessful()) {
                    String userToken = task.getResult().getToken();
                    createUserDB(userToken);
                } else {
                    Snackbar.make(view, R.string.firebase_error_token, Snackbar.LENGTH_LONG).show();
                }
            }
        });
    }



    // Crea usuario
    private void createUserDB(final String token){
        newUser = User.getInstance();
        newUser.setUid(currentUser.getUid());
        newUser.setEmail(currentUser.getEmail());
        newUser.setName(name);
        newUser.setCity(city);
        newUser.setPhotoUrl("");

        userViewModel.newUser(token, newUser);

        userViewModel.newUserResponse().observe(getActivity(), new Observer<User>() {
            @Override
            public void onChanged(User user) {
                if(user!=null){
                    Loader.hide();
                    Snackbar.make(view, R.string.signup_success, Snackbar.LENGTH_SHORT).show();
                    userViewModel.setUserToken(token);
                    userViewModel.setUserData(user);
                    gotoAuth();
                }
            }
        });
    }



    private void gotoSignIn() { navController.navigate(R.id.action_signUpFragment_to_signInFragment); }
    private void gotoAuth() { navController.navigate(R.id.action_global_authFragment); }
}
