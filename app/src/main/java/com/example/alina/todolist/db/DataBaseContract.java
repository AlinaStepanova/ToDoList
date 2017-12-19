package com.example.alina.todolist.db;

import android.content.ContentValues;
import android.database.Cursor;

/**
 * Created by Student_3 on 05/12/2017.
 */

public interface DataBaseContract {
    void initByCursor(Cursor cursor);

    ContentValues toContentValues();
}
