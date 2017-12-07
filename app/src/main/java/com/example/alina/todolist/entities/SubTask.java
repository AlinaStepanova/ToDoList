package com.example.alina.todolist.entities;

import android.content.ContentValues;
import android.database.Cursor;
import android.os.Parcel;

import com.example.alina.todolist.db.DataBaseContract;
import com.example.alina.todolist.db.DataBaseManager;

/**
 * Created by Alina on 02.11.2017.
 */

public class SubTask extends TaskObject implements DataBaseContract{

    public SubTask() {
        super();
    }

    protected SubTask(Parcel in) {
        super(in);
    }

    @Override
    public void initByCursor(Cursor cursor) {
        this.setDescription(cursor.getString(cursor.getColumnIndex(DataBaseManager.COLUMN_SUBTASK_DESCRIPTION)));
        this.setStatus(TaskStatus.valueOf(cursor.getString(cursor.getColumnIndex(DataBaseManager.COLUMN_SUBTASK_STATUS))));
    }

    @Override
    public ContentValues toContentValues() {
        return null;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest, flags);
    }

    public static final Creator<SubTask> CREATOR = new Creator<SubTask>() {
        @Override
        public SubTask createFromParcel(Parcel source) {
            return new SubTask(source);
        }

        @Override
        public SubTask[] newArray(int size) {
            return new SubTask[0];
        }
    };
}
