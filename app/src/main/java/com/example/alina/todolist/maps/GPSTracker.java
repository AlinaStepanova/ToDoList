package com.example.alina.todolist.maps;

import android.Manifest;
import android.app.Service;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.RequiresPermission;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.example.alina.todolist.enums.BundleKey;
import com.example.alina.todolist.enums.Constants;

import static android.Manifest.permission.ACCESS_COARSE_LOCATION;
import static android.Manifest.permission.ACCESS_FINE_LOCATION;

public class GPSTracker extends Service implements LocationListener {

    private LocationManager locationManager;
    private boolean isGPSEnabled = false;
    private boolean isNetworkEnabled = false;
    private Location currentLocation;
    private static final String GPS_NETWORK_IS_NOT_ENABLED = "GPS and network is not enabled";


    public GPSTracker() {
        super();
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d("Location_", "onCreate");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d("Location_", "onStartCommand");
        if (checkSelfPermission()) {
            Log.d("Location_", "permission denied");
        } else {
            findLocation();

        }
        return super.onStartCommand(intent, flags, startId);
    }

    private boolean checkSelfPermission() {
        return (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED || ActivityCompat
                .checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED);
    }


    private void sendData() {
        Intent intent = new Intent(Constants.ACTION_LOCAL_BROADCAST_LOCATION);
        intent.putExtra(BundleKey.CURRENT_LOCATION.name(), currentLocation);
        LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
    }

    @RequiresPermission(allOf = {ACCESS_FINE_LOCATION, ACCESS_COARSE_LOCATION})
    public void findLocation() {
        if (checkSelfPermission()) {
            Log.d("Location_", "permission denied in Location");
        }
        String provider = null;

        locationManager = (LocationManager) getApplicationContext()
                .getSystemService(LOCATION_SERVICE);

        isGPSEnabled = locationManager
                .isProviderEnabled(LocationManager.GPS_PROVIDER);

        isNetworkEnabled = locationManager
                .isProviderEnabled(LocationManager.NETWORK_PROVIDER);

        if (!isGPSEnabled && !isNetworkEnabled) {
            Log.d("Location_", "gps and net is not enabled");
        } else {
            if (isNetworkEnabled) {
                provider = LocationManager.NETWORK_PROVIDER;
            }
            if (isGPSEnabled) {
                provider = LocationManager.GPS_PROVIDER;
            }
            getLocationFromProvider(provider);
        }
    }

    private void getLocationFromProvider(String provider) {
        if (checkSelfPermission()) {
            Log.d("Location_", "permission denied");
        } else {
            if (provider != null) {
                locationManager.requestLocationUpdates(
                        provider,
                        Constants.MIN_TIME_BW_UPDATES,
                        Constants.MIN_DISTANCE_CHANGE_FOR_UPDATES, this);
                if (locationManager != null) {
                    currentLocation = locationManager
                            .getLastKnownLocation(provider);
                }
            }
        }
    }


    @Override
    public void onLocationChanged(Location location) {
        Log.d("Location_", "currentLocation changed");
        if (checkSelfPermission()) {
            Log.d("Location_", "permission denied");
        } else {
            if (currentLocation != null) {
                Log.d("Location_", currentLocation.distanceTo(location) + " ");
                Log.d("Location_", currentLocation.getLatitude()+" "+currentLocation.getLongitude());
                if (currentLocation.distanceTo(location) > Constants.MIN_DISTANCE_CHANGE_FOR_UPDATES) {
                    findLocation();
                    sendData();
                }
            }
        }
    }

    @Override
    public void onProviderDisabled(String provider) {
    }

    @Override
    public void onProviderEnabled(String provider) {
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
    }

    @Override
    public IBinder onBind(Intent arg0) {
        return null;
    }

}