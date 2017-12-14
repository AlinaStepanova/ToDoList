package com.example.alina.todolist;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.example.alina.todolist.enums.Constants;
import com.example.alina.todolist.maps.GPSTracker;

public abstract class BaseLocationActivity extends AppCompatActivity {

    private double lat;
    private double lon;
    private BroadcastReceiver listener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        startService(new Intent(this, GPSTracker.class));
    }

    @Override
    protected void onResume() {
        super.onResume();
        onReceive();
    }

    private void onReceive() {
        listener = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                lat = intent.getDoubleExtra(Constants.LATITUDE, 0);
                lon = intent.getDoubleExtra(Constants.LONGITUDE, 0);
                Log.d("LBR", lat+" "+lon);
            }
        };
        LocalBroadcastManager.getInstance(this).registerReceiver(listener,
                new IntentFilter(Constants.LOCAL_BROADCAST_LOCATION));
    }

    @Override
    protected void onStop() {
        super.onStop();
        LocalBroadcastManager.getInstance(this).unregisterReceiver(listener);
    }
}
