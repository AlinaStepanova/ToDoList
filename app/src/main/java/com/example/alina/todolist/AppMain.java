package com.example.alina.todolist;

import android.app.Application;


public class AppMain extends Application {
    private static Repository repository;

    @Override
    public void onCreate() {
        super.onCreate();
        repository = new Repository(this);
    }

    public static Repository getRepository(){
        return repository;
    }
}
