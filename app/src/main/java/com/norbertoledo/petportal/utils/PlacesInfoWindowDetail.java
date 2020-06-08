package com.norbertoledo.petportal.utils;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.norbertoledo.petportal.R;
import com.norbertoledo.petportal.models.Place;

import java.io.Serializable;

public class PlacesInfoWindowDetail extends DialogFragment {

    private View view;
    public static Place place;
    private ImageView placeImage;
    private TextView placeTitle, placeDescription;
    private AlertDialog dialog;
    int displayWidth;
    int displayHeight;
    int finalWidth;
    int finalHeight;

    private static final String PLACE_KEY = "place_key";


    public PlacesInfoWindowDetail newInstance(Place place) {

        this.place = place;
        PlacesInfoWindowDetail fragment = new PlacesInfoWindowDetail();
        return fragment;
    }


    public PlacesInfoWindowDetail() {

    }


    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity(), R.style.CustomDialog);
        // Get the layout inflater
        LayoutInflater inflater = getActivity().getLayoutInflater();
        view = inflater.inflate(R.layout.places_info_window_detail, null);

        builder.setView(view);

        dialog = builder.create();

        placeImage = view.findViewById(R.id.placeImage);
        placeTitle = view.findViewById(R.id.placeTitle);
        placeDescription = view.findViewById(R.id.placeDescription);

        setContent();
        return dialog;

    }


    public void setContent(){
        Glide.with(getContext())
                .load(place.getImage())
                .centerCrop()
                .into(placeImage);
        placeTitle.setText(place.getTitle());
        placeDescription.setText(place.getDescription());
    }

    @Override
    public void onResume() {
        super.onResume();
        displayWidth = (int)(getResources().getDisplayMetrics().widthPixels);
        displayHeight = (int)(getResources().getDisplayMetrics().heightPixels);

        finalWidth = (int) Math.round(displayWidth*0.90);
        finalHeight = (int) (dialog.getWindow().getAttributes().height);

        if( finalWidth > 1024 ){
            finalWidth = (int) Math.round(displayWidth*0.70);
        }else if( finalWidth > 1280 ){
            finalWidth = (int) Math.round(displayWidth*0.50);
        }

        if(finalHeight > (int) Math.round(displayHeight*0.90)){
            finalHeight = (int) Math.round(displayHeight*0.90);
        }

        dialog.getWindow().setLayout(finalWidth, finalHeight);

    }


}
