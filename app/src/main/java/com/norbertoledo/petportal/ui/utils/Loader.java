package com.norbertoledo.petportal.ui.utils;

import android.app.Activity;

import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.norbertoledo.petportal.MainActivity;

public class Loader {
/*
    private static final String TAG = "LOADER";
    private static Loader instance;
    private Activity activity;
    */

    private static LoaderFragment fragment;
    private static FragmentManager fragmentManager;
    private static FragmentTransaction fragmentTransaction;
/*
    public static Loader getInstance(Activity activity){
        if(instance == null){
            instance = new Loader(activity);
        }
        return instance;
    }

    private Loader(Activity activity){
        this.activity = activity;
    }
    */

    public static void show(Activity activity, int container, int message){
        fragmentManager = ((MainActivity) activity).getSupportFragmentManager();
        fragmentTransaction = fragmentManager.beginTransaction();
        fragment = new LoaderFragment();
        fragmentTransaction.add(container, fragment);
        fragment.message = message;
        fragmentTransaction.commit();
    }

    public static void hide(){
        fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.remove(fragment);
        fragmentTransaction.commit();
    }
}
