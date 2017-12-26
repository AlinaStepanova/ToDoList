package com.example.alina.todolist;


import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.example.alina.todolist.data.db.DataLoadCallback;
import com.example.alina.todolist.data.repository.DatabaseSource;
import com.example.alina.todolist.data.repository.IRepositorySource;
import com.example.alina.todolist.data.repository.RemoteSource;
import com.example.alina.todolist.entities.Task;

import java.util.List;

public class Repository {
    private Context context;
    private IRepositorySource localSource;
    private IRepositorySource remoteSource;

    public Repository(Context context){
        this.context = context;
        localSource = new DatabaseSource(context);
        remoteSource = new RemoteSource();
    }

/*    public IRepositorySource getRepository(){
        if (isNetworkConnected()){
            return remoteSource;
        }else return localSource;
    }*/

    public void getAllTask(DataLoadCallback<List<Task>> callback){
        if (isNetworkConnected()){

        }else {

        }
    }

    public void createNewTask(Task task){
        localSource.createTask(task);
        if (isNetworkConnected()){
            remoteSource.createTask(task);
        }
    }

    public void updateTask(Task task){
        localSource.updateTask(task);
        if (isNetworkConnected())
            remoteSource.updateTask(task);
    }

    private boolean isNetworkConnected(){
        ConnectivityManager cm =
                (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        return activeNetwork.isConnectedOrConnecting();
    }
}
