package com.example.alina.todolist.data;

import android.support.v7.app.AppCompatActivity;

import com.example.alina.todolist.listeners.OnDataChangedListener;

import java.util.HashSet;
import java.util.Set;

public class DataBaseSourceBuilder {

    private AppCompatActivity context;
    private OnDataChangedListener onDataChangedListener;

    private Set<LoadingNeeds> loadingNeeds;
    private enum LoadingNeeds{
        USERS,
        TASKS,
        CATEGORIES,
    }

    public DataBaseSourceBuilder(AppCompatActivity context, OnDataChangedListener onDataChangedListener){
        this.loadingNeeds = new HashSet<>();
        this.context = context;
        this.onDataChangedListener = onDataChangedListener;
    }

    public DataBaseSourceBuilder setUserLoading(){
        loadingNeeds.add(LoadingNeeds.USERS);
        return this;
    }

    public DataBaseSourceBuilder setTaskLoading(){
        loadingNeeds.add(LoadingNeeds.TASKS);
        return this;
    }

    public DataBaseSourceBuilder setCategoryLoading(){
        loadingNeeds.add(LoadingNeeds.CATEGORIES);
        return this;
    }

    public DataBaseDataSource build(){
        DataBaseDataSource dataBaseDataSource = new DataBaseDataSource(context, onDataChangedListener);
        for (LoadingNeeds need : loadingNeeds){
            switch (need){
                case TASKS:
                    dataBaseDataSource.loadTasks();
                    break;
                case USERS:
                    dataBaseDataSource.loadUsers();
                    break;
                case CATEGORIES:
                    dataBaseDataSource.loadCategories();
                    break;
                default:
                    break;
            }
        }
        return dataBaseDataSource;
    }
}
