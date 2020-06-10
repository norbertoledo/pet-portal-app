package com.norbertoledo.petportal.utils;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelStoreOwner;

import com.norbertoledo.petportal.R;
import com.norbertoledo.petportal.models.State;
import com.norbertoledo.petportal.viewmodels.LocationViewModel;
import com.norbertoledo.petportal.viewmodels.StatesViewModel;

import java.util.ArrayList;
import java.util.List;

public class LocationDialog extends Fragment {

    private static final String TAG = "LOADER";
    private static ProgressDialog dialog;
    private static ListView listView;
    private static StatesViewModel statesViewModel;
    private static LocationViewModel locationViewModel;
    private static List<State> listStates;
    private static List<String> arrStates;
    private static ArrayAdapter<String> adapter;
    private static Button btnDismiss;
    private static LinearLayout location_dialog;

    public static void show(Context context, final Activity activity){
        dialog = new ProgressDialog(context);
        dialog.getWindow().setBackgroundDrawableResource(
                android.R.color.transparent
        );

        dialog.show();
        dialog.setContentView(R.layout.fragment_location_dialog);
        dialog.setCancelable(true);
        dialog.setCanceledOnTouchOutside(true);
        listView = dialog.findViewById(R.id.locationListView);
        btnDismiss = dialog.findViewById(R.id.locationDismiss);
        location_dialog = dialog.findViewById(R.id.location_dialog);
        arrStates = new ArrayList<>();


        locationViewModel = new ViewModelProvider((ViewModelStoreOwner) activity).get(LocationViewModel.class);
        // States ViewModel
        statesViewModel = new ViewModelProvider((ViewModelStoreOwner) activity).get(StatesViewModel.class);
        statesViewModel.init();
        statesViewModel.getStates().observe((LifecycleOwner) activity, new Observer<List<State>>() {
            @Override
            public void onChanged(List<State> states) {
                if(states!= null){

                    listStates = states;
                    int size = states.size();
                    for( int i=0; i<size; i++ ){
                        arrStates.add(states.get(i).getName());
                    }

                    adapter = new ArrayAdapter<String>(activity, android.R.layout.simple_list_item_1, arrStates);

                    listView.setAdapter(adapter);
                }
            }
        });


        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                locationViewModel.setLocation(listStates.get(position));
                LocationDialog.hide();
            }
        });


        btnDismiss.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hide();
            }
        });



    }


    public static void hide(){
        dialog.dismiss();
    }


}
