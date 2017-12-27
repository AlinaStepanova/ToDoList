package com.example.alina.todolist.data;

import android.support.annotation.NonNull;

import com.example.alina.todolist.data.database.OnEntitiesLoad;
import com.example.alina.todolist.entities.Task;

public interface DataSource {
    void loadTaskList(@NonNull OnEntitiesLoad<Task> onTasksLoaded);
    void createTask(@NonNull Task task);
}