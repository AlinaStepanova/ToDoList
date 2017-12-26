package com.example.alina.todolist.entities;


import android.content.ContentValues;
import android.database.Cursor;
import android.location.Location;
import android.os.Parcel;

import com.example.alina.todolist.data.db.DatabaseContract;
import com.example.alina.todolist.data.db.DatabaseSchema;
import com.example.alina.todolist.enums.TaskImageStatus;
import com.example.alina.todolist.validators.Constants;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.TimeUnit;


public class Task extends TaskObject implements DatabaseContract {
    private int id;
    private int user_id;
    private int category_id;
    private String name;
    private Date expireDate;
    private List<SubTask> subTasksList;
    private Location location;
    private String imageUrl;
    private TaskImageStatus imageDownloadState;

    public Task() {
        expireDate = new Date();
        subTasksList = new ArrayList<>();
        location = new Location("");
        imageDownloadState = TaskImageStatus.NO_SYNCH;
    }

    public int getId() {
        return id;
    }

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public int getCategory_id() {
        return category_id;
    }

    public void setCategory_id(int category_id) {
        this.category_id = category_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getExpireDateString() {
        return Constants.DATE_FORMAT.format(expireDate);
    }

    public void setExpireDate(Date expireDate) {
        this.expireDate = expireDate;
    }

    public List<SubTask> getSubTasks() {
        return subTasksList;
    }

    public void setSubTasks(List<SubTask> subTasks) {
        this.subTasksList = subTasks;
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

    public TaskImageStatus getImageDownloadState(){
        return imageDownloadState;
    }

    public void setImageDownloadState(TaskImageStatus state){
        imageDownloadState = state;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public boolean isExpire() {
        Calendar calendar = Calendar.getInstance();
        Date date = calendar.getTime();
        return expireDate.compareTo(date) < 1;
    }

    public String getLeftTime() {
        long difference = expireDate.getTime() - System.currentTimeMillis();
        String result;
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

    public boolean isAllSubTasksDone() {
        int count = 0;
        for (SubTask subTask : subTasksList) {
            if (subTask.isDone()) {
                ++count;
            }
        }
        return count != 0 && count == getSubTasks().size();
    }

    @Override
    public boolean isDone() {
        return super.isDone();
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
        int tmpStatus = in.readInt();
        this.imageDownloadState = tmpStatus == -1 ? null : TaskImageStatus.values()[tmpStatus];
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
        dest.writeInt(this.imageDownloadState == null ? -1 : this.imageDownloadState.ordinal());
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
    public void initByCursor(Cursor cursor) {
        id = cursor.getInt(cursor.getColumnIndex(DatabaseSchema.Task.ID));
        name = cursor.getString(cursor.getColumnIndex(DatabaseSchema.Task.NAME));
        setStatus(TaskStatus.valueOf(cursor.getString(cursor.getColumnIndex(DatabaseSchema.Task.STATUS))));
        setDescription(cursor.getString(cursor.getColumnIndex(DatabaseSchema.Task.DESC)));
        expireDate.setTime(cursor.getInt(cursor.getColumnIndex(DatabaseSchema.Task.EXP_DATE)));
        location.setLatitude(Double.parseDouble(cursor.getString(cursor.getColumnIndex(DatabaseSchema.Task.LOCATION_LAT))));
        location.setLongitude(Double.parseDouble(cursor.getString(cursor.getColumnIndex(DatabaseSchema.Task.LOCATION_LON))));
        imageUrl = cursor.getString(cursor.getColumnIndex(DatabaseSchema.Task.IMAGE_URL));
        imageDownloadState = TaskImageStatus.valueOf(cursor.getString(cursor.getColumnIndex(DatabaseSchema.Task.IMAGE_STATUS)));
    }

    @Override
    public ContentValues toContentValues() {
        ContentValues cv = new ContentValues();
        cv.put(DatabaseSchema.Task.NAME, name);
        cv.put(DatabaseSchema.Task.STATUS, getStatus().name());
        cv.put(DatabaseSchema.Task.DESC, getDescription());
        cv.put(DatabaseSchema.Task.EXP_DATE, expireDate.getTime());
        cv.put(DatabaseSchema.Task.LOCATION_LAT, location.getLatitude());
        cv.put(DatabaseSchema.Task.LOCATION_LON, location.getLongitude());
        cv.put(DatabaseSchema.Task.CATEGORY_ID, category_id);
        cv.put(DatabaseSchema.Task.USER_ID, user_id);
        cv.put(DatabaseSchema.Task.IMAGE_URL, imageUrl);
        cv.put(DatabaseSchema.Task.IMAGE_STATUS, imageDownloadState.name());
        return cv;
    }
}
