package com.norbertoledo.petportal.ui.splash;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import com.norbertoledo.petportal.MainActivity;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        //startActivity(new Intent(this, AuthActivity.class));
        startActivity(new Intent(this, MainActivity.class));
        //setContentView(R.layout.activity_auth);
    }
}
