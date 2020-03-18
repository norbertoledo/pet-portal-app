package com.norbertoledo.petportal.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.norbertoledo.petportal.R;

public class AuthActivity extends AppCompatActivity {

    private static final String TAG = "AUTH ACTIVITY";
/*
    private FirebaseAuth mAuth;
    private Store store;
*/

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auth);
/*
        store = (Store) getApplicationContext();
        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();
        */

    }

    @Override
    protected void onStart() {
        super.onStart();
        /*
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        updateUI(currentUser);

         */

    }
/*
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
                    // Send token to your backend via HTTPS
                    // ...
                    store.setUserToken(userToken);
                    loadApp();

                } else {
                    // Handle error -> task.getException();
                }
            }
        });
    }

    private void loadApp(){
        Intent intent = new Intent(AuthActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    private void loadSignIn(){

        // Login Fragment
        SignInFragment signinFragment = new SignInFragment();
        getSupportFragmentManager()
                .beginTransaction()
                .add(R.id.nav_host_auth, signinFragment)
                .commit();

    }
*/

}
