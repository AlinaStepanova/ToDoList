package com.example.alina.todolist.entities;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Alina on 02.11.2017.
 */

public abstract class TaskObject implements Parcelable{

    public enum TaskStatus {
        NEW,
        DONE
    }

    private String description;
    private TaskStatus status;

    @Override
    public int describeContents() {return 0;}
    @Override
    public void writeToParcel (Parcel dest, int flags) {
        dest.writeString(this.description);
        dest.writeInt(this.status == null ? -1 : this.status.ordinal());
    }

    protected TaskObject (Parcel in) {
        this.description = in.readString();
        int tmpStatus = in.readInt();
        this.status = tmpStatus == -1 ? null : TaskStatus.values()[tmpStatus];
    }

    public TaskObject() {
        status = TaskStatus.NEW;
    }

    public boolean isDone() {
        return status == TaskStatus.DONE;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public TaskStatus getStatus() {
        return status;
    }

    public void setStatus(TaskStatus status) {
        this.status = status;
    }
}
