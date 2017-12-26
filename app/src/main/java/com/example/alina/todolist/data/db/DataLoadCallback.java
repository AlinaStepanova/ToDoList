package com.example.alina.todolist.data.db;


import android.support.annotation.Nullable;

public interface DataLoadCallback<T> {
    void onSuccess(@Nullable T data);
}
