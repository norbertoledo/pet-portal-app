package com.norbertoledo.petportal.ui.app;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.bumptech.glide.Glide;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.norbertoledo.petportal.R;
import com.norbertoledo.petportal.models.Service;
import com.norbertoledo.petportal.viewmodels.ServicesViewModel;

public class ServiceFragment extends Fragment  implements OnMapReadyCallback {
    private View view;
    private ServicesViewModel servicesViewModel;
    private Service service;
    private ImageView serviceImage;
    private TextView serviceName, serviceDescription, serviceAddress, servicePhone, serviceWebsite, serviceUbicacion;
    private MapView serviceMapView;
    private GoogleMap gMap;
    private final float DEFAULT_ZOOM = 12;
    private LatLng latLng;
    private Double lat;
    private Double lng;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_service, container, false);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        servicesViewModel = new ViewModelProvider(getActivity()).get(ServicesViewModel.class);

        service = servicesViewModel.getSelectedService().getValue();

        serviceImage = view.findViewById(R.id.serviceImage);
        serviceName = view.findViewById(R.id.serviceName);
        serviceDescription = view.findViewById(R.id.serviceDescription);
        serviceAddress = view.findViewById(R.id.serviceAddress);
        servicePhone = view.findViewById(R.id.servicePhone);
        serviceWebsite = view.findViewById(R.id.serviceWebsite);
        serviceUbicacion = view.findViewById(R.id.serviceUbicacion);
        serviceMapView = view.findViewById(R.id.serviceMapView);

        serviceMapView.onCreate(savedInstanceState);
        serviceMapView.getMapAsync(this);

        setView();

    }

    public void setView(){
        Glide.with(getActivity()).load(service.getImage()).centerCrop().into(serviceImage);
        serviceName.setText( Html.fromHtml(service.getName()) );
        serviceDescription.setText( Html.fromHtml(service.getDescription()) );
        serviceAddress.setText( Html.fromHtml("<b>Dirección: </b>" + service.getAddress()) );
        servicePhone.setText( Html.fromHtml("<b>Teléfono: </b>" + service.getPhone()) );
        serviceWebsite.setText( Html.fromHtml("<b>Website: </b>" + service.getWebsite()) );
        serviceUbicacion.setText( Html.fromHtml("<b>Ubicación</b>"));

        serviceWebsite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openLink(service.getWebsite());
            }
        });
    }

    private void openLink(String url){
        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
        startActivity(browserIntent);
    }

    @Override
    public void onMapReady(GoogleMap map) {
        gMap = map;

        gMap.getUiSettings().setZoomControlsEnabled(true);
        gMap.getUiSettings().setCompassEnabled(true);
        gMap.getUiSettings().setMapToolbarEnabled(true);

        lat = service.getLatlng().get("lat");
        lng = service.getLatlng().get("long");
        latLng = new LatLng(lat,lng);

        gMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, DEFAULT_ZOOM));

        gMap.addMarker(
                new MarkerOptions()
                        .position( latLng )
                        .title(service.getName())
                        .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED))

        );


    }
    @Override
    public void onResume() {
        super.onResume();
        serviceMapView.onResume();
    }

    @Override
    public void onStart() {
        super.onStart();
        serviceMapView.onStart();
    }

    @Override
    public void onStop() {
        super.onStop();
        serviceMapView.onStop();
    }

    @Override
    public void onPause() {
        serviceMapView.onPause();
        super.onPause();
    }

    @Override
    public void onDestroy() {
        serviceMapView.onDestroy();
        super.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        serviceMapView.onLowMemory();
    }

}
