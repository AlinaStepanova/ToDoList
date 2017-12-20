package com.example.alina.todolist.data;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.AppCompatActivity;

import com.example.alina.todolist.activities.CategoryActivity;
import com.example.alina.todolist.activities.LoginActivity;
import com.example.alina.todolist.activities.MainActivity;
import com.example.alina.todolist.activities.MapsActivity;
import com.example.alina.todolist.listeners.OnDataChangedListener;

/**
 * Created by gromi on 12/19/2017.
 */

public class DataSourceFactory {

    private AppCompatActivity context;
    private OnDataChangedListener onDataChangedListener;

    public DataSourceFactory(AppCompatActivity context, OnDataChangedListener onDataChangedListener){
        this.onDataChangedListener = onDataChangedListener;
        this.context = context;
    }

    public IDataSource createDataSource(){
        if(isNetworkAvailable()){
            return new FirebaseDataSource(onDataChangedListener);
        } else {
            if (context instanceof MainActivity){
                return new DataBaseSourceBuilder(context, onDataChangedListener)
                        .setTaskLoading()
                        .build();
            }
            if (context instanceof CategoryActivity){
                return new DataBaseSourceBuilder(context, onDataChangedListener)
                        .setCategoryLoading()
                        .build();
            }
            if (context instanceof MapsActivity){
                return new DataBaseSourceBuilder(context, onDataChangedListener)
                        .setTaskLoading()
                        .build();
            }
            if (context instanceof LoginActivity){
                return new DataBaseSourceBuilder(context, onDataChangedListener)
                        .setUserLoading()
                        .build();
            }
        }
        return new DataBaseSourceBuilder(context, onDataChangedListener)
                .setUserLoading()
                .setCategoryLoading()
                .setTaskLoading()
                .build();
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }
}
