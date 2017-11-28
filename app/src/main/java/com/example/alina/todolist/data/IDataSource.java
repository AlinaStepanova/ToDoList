package com.example.alina.todolist.data;

import android.support.annotation.IntRange;
import android.support.annotation.NonNull;

import com.example.alina.todolist.entities.Task;

import java.util.ArrayList;

/**
 * Created by Alina on 14.11.2017.
 */

public interface IDataSource {

    ArrayList<Task> getTaskList();

    boolean createTask(@NonNull Task task);

    boolean updateTask(@NonNull Task task, @IntRange(from = 0, to = Integer.MAX_VALUE) int index);

    boolean updateTask(@NonNull Task task);
}
