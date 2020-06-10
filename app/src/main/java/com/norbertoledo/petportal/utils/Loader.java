package com.norbertoledo.petportal.utils;

import android.app.Activity;

import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.norbertoledo.petportal.MainActivity;

public class Loader {

    private static LoaderFragment fragment;
    private static FragmentManager fragmentManager;
    private static FragmentTransaction fragmentTransaction;
    private static boolean visible;


    public static void show(Activity activity, int container, int message){
        setIsVisible(true);
        fragmentManager = ((MainActivity) activity).getSupportFragmentManager();
        fragmentTransaction = fragmentManager.beginTransaction();
        fragment = new LoaderFragment();
        fragmentTransaction.add(container, fragment);
        fragment.message = message;
        fragmentTransaction.commit();
    }

    public static void hide(){
        setIsVisible(false);
        fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.remove(fragment);
        fragmentTransaction.commit();
    }

    public static boolean isVisible() {
        return visible;
    }

    public static void setIsVisible(boolean isVisible) {
        Loader.visible = isVisible;
    }
}
