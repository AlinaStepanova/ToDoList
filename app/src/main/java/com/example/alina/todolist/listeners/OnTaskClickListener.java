package com.example.alina.todolist.listeners;

import android.view.View;

import com.example.alina.todolist.entities.Task;

public interface OnTaskClickListener {
    void onTaskClick(Task task, View view);
}
