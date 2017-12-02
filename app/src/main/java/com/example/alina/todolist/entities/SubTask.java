package com.example.alina.todolist.entities;

import android.os.Parcel;

/**
 * Created by Alina on 02.11.2017.
 */

public class SubTask extends TaskObject {

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
}
