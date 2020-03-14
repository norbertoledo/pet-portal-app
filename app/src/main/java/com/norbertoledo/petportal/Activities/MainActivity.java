package com.norbertoledo.petportal.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.norbertoledo.petportal.Fragments.HomeFragment;
import com.norbertoledo.petportal.Fragments.LinksFragment;
import com.norbertoledo.petportal.Fragments.PlacesFragment;
import com.norbertoledo.petportal.R;
import com.norbertoledo.petportal.Fragments.ServicesFragment;
import com.norbertoledo.petportal.Fragments.TipsFragment;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private FirebaseAuth mAuth;
    private DrawerLayout drawer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mAuth = FirebaseAuth.getInstance();

        setContentView(R.layout.activity_home);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        drawer = findViewById(R.id.drawer_layout);


        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);


        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.setDrawerIndicatorEnabled(true);
        toggle.syncState();

        if(savedInstanceState == null){
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new HomeFragment()).commit();
            navigationView.setCheckedItem(R.id.nav_home);
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()){
            case R.id.nav_home:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new HomeFragment()).commit();
                break;
            case R.id.nav_services:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new ServicesFragment()).commit();
                break;
            case R.id.nav_places:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new PlacesFragment()).commit();
                break;
            case R.id.nav_tips:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new TipsFragment()).commit();
                break;
            case R.id.nav_links:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new LinksFragment()).commit();
                break;
            case R.id.nav_profile:
                showUserInfo();
                break;
            case R.id.nav_logout:
                logout();
                break;
        }

        drawer.closeDrawer(GravityCompat.START);

        return true;
    }

    @Override
    public void onBackPressed() {
        if(drawer.isDrawerOpen(GravityCompat.START)){
            drawer.closeDrawer(GravityCompat.START);
        }else{
            super.onBackPressed();
        }
    }

    private void logout(){

        Toast.makeText(this, "¡¡¡ LOGOUT !!!", Toast.LENGTH_LONG).show();
        /*
        mAuth.signOut();
        startActivity(new Intent(AuthActivity.this, SignInActivity.class));
        finish();
        */
    }

    private void showUserInfo(){
        FirebaseUser currentUser = mAuth.getCurrentUser();
        String email = currentUser.getEmail();
        Toast.makeText(this, email, Toast.LENGTH_SHORT).show();
    }



}
