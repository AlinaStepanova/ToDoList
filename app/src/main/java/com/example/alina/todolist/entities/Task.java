package com.example.alina.todolist.entities;


import android.content.ContentValues;
import android.database.Cursor;
import android.os.Parcel;

import com.example.alina.todolist.data.database.DataBaseManager;
import com.example.alina.todolist.enums.Constants;
import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

public class Task extends TaskObject  {

    private int id;
    private int userId;
    private int categoryId;
    private String name;
    private Date expireDate;
    private Category category;
    private List<SubTask> subTasksList;
    private double latitude;
    private double longitude;
    private String photoPath;

    public Task() {
        expireDate = new Date();
        subTasksList = new ArrayList<>();
        id = UUID.randomUUID().hashCode();
    }

    public int getId() {
        return id;
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

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLocation(LatLng location) {
        latitude = location.latitude;
        longitude = location.longitude;
    }

    public void setLocation (double lat, double lon){
        latitude = lat;
        longitude = lon;
    }


    public void setPhotoPath(String photoPath) {
        this.photoPath = photoPath;
    }

    public String getPhotoPath() {
        return photoPath;
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
        String result = "";
        if (difference <= 0) {
            result = "expired";
        } else {
            long minutes = TimeUnit.MINUTES.convert(difference, TimeUnit.MILLISECONDS);
            long hours = TimeUnit.HOURS.convert(difference, TimeUnit.MILLISECONDS);
            long days = TimeUnit.DAYS.convert(difference, TimeUnit.MILLISECONDS);
            if (days != 0) {
                result = String.format(Locale.getDefault(), "%d, day%s", days, (days > 1) ? "s" : "");
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

    @Override
    public boolean isDone() {
        return super.isDone();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest, flags);
        dest.writeInt(this.id);
        dest.writeInt(this.userId);
        dest.writeInt(this.categoryId);
        dest.writeString(this.name);
        dest.writeLong(this.expireDate != null ? this.expireDate.getTime() : -1);
        dest.writeDouble(this.latitude);
        dest.writeDouble(this.longitude);

        dest.writeString(this.photoPath);

        dest.writeParcelable(this.category, flags);
        dest.writeTypedList(this.subTasksList);
    }

    protected Task(Parcel in) {
        super(in);
        this.id = in.readInt();
        this.userId = in.readInt();
        this.categoryId = in.readInt();
        this.name = in.readString();
        long tmpExpireDate = in.readLong();
        this.expireDate = tmpExpireDate == -1 ? null : new Date(tmpExpireDate);
        this.latitude = in.readDouble();
        this.longitude = in.readDouble();

        this.photoPath = in.readString();

        this.category = in.readParcelable(Category.class.getClassLoader());
        this.subTasksList = in.createTypedArrayList(SubTask.CREATOR);

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
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", expireDate=" + expireDate +
                ", '" + category.toString() + '\'' +
                ", subTasksList=" + subTasksList + " " + getStatus().toString() + " " +
                "lat='" + latitude + '\'' +
                "lon='" + longitude + '\'' +
                '}';
    }

    @Override
    public void initByCursor(Cursor cursor) {
        this.id = cursor.getInt(cursor.getColumnIndex(DataBaseManager.COLUMN_TASK_ID_NAME));
        this.userId = cursor.getInt(cursor.getColumnIndex(DataBaseManager.COLUMN_TASK_USER_ID));
        this.categoryId = cursor.getInt(cursor.getColumnIndex(DataBaseManager.COLUMN_TASK_CATEGORY_ID));
        this.name = cursor.getString(cursor.getColumnIndex(DataBaseManager.COLUMN_TASK_NAME));
        this.setDescription(
                cursor.getString(cursor.getColumnIndex(DataBaseManager.COLUMN_TASK_DESCRIPTION)));
        this.setStatus(TaskStatus.valueOf(cursor.getString(cursor.getColumnIndex(DataBaseManager.COLUMN_TASK_STATUS))));
        this.expireDate = new Date(cursor.getLong(cursor.getColumnIndex(DataBaseManager.COLUMN_TASK_EXPIRE_DATE)));

        this.latitude = cursor.getDouble(cursor.getColumnIndex(DataBaseManager.COLUMN_TASK_LATITUDE));
        this.longitude = cursor.getDouble(cursor.getColumnIndex(DataBaseManager.COLUMN_TASK_LONGITUDE));
        this.setUuid(cursor.getString(cursor.getColumnIndex(DataBaseManager.COLUMN_TASK_UUID)));

        /**
          it`s not finished------------------------------------------------------------------
         categories
         subtasks
         */
    }

    @Override
    public ContentValues toContentValues() {
        ContentValues contentValues = new ContentValues();
        contentValues.put(DataBaseManager.COLUMN_TASK_NAME, this.name);
        contentValues.put(DataBaseManager.COLUMN_TASK_DESCRIPTION, this.getDescription());
        contentValues.put(DataBaseManager.COLUMN_TASK_STATUS, this.getStatus().name());
        contentValues.put(DataBaseManager.COLUMN_TASK_EXPIRE_DATE, this.expireDate.getTime());
        contentValues.put(DataBaseManager.COLUMN_TASK_LATITUDE, this.latitude);
        contentValues.put(DataBaseManager.COLUMN_TASK_LONGITUDE, this.longitude);
        contentValues.put(DataBaseManager.COLUMN_TASK_UUID, this.getUuid());


        /**
         it`s not finished------------------------------------------------------------------
         categories
         subtasks
         */
        return contentValues;
    }
}
