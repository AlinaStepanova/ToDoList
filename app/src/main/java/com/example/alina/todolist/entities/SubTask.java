package com.example.alina.todolist.entities;

import android.content.ContentValues;
import android.database.Cursor;
import android.os.Parcel;

import com.example.alina.todolist.data.db.DatabaseContract;
import com.example.alina.todolist.data.db.DatabaseSchema;

public class SubTask extends TaskObject implements DatabaseContract {
    private int id;
    private int parentTaskId;

    public SubTask() {
        super();
    }

    protected SubTask(Parcel in) {
        super(in);
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

    public int getId() {
        return id;
    }

    public int getParentTaskId() {
        return parentTaskId;
    }

    public void setParentTaskId(int parentTaskId) {
        this.parentTaskId = parentTaskId;
    }

    @Override
    public void initByCursor(Cursor cursor) {
        id = cursor.getInt(cursor.getColumnIndex(DatabaseSchema.SubTask.ID));
        parentTaskId = cursor.getInt(cursor.getColumnIndex(DatabaseSchema.SubTask.TASK_ID));
        setDescription(cursor.getString(cursor.getColumnIndex(DatabaseSchema.SubTask.DESC)));
        setStatus(TaskStatus.valueOf(cursor.getString(cursor.getColumnIndex(DatabaseSchema.SubTask.STATUS))));
    }

    @Override
    public ContentValues toContentValues() {
        ContentValues cv = new ContentValues();
        cv.put(DatabaseSchema.SubTask.STATUS, getStatus().name());
        cv.put(DatabaseSchema.SubTask.DESC, getDescription());
        cv.put(DatabaseSchema.SubTask.TASK_ID, parentTaskId);
        return cv;
    }
}
