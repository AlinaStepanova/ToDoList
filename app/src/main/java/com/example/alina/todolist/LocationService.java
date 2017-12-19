package com.example.alina.todolist;

import android.*;
import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Service;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.IBinder;
import android.os.Looper;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.example.alina.todolist.enums.BundleKey;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;

public class LocationService extends Service {
    private static final long UPDATE_INTERVAL_IN_MILLISECONDS = 1000;
    private static final long FASTEST_UPDATE_INTERVAL_IN_MILLISECONDS = 1000;

    private FusedLocationProviderClient mFusedLocationClient;
    private LocationCallback locationCallback;
    private LocationRequest locationRequest;
    private LocationSettingsRequest locationSettingsRequest;

    public LocationService() {}

    @Override
    public void onCreate() {
        super.onCreate();
        this.locationRequest = new LocationRequest();
        this.locationRequest.setInterval(UPDATE_INTERVAL_IN_MILLISECONDS);
        this.locationRequest.setFastestInterval(FASTEST_UPDATE_INTERVAL_IN_MILLISECONDS);
        this.locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder();
        builder.addLocationRequest(this.locationRequest);
        this.locationSettingsRequest = builder.build();

        final LocalBroadcastManager broadcastManager = LocalBroadcastManager.getInstance(this);

        this.locationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                super.onLocationResult(locationResult);
                Location currentLocation = locationResult.getLastLocation();
                Intent intent = new Intent(BundleKey.LOCATION.name());
                intent.putExtra(BundleKey.LOCATION.name(), currentLocation);
                broadcastManager.sendBroadcast(intent);
            }
        };

        this.mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        if(ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            this.mFusedLocationClient.requestLocationUpdates(this.locationRequest,
                    this.locationCallback, Looper.myLooper());
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
