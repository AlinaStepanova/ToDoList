package com.example.alina.todolist.data;


public interface DataCallback<T> {
    void onSuccess(T data);
    void onError(String massage);
}
