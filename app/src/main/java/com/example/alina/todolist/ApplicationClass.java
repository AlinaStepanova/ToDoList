package com.example.alina.todolist;

import android.app.Application;
import android.content.Intent;

import com.example.alina.todolist.maps.GPSTracker;

/**
 * Created by Ваня on 14.12.2017.
 */

public class ApplicationClass extends Application{
    @Override
    public void onCreate() {
        super.onCreate();
        startService(new Intent(getApplicationContext(), GPSTracker.class));
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
    }
}
