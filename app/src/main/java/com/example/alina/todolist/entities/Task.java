package com.example.alina.todolist.entities;


import android.os.Parcel;

import com.example.alina.todolist.validators.Constants;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

/**
 * Created by Alina on 02.11.2017.
 */

public class Task extends TaskObject {

    private String name;

    private Date expireDate;

    private ArrayList<SubTask> subTasksList;
    public Task() {
        expireDate = new Date();
        subTasksList = new ArrayList<>();
    }

    public String getExpireDateString() {
        return Constants.DATE_FORMAT.format(expireDate);
    }

    public String getName() {
        return name;
    }

    public boolean isExpire(Date date) {
        return expireDate.compareTo(date) < 1;
    }

    public boolean isExpire() {
        Calendar calendar = Calendar.getInstance();
        Date date = calendar.getTime();
        return expireDate.compareTo(date) < 1;
    }

    public String getLeftTime() {
        long difference = expireDate.getTime() - System.currentTimeMillis();
        String result = null;
        if (difference <= 0) {
            result = "expired";
        } else {
            long minutes = TimeUnit.MINUTES.convert(difference, TimeUnit.MILLISECONDS);
            long hours = TimeUnit.HOURS.convert(difference, TimeUnit.MILLISECONDS);
            long days = TimeUnit.DAYS.convert(difference, TimeUnit.MILLISECONDS);
            if (days != 0) {
                result = String.format(Locale.getDefault(), "%d, day%s", (days > 1) ? "s" : "");
            } else {
                if (hours != 0) {
                    result = (hours > 1) ? hours + " hours" : "1 hour";
                } else {
                    result = (minutes > 1) ? minutes + " minutes" : "1 minute";
                }
            }
        }
        return result;
    }

    public void setExpireDate(Date expireDate) {
        this.expireDate = expireDate;
    }

    public void setName(String name) {
        this.name = name;
    }

    /*public ArrayList<SubTask> getSubTasks() {
        return subTasks;
    }

    public void setSubTasks(ArrayList<SubTask> subTasks) {
        this.subTasks = subTasks;
    }*/

    @Override
    public boolean isDone() {
        return super.isDone();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest, flags);
        dest.writeString(this.name);
        dest.writeLong(this.expireDate != null ? this.expireDate.getTime() : -1);
        //dest.writeTypedList(this.subTasksList);
    }

    protected Task(Parcel in) {
        super(in);
        this.name = in.readString();
        long tmpExpireDate = in.readLong();
        this.expireDate = tmpExpireDate == -1 ? null : new Date(tmpExpireDate);
        //this.subTasksList = in.createTypedArrayList(SubTask.CREATOR);
    }

    public static final Creator<Task> CREATOR = new Creator<Task>() {
        @Override
        public Task createFromParcel(Parcel source) {
            return new Task(source);
        }

        @Override
        public Task[] newArray(int size) {
            return new Task[0];
        }
    };
}
