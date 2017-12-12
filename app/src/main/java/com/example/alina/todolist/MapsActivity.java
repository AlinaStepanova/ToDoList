package com.example.alina.todolist;

import android.location.Location;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;

import com.example.alina.todolist.enums.BundleKey;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap map;
    private Location taskLocation;
    private String taskName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        if (getIntent().getExtras() != null){
            taskName = getIntent().getStringExtra(BundleKey.TASK_NAME.name());
            taskLocation = getIntent().getParcelableExtra(BundleKey.TASK_LOCATION.name());
        }

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        map = googleMap;
        LatLng position = new LatLng(taskLocation.getLatitude(), taskLocation.getLongitude());
        map.addMarker(new MarkerOptions().position(position).title(taskName));
        map.moveCamera(CameraUpdateFactory.newLatLng(position));
    }
}
