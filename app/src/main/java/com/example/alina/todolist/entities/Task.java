package com.example.alina.todolist.entities;


import android.location.Location;
import android.os.Parcel;

import com.example.alina.todolist.enums.TaskImageStatus;
import com.example.alina.todolist.validators.Constants;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

/**
 * Created by Alina on 02.11.2017.
 */

public class Task extends TaskObject {

    private int id;

    private String name;

    private Date expireDate;

    private List<SubTask> subTasksList;

    private Location location;

    private String imageUrl;

    private int imageDownloadState;

    public Task() {
        expireDate = new Date();
        subTasksList = new ArrayList<>();
        location = new Location("");
    }

    public List<SubTask> getSubTasks() {
        return subTasksList;
    }

    public void setExpireDate(Date expireDate) {
        this.expireDate = expireDate;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setSubTasks(List<SubTask> subTasks) {
        this.subTasksList = subTasks;
    }

    public boolean isExpire() {
        Calendar calendar = Calendar.getInstance();
        Date date = calendar.getTime();
        return expireDate.compareTo(date) < 1;
    }

    public String getName() {
        return name;
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

    public String getExpireDateString() {
        return Constants.DATE_FORMAT.format(expireDate);
    }

    public boolean isAllSubTasksDone() {
        int count = 0;
        for (SubTask subTask : subTasksList) {
            if (subTask.isDone()) {
                ++count;
            }
        }
        return count != 0 && count == getSubTasks().size();
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public void setImageDownloadState(TaskImageStatus state){
        imageDownloadState = state.ordinal();
    }

    public int getImageDownloadState(){
        return imageDownloadState;
    }

    @Override
    public boolean isDone() {
        return super.isDone();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest, flags);
        dest.writeInt(this.id);
        dest.writeString(this.name);
        dest.writeLong(this.expireDate != null ? this.expireDate.getTime() : -1);
        dest.writeTypedList(this.subTasksList);
        location.writeToParcel(dest, flags);
        dest.writeString(this.imageUrl);
        dest.writeInt(imageDownloadState);
    }

    protected Task(Parcel in) {
        super(in);
        this.id = in.readInt();
        this.name = in.readString();
        long tmpExpireDate = in.readLong();
        this.expireDate = tmpExpireDate == -1 ? null : new Date(tmpExpireDate);
        this.subTasksList = in.createTypedArrayList(SubTask.CREATOR);
        this.location = Location.CREATOR.createFromParcel(in);
        this.imageUrl = in.readString();
        this.imageDownloadState = in.readInt();
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

    @Override
    public String toString() {
        return "Task{" +
                "id=" + id + '\'' +
                "name='" + name + '\'' +
                ", expireDate=" + expireDate +
                ", subTasksList=" + subTasksList + " " + getStatus().toString() + " " +
                '}';
    }
}
