package com.norbertoledo.petportal.ui.app;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.norbertoledo.petportal.R;
import com.norbertoledo.petportal.ui.auth.AuthActivity;

public class MainActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private TextView headerDisplayName;
    private TextView headerEmail;


    private DrawerLayout drawer;
    private NavigationView navigationView;
    private NavController navController;
    private Toolbar toolbar;

    private AppBarConfiguration mAppBarConfiguration;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mAuth = FirebaseAuth.getInstance();

        setContentView(R.layout.activity_main);
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        drawer = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);


        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home,
                R.id.nav_services,
                R.id.nav_places,
                R.id.nav_tips,
                R.id.nav_links
                )
                .setDrawerLayout(drawer)
                .build();

        navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupWithNavController(navigationView, navController);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);


        setupDrawerContent();
        showUserInfo();

    }


    private void setupDrawerContent() {
        navigationView.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(MenuItem item) {
                        drawer.closeDrawers();
                        int id = item.getItemId();

                        switch (id) {

                            case R.id.nav_home:
                                navController.navigate(R.id.nav_home);
                                break;

                            case R.id.nav_services:
                                navController.navigate(R.id.nav_services);
                                break;

                            case R.id.nav_places:
                                navController.navigate(R.id.nav_places);
                                break;
                            case R.id.nav_tips:
                                navController.navigate(R.id.nav_tips);
                                break;
                            case R.id.nav_links:
                                navController.navigate(R.id.nav_links);
                                break;

                            case R.id.nav_profile:
                                showUserInfo();
                                break;
                            case R.id.nav_logout:
                                logout();
                                break;

                        }
                        return true;
                    }
                });
    }

    @Override
    public boolean onSupportNavigateUp() {
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }



    private void logout(){
        mAuth.signOut();
        startActivity(new Intent(MainActivity.this, AuthActivity.class));
        finish();
    }

    private FirebaseUser getUserInfo(){
        return mAuth.getCurrentUser();
    }

    private void showUserInfo(){

        FirebaseUser user = getUserInfo();

        if(user != null){

            String name = user.getDisplayName();
            String email = user.getEmail();
            Uri photoUrl = user.getPhotoUrl();

            View header = navigationView.getHeaderView(0);

            headerDisplayName = header.findViewById(R.id.headerDisplayName);
            headerEmail = header.findViewById(R.id.headerEmail);


            if(user.getDisplayName() == null){
                name = "Bienvenido";
            }

            headerDisplayName.setText(name);
            headerEmail.setText(user.getEmail());

        }else{
            Toast.makeText(this, "Usuario no válido", Toast.LENGTH_SHORT).show();
        }

    }



}
