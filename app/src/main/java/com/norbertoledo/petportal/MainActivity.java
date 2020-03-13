package com.norbertoledo.petportal;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GetTokenResult;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MAIN";

    private FirebaseAuth mAuth;
    private StoreClass store;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        store = (StoreClass) getApplicationContext();


        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();
    }

    @Override
    protected void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        updateUI(currentUser);
    }

    private void updateUI(FirebaseUser currentUser) {
        Log.d(TAG, "currentUser-> "+currentUser);
        if(currentUser != null) {
            getIdToken(currentUser);
        }else{
            gotoSignIn();
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
                    gotoHome();

                } else {
                    // Handle error -> task.getException();
                }
            }
        });
    }

    private void gotoHome(){
        //Intent intent = new Intent(SignInActivity.this, LinksActivity.class);
        Intent intent = new Intent(MainActivity.this, HomeActivity.class);
        startActivity(intent);
        finish();
    }

    private void gotoSignIn(){
        Intent intent = new Intent(MainActivity.this, SignInActivity.class);
        startActivity(intent);
        finish();
    }
}
