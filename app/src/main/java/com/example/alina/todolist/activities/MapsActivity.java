package com.example.alina.todolist.activities;

import android.os.Bundle;

import com.example.alina.todolist.R;
import com.example.alina.todolist.data.DataSourceFactory;
import com.example.alina.todolist.data.IDataSource;
import com.example.alina.todolist.entities.Task;
import com.example.alina.todolist.listeners.OnDataChangedListener;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class MapsActivity extends BaseActivity implements OnMapReadyCallback, OnDataChangedListener {

    private GoogleMap map;
    private IDataSource dataSource;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        map = googleMap;
        DataSourceFactory factory = new DataSourceFactory(this, this);
        dataSource = factory.createDataSource();
        addMarkers();
    }

    @Override
    public void notifyDataChanged() {
        map.clear();
        addMarkers();
    }

    private void addMarkers(){
        for (Task task : dataSource.getTaskList()){
            map.addMarker(new MarkerOptions()
                    .position(new LatLng(task.getLatLng().getLatitude(), task.getLatLng().getLongitude()))
                    .title(task.getName())
                    .snippet(task.getDescription()));
        }
    }
}
