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
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.signature.ObjectKey;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.norbertoledo.petportal.models.State;
import com.norbertoledo.petportal.models.User;
import com.norbertoledo.petportal.viewmodels.LocationViewModel;
import com.norbertoledo.petportal.viewmodels.StatesViewModel;
import com.norbertoledo.petportal.viewmodels.UserViewModel;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MAIN ACTIVITY";

    private FirebaseAuth mAuth;
    private View drawerHeader;
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
    private String userName;
    private String userEmail;
    private String userPhotoUrl;



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

        drawerHeader = navigationView.getHeaderView(0);
        headerName = drawerHeader.findViewById(R.id.headerName);
        headerEmail = drawerHeader.findViewById(R.id.headerEmail);
        headerPhoto = drawerHeader.findViewById(R.id.headerPhoto);

        // Navigation
        setActionBar();
        destinationHandler();
        setDrawerAction();

        // ViewModels
        userViewModel = new ViewModelProvider(this).get(UserViewModel.class);
        locationViewModel = new ViewModelProvider(this).get(LocationViewModel.class);
        statesViewModel = new ViewModelProvider(this).get(StatesViewModel.class);

        statesViewModel.init();
        statesViewModel.getStates().observe(this, new Observer<List<State>>(){

            @Override
            public void onChanged(List<State> states) {
                if(states!=null){
                    listStates = states;
                    waitUserToken();
                }
            }

        });

        drawerHeader.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navController.navigate(R.id.action_global_profileFragment);
            }
        });

    }

    private void waitUserToken(){
        userViewModel.getUserToken().observe(this, new Observer<String>(){

            @Override
            public void onChanged(String token) {
                if(token!=null){
                    loadUserInfo();
                }
            }

        });
    }


    private void loadUserInfo(){

        userViewModel.getUserData().observe(this, new Observer<User>() {
            @Override
            public void onChanged(User user) {

                if(user!=null){

                    String city = user.getCity();
                    State state = null;

                    for (int i = 0; i < listStates.size() ; i++) {
                        if(listStates.get(i).getName().equals(city)){
                            state = listStates.get(i);
                        }
                    }

                    // Seteo la ciudad
                    if(locationViewModel.getLocation().getValue() == null){
                        locationViewModel.setLocation( state );
                    }

                    userName = user.getName();
                    userEmail = user.getEmail();
                    userPhotoUrl = user.getPhotoUrl();


                    if(user.getName() == null){
                        userName = "Usuario sin nombre";
                    }

                    headerName.setText(userName);
                    headerEmail.setText(userEmail);


                    ObjectKey signature = null;

                    if( userViewModel.getImageProfileSignature() != null ){
                        signature = userViewModel.getImageProfileSignature();

                    }else{
                        signature = new ObjectKey(String.valueOf(System.currentTimeMillis()));
                        userViewModel.setImageProfileSignature(signature);
                    }

                    Glide.with(MainActivity.this)
                            .load(userPhotoUrl)
                            .signature(signature)
                            .centerCrop()
                            .error(R.drawable.no_image)
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
        locationViewModel.resetLocation();
        mAuth.signOut();
        navController.navigate(R.id.action_global_signInFragment);
    }


}
