package com.norbertoledo.petportal.ui.app;

import android.app.Dialog;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.norbertoledo.petportal.R;
import com.norbertoledo.petportal.models.Place;
import com.norbertoledo.petportal.models.ReferencePlaces;
import com.norbertoledo.petportal.models.State;
import com.norbertoledo.petportal.utils.PlacesAdapter;
import com.norbertoledo.petportal.utils.PlacesInfoWindowDetail;
import com.norbertoledo.petportal.viewmodels.LocationViewModel;
import com.norbertoledo.petportal.viewmodels.PlacesViewModel;
import com.norbertoledo.petportal.viewmodels.UserViewModel;

import java.util.ArrayList;
import java.util.List;

public class PlacesFragment extends Fragment implements OnMapReadyCallback, GoogleMap.OnInfoWindowClickListener {

    private LocationViewModel locationViewModel;
    private PlacesViewModel placesViewModel;
    private UserViewModel userViewModel;
    private List<Place> listPlaces;
    private List<ReferencePlaces> referencePlacesList;
    private ArrayAdapter<ReferencePlaces> adapter;
    private ListView placesListView;
    private MapView mapView;
    private double lat, lng;
    private LatLng initialLocation;
    private LatLng latLng;
    private final float DEFAULT_ZOOM = 7;
    private String title;
    private String caption;
    private String category;
    private String color;
    private int colorInt;
    private float colorHsv;
    private GoogleMap gMap;
    private Marker marker;
    private Dialog dialog;
    private View view;
    private LinearLayout placesReferences;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_places, container, false);

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        dialog = new Dialog(getActivity());

        placesReferences = view.findViewById(R.id.placesReferences);
        placesListView = view.findViewById(R.id.placesListView);
        mapView = view.findViewById(R.id.mapView);

        mapView.onCreate(savedInstanceState);
        mapView.getMapAsync(this);

        referencePlacesList = new ArrayList<ReferencePlaces>();

        userViewModel = new ViewModelProvider(getActivity()).get(UserViewModel.class);

        placesViewModel = new ViewModelProvider(getActivity()).get(PlacesViewModel.class);
        placesViewModel.init(userViewModel.getUserToken().getValue());

        locationViewModel = new ViewModelProvider(getActivity()).get(LocationViewModel.class);

    }


    // Retorna el color HSV para el marcador
    public float colorIntToHsv(int color) {
        float[] hsv = new float[3];
        Color.colorToHSV(color, hsv);
        return hsv[0];
    }

    @Override
    public void onMapReady(GoogleMap map) {
        gMap = map;

        // Set a listener for info window events.
        gMap.setOnInfoWindowClickListener(this);
        // Habilitar toolbar de Ruta y Mapa
        gMap.getUiSettings().setMapToolbarEnabled(true);

        placesViewModel.getPlaces().observe(getViewLifecycleOwner(), new Observer<List<Place>>() {
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onChanged(List<Place> places) {
                if(places!=null){

                    listPlaces = places;

                    locationViewModel.getLocation().observe(getViewLifecycleOwner(), new Observer<State>() {
                        @Override
                        public void onChanged(State location) {
                            lat = locationViewModel.getLocation().getValue().getLatlng().get("lat");
                            lng = locationViewModel.getLocation().getValue().getLatlng().get("long");
                            initialLocation = new LatLng(lat,lng);
                            gMap.moveCamera(CameraUpdateFactory.newLatLngZoom(initialLocation, DEFAULT_ZOOM));
                        }
                    });


                    for (Place place : listPlaces) {
                        lat = place.getLatlng().get("lat");
                        lng = place.getLatlng().get("long");
                        latLng = new LatLng(lat,lng);
                        title = place.getTitle();
                        caption = place.getCaption()+" - [+ info]";
                        category = place.getCategory();
                        color = place.getColor();


                        boolean isCategoryExist = false;

                        for (int i = 0; i < referencePlacesList.size() ; i++) {
                            if( referencePlacesList.get(i).getCategory().equals(category) ){
                                isCategoryExist = true;
                                break;
                            }
                        }
                        if(!isCategoryExist){
                            ReferencePlaces newRef = new ReferencePlaces( category, color );
                            newRef.setCategory(category);
                            newRef.setColor(color);
                            referencePlacesList.add(newRef);
                        }

                        colorInt = Color.parseColor(color);
                        colorHsv = colorIntToHsv(colorInt);


                        marker = gMap.addMarker(
                                new MarkerOptions()
                                        .position( latLng )
                                        .title(title)
                                        .snippet(caption)
                                        .icon(BitmapDescriptorFactory.defaultMarker(colorHsv))

                        );
                        marker.setTag(place);
                    }

                    // References List
                    adapter = new PlacesAdapter(getActivity(), R.layout.item_list_places, referencePlacesList);
                    placesListView.setAdapter(adapter);

                    placesReferences.setMinimumWidth(calculateWidth(placesListView) + placesReferences.getPaddingLeft() + placesReferences.getPaddingRight());


                }
            }
        });


    }

    // Retorna el valor del item mas ancho
    private int calculateWidth(ListView list) {
        int width = 0;
        for (int i = 0; i < list.getCount(); i++) {
            View childView = list.getAdapter().getView(i, null, list);
            childView.measure(View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED), View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));
            if(childView.getMeasuredWidth() > width ){
                width = childView.getMeasuredWidth();
            }
        }
        return width;
    }


    @Override
    public void onInfoWindowClick(Marker marker) {
        Place p = (Place) marker.getTag();
        PlacesInfoWindowDetail placesInfo = new PlacesInfoWindowDetail();
        placesInfo.newInstance(p).show(getActivity().getSupportFragmentManager(), null);
    }


    @Override
    public void onResume() {
        super.onResume();
        mapView.onResume();
    }

    @Override
    public void onStart() {
        super.onStart();
        mapView.onStart();
    }

    @Override
    public void onStop() {
        super.onStop();
        mapView.onStop();
    }

    @Override
    public void onPause() {
        mapView.onPause();
        super.onPause();
    }

    @Override
    public void onDestroy() {
        mapView.onDestroy();
        super.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapView.onLowMemory();
    }


}
