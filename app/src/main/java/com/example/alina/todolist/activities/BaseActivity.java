package com.example.alina.todolist.activities;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.location.Location;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.example.alina.todolist.LocationService;
import com.example.alina.todolist.enums.ActivityRequest;
import com.example.alina.todolist.enums.BundleKey;
import com.example.alina.todolist.validators.Constants;

public abstract class BaseActivity extends AppCompatActivity {

    private long currentTime;
    private boolean needCheckCurrentTime = false;
    private BroadcastReceiver receiver;
    private Location currentLocation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        receiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                if(intent.getExtras() != null) {
                    currentLocation = intent.getExtras().getParcelable(BundleKey.LOCATION.name());
                    Log.i("ACTIVITY", currentLocation.toString());
                }
            }
        };
        IntentFilter intentFilter = new IntentFilter(BundleKey.LOCATION.name());
        LocalBroadcastManager.getInstance(this).registerReceiver(receiver, intentFilter);
        startService(new Intent(this, LocationService.class));
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == ActivityRequest.ON_CHECK_PIN_PRESSED_BACK.ordinal()){
            if(getCallingActivity() != null){
                setResult(ActivityRequest.ON_CHECK_PIN_PRESSED_BACK.ordinal());
            }
            finish();
        }
        Log.d("RESULTS", resultCode+"  BASE");
        switch (ActivityRequest.values()[requestCode]) {
            case CHECK_PASSWORD:
                if (resultCode != Activity.RESULT_OK) {
                    setResult(ActivityRequest.ON_CHECK_PIN_PRESSED_BACK.ordinal());
                    finish();
                }
                break;
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        if(needCheckCurrentTime && System.currentTimeMillis() - currentTime > Constants.TimeForCheckPassword){
            Intent intent = new Intent(this, LoginActivity.class);
            intent.putExtra(BundleKey.NEED_CHECK_PASSWORD.name(), true);
            Log.d("inside", "inside onStart");
            startActivityForResult(intent, ActivityRequest.CHECK_PASSWORD.ordinal());
            needCheckCurrentTime = false;
        } else {
            needCheckCurrentTime = true;
        }
    }


    @Override
    protected void onStop() {
        super.onStop();
        currentTime = System.currentTimeMillis();
    }

    protected void setNeedCheckCurrentTime(boolean needToCheck){
        this.needCheckCurrentTime = needToCheck;
    }

    protected Location getCurrentLocation(){
        return currentLocation;
    }
}
