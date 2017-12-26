package com.example.alina.todolist.data.db;


import android.content.ContentValues;
import android.database.Cursor;

public interface DatabaseContract {
    void initByCursor(Cursor cursor);
    ContentValues toContentValues();
}
