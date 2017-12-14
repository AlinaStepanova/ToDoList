package com.example.alina.todolist.entities;


import android.content.ContentValues;
import android.database.Cursor;
import android.os.Parcel;

import com.example.alina.todolist.data.database.DataBaseContract;
import com.example.alina.todolist.data.database.DataBaseManager;
import com.example.alina.todolist.validators.Constants;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

public class Task extends TaskObject  {

    private int id;
    private String name;
    private Date expireDate;
    private Category category;
    private List<SubTask> subTasksList;
    private double longitude;
    private double latitude;


    public Task() {
        expireDate = new Date();
        subTasksList = new ArrayList<>();
        id = UUID.randomUUID().hashCode();
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

    public double getLongitude() {
        return longitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
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
        dest.writeString(this.name);
        dest.writeLong(this.expireDate != null ? this.expireDate.getTime() : -1);
        dest.writeParcelable(this.category, flags);
        dest.writeTypedList(this.subTasksList);
        dest.writeDouble(this.latitude);
        dest.writeDouble(this.longitude);
    }

    protected Task(Parcel in) {
        super(in);
        this.id = in.readInt();
        this.name = in.readString();
        long tmpExpireDate = in.readLong();
        this.expireDate = tmpExpireDate == -1 ? null : new Date(tmpExpireDate);
        this.category = in.readParcelable(Category.class.getClassLoader());
        this.subTasksList = in.createTypedArrayList(SubTask.CREATOR);
        this.latitude = in.readDouble();
        this.longitude = in.readDouble();
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
                '}';
    }

    @Override
    public void initByCursor(Cursor cursor) {
        this.id = cursor.getInt(cursor.getColumnIndex(DataBaseManager.COLUMN_TASK_ID_NAME));
        this.name = cursor.getString(cursor.getColumnIndex(DataBaseManager.COLUMN_TASK_NAME));
        this.setDescription(
                cursor.getString(cursor.getColumnIndex(DataBaseManager.COLUMN_TASK_DESCRIPTION)));
        this.setStatus(TaskStatus.valueOf(cursor.getString(cursor.getColumnIndex(DataBaseManager.COLUMN_TASK_STATUS))));
        this.expireDate = new Date(cursor.getLong(cursor.getColumnIndex(DataBaseManager.COLUMN_TASK_EXPIRE_DATE)));

        this.latitude = cursor.getInt(cursor.getColumnIndex(DataBaseManager.COLUMN_TASK_ID_NAME));
        this.longitude = cursor.getInt(cursor.getColumnIndex(DataBaseManager.COLUMN_TASK_ID_NAME));

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
/*чи не буде помилки з cast int->long, long->db(int)*/
        contentValues.put(DataBaseManager.COLUMN_TASK_EXPIRE_DATE, this.expireDate.getTime());
        /**
         it`s not finished------------------------------------------------------------------
         categories
         subtasks
         */
        return contentValues;
    }
}
