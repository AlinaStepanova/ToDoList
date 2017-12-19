package com.example.alina.todolist.entities;

import android.content.ContentValues;
import android.database.Cursor;
import android.os.Parcel;

import com.example.alina.todolist.db.DataBaseContract;
import com.example.alina.todolist.db.DataBaseManager;

public class SubTask extends TaskObject {

    private int taskId;

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

    public int getTaskId() {
        return taskId;
    }

    public SubTask setTaskId(int taskId) {
        this.taskId = taskId;
        return this;
    }
}
