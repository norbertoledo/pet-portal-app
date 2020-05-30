package com.norbertoledo.petportal.ui.utils;

import android.app.ProgressDialog;
import android.content.Context;
import android.widget.TextView;

import com.norbertoledo.petportal.R;

public class LoaderDialog {

    private static final String TAG = "LOADER";
    private static LoaderDialog instance;
    private ProgressDialog loader;
    private TextView loaderMessage;

    public static LoaderDialog getInstance(Context context){
        if(instance == null){
            instance = new LoaderDialog(context);
        }
        return instance;
    }

    private LoaderDialog(Context context){
        loader = new ProgressDialog(context);
        loader.setCancelable(false);
        loader.getWindow().setBackgroundDrawableResource(
                android.R.color.transparent
        );
    }


    public void show(int message){
        loader.show();
        loader.setContentView(R.layout.loader_dialog);
        loaderMessage = loader.findViewById(R.id.loaderMessage);
        loaderMessage.setText(message);
    }

    public void hide(){
        loader.dismiss();
    }



}
