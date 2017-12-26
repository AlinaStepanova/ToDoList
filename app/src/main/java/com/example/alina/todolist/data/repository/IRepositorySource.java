package com.example.alina.todolist.data.repository;


import android.support.v4.app.LoaderManager;

import com.example.alina.todolist.data.db.DataLoadCallback;
import com.example.alina.todolist.entities.Task;

import java.util.List;

public interface IRepositorySource {
    void getAllTask(DataLoadCallback<List<Task>> callback, LoaderManager manager);
    void createTask(Task task);
    void updateTask(Task task);
}
