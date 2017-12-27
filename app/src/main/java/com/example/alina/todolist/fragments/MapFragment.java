package com.example.alina.todolist.fragments;


import android.content.Context;
import android.location.Location;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.alina.todolist.R;
import com.example.alina.todolist.entities.Task;
import com.example.alina.todolist.enums.BundleKey;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;


public class MapFragment extends Fragment implements OnMapReadyCallback {

    private GoogleMap googleMap;
    private MapView mapView;
    private OnCloseMapFragment onCloseMapFragment;
    private Task task;


    public MapFragment() {
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        onCloseMapFragment = (OnCloseMapFragment) context;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_map, container, false);
        mapView = view.findViewById(R.id.mapView);
        mapView.onCreate(savedInstanceState);
        mapView.onResume();
        Bundle bundle = this.getArguments();

        if(bundle != null){
            task = bundle.getParcelable(BundleKey.TASK.name());

        }

        Log.d("in_bundle", "bundle in map: " + task.getLatitude()+ " "+ task.getLongitude());
        initMap();
        return view;
    }

    private void initMap() {
        mapView.getMapAsync(this);

    }

    @Override
    public void onMapReady(GoogleMap gMap) {
        googleMap = gMap;

        googleMap.addMarker(new MarkerOptions().position(new LatLng(task.getLatitude(), task.getLongitude())).title(task.getDescription()));
    }

    @Override
    public void onStop() {
        super.onStop();
        onCloseMapFragment.onClose();
    }

    public interface OnCloseMapFragment {
        void onClose();
    }
}
