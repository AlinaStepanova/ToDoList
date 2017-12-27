package com.example.alina.todolist.activities;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.location.Location;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.example.alina.todolist.enums.BundleKey;
import com.example.alina.todolist.enums.Constants;

public abstract class BaseLocationActivity extends BaseTimerActivity {

    private Location location;
    private BroadcastReceiver listener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void onStart() {
        super.onStart();
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

                location = intent.getParcelableExtra(BundleKey.CURRENT_LOCATION.name());
                Log.d("LBR", location.getLatitude() + " " + location.getLongitude());
            }
        };
        LocalBroadcastManager.getInstance(this).registerReceiver(listener,
                new IntentFilter(Constants.ACTION_LOCAL_BROADCAST_LOCATION));
    }

    @Override
    protected void onStop() {
        super.onStop();
        LocalBroadcastManager.getInstance(this).unregisterReceiver(listener);
    }

    //protected abstract View getActivityContentViewId();
}
