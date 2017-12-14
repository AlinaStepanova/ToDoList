package com.example.alina.todolist.data.database;

import android.content.ContentValues;
import android.database.Cursor;

public interface DataBaseContract {

    void initByCursor(Cursor cursor);

    ContentValues toContentValues();

}