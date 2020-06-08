package com.norbertoledo.petportal;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.NavDestination;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.signature.ObjectKey;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.norbertoledo.petportal.models.State;
import com.norbertoledo.petportal.models.User;
import com.norbertoledo.petportal.utils.LocationDialog;
import com.norbertoledo.petportal.viewmodels.LocationViewModel;
import com.norbertoledo.petportal.viewmodels.StatesViewModel;
import com.norbertoledo.petportal.viewmodels.UserViewModel;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MAIN ACTIVITY";

    private FirebaseAuth mAuth;
    private TextView headerName;
    private TextView headerEmail;
    private ImageView headerPhoto;
    private DrawerLayout drawer;
    private NavigationView navigationView;
    private NavController navController;
    private Toolbar toolbar;
    private AppBarConfiguration mAppBarConfiguration;
    private UserViewModel userViewModel;
    private LocationViewModel locationViewModel;
    private StatesViewModel statesViewModel;
    private List<State> listStates;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Layout
        setContentView(R.layout.activity_main);

        // Instances
        navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        mAuth = FirebaseAuth.getInstance();
        toolbar = findViewById(R.id.toolbar);
        drawer = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);

        // Navigation
        setActionBar();
        destinationHandler();
        setDrawerAction();

        Log.d(TAG, "****************** CARGO MAIN ACTIVITY ************** ");
        // User ViewModel
        userViewModel = new ViewModelProvider(this).get(UserViewModel.class);
        locationViewModel = new ViewModelProvider(this).get(LocationViewModel.class);

        statesViewModel = new ViewModelProvider(this).get(StatesViewModel.class);

        userViewModel.getUserToken().observe(this, new Observer<String>(){

            @Override
            public void onChanged(String token) {
                if(token!=null){

                    //loadUserInfo();
                    loadStates();
                }
            }

        });

    }
    private void loadStates(){
        statesViewModel.init();
        statesViewModel.getStates().observe(this, new Observer<List<State>>(){

            @Override
            public void onChanged(List<State> states) {
                if(states!=null){
                    listStates = states;
                    Log.d("STATEEEEEEEEEES -> ", String.valueOf(listStates.size()));

                        loadUserInfo();

                }
            }

        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.toolbar_nav_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == R.id.locationItem){

            LocationDialog.show(MainActivity.this, MainActivity.this);
        }
        return super.onOptionsItemSelected(item);
    }

    private void loadUserInfo(){

        userViewModel.getUserData().observe(this, new Observer<User>() {
            @Override
            public void onChanged(User user) {
                Log.d(TAG, "****************** GET USER CHANGED ************** ");


                if(user!=null){

                    String city = user.getCity();
                    State state = null;

                    for (int i = 0; i < listStates.size() ; i++) {
                        if(listStates.get(i).getName().equals(city)){
                            state = listStates.get(i);
                        }
                    }

                    // Seteo la ciudad
                    locationViewModel.setLocation( state );

                    Log.d(TAG, "USER-> "+user.toString());
                    Log.d(TAG, "USER TOKEN -> "+userViewModel.getUserToken().getValue());
                    String name = user.getName();
                    String email = user.getEmail();
                    String photoUrl = user.getPhotoUrl();

                    View header = navigationView.getHeaderView(0);

                    headerName = header.findViewById(R.id.headerName);
                    headerEmail = header.findViewById(R.id.headerEmail);
                    headerPhoto = header.findViewById(R.id.headerPhoto);

                    if(user.getName() == null){
                        name = "Usuario sin nombre";
                    }

                    headerName.setText(name);
                    headerEmail.setText(email);


                    ObjectKey signature = null;

                    if( userViewModel.getImageProfileSignature() != null ){
                        signature = userViewModel.getImageProfileSignature();

                    }else{
                        signature = new ObjectKey(String.valueOf(System.currentTimeMillis()));
                        userViewModel.setImageProfileSignature(signature);
                    }

                    Glide.with(MainActivity.this)
                            .load(photoUrl)
                            .signature(signature)
                            //.circleCrop()
                            .centerCrop()
                            .error(R.mipmap.ic_launcher_round)
                            .into(headerPhoto);
                }


            }
        });

    }


    private void setActionBar(){
        setSupportActionBar(toolbar);
        mAppBarConfiguration = new AppBarConfiguration
                .Builder(
                R.id.homeFragment,
                R.id.servicesCategoryFragment,
                R.id.placesFragment,
                R.id.tipsFragment,
                R.id.linksFragment
        )
                .setOpenableLayout(drawer)
                .build();

        NavigationUI.setupWithNavController(navigationView, navController);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
    }

    private void destinationHandler() {
        navController.addOnDestinationChangedListener(new NavController.OnDestinationChangedListener() {
            @Override
            public void onDestinationChanged(@NonNull NavController controller, @NonNull NavDestination destination, @Nullable Bundle arguments) {

                int dest = destination.getId();

                if(dest == R.id.authFragment || dest == R.id.signInFragment || dest == R.id.signUpFragment){
                    drawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
                    toolbar.setVisibility(View.GONE);

                }else{
                    drawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);
                    toolbar.setVisibility(View.VISIBLE);
                    drawer.closeDrawers();
                    navigationView.setCheckedItem(dest);
                }

            }
        });
    }


    private void setDrawerAction() {


        navigationView.setNavigationItemSelectedListener(

                new NavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(MenuItem item) {

                        if( item.isChecked() ){
                            drawer.closeDrawers();
                            return false;
                        }

                        int itemId = item.getItemId();

                        switch (itemId) {

                            case R.id.homeFragment:
                                navController.navigate(R.id.action_global_homeFragment);
                                break;

                            case R.id.servicesFragment:
                                navController.navigate(R.id.action_global_servicesCategoryFragment);
                                break;

                            case R.id.placesFragment:
                                navController.navigate(R.id.action_global_placesFragment);
                                break;
                            case R.id.tipsFragment:
                                navController.navigate(R.id.action_global_tipsFragment);
                                break;
                            case R.id.linksFragment:
                                navController.navigate(R.id.action_global_linksFragment);
                                break;

                            case R.id.profileFragment:
                                navController.navigate(R.id.action_global_profileFragment);
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


    public void logout(){
        userViewModel.resetUserData();
        mAuth.signOut();
        navController.navigate(R.id.action_global_signInFragment);
    }




}
