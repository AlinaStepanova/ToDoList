package com.example.alina.todolist.entities;

import android.content.ContentValues;
import android.database.Cursor;
import android.os.Parcel;

import com.example.alina.todolist.data.database.DataBaseManager;

/**
 * Created by Alina on 02.11.2017.
 */

public class SubTask extends TaskObject {

    private int id;
    private int idTask;
    public SubTask() {
        super();
    }

    protected SubTask(Parcel in) {
        super(in);
        this.id = in.readInt();
        this.id = in.readInt();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest, flags);
        dest.writeInt(this.id);
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

    @Override
    public void initByCursor(Cursor cursor) {
        this.setDescription(cursor.getString(cursor.getColumnIndex(DataBaseManager.COLUMN_SUBTASK_DESCRIPTION)));
        this.id = cursor.getInt(cursor.getColumnIndex(DataBaseManager.COLUMN_SUBTASK_ID_NAME));
        this.setStatus(TaskStatus.valueOf(cursor.getString(cursor.getColumnIndex(DataBaseManager.COLUMN_SUBTASK_STATUS))));
    }

    @Override
    public ContentValues toContentValues() {
        ContentValues contentValues = new ContentValues();
        contentValues.put(DataBaseManager.COLUMN_SUBTASK_DESCRIPTION, this.getDescription());
        contentValues.put(DataBaseManager.COLUMN_SUBTASK_ID_NAME, this.id);
        contentValues.put(DataBaseManager.COLUMN_SUBTASK_STATUS, this.getStatus().name());
        return null;
    }
}
