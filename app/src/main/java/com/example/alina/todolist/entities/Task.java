package com.example.alina.todolist.entities;


import android.content.ContentValues;
import android.database.Cursor;
import android.os.Parcel;

import com.example.alina.todolist.db.DataBaseContract;
import com.example.alina.todolist.db.DataBaseManager;
import com.example.alina.todolist.validators.Constants;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.TimeUnit;



public class Task extends TaskObject implements DataBaseContract{

    private String name;

    private Date expireDate;

    private List<SubTask> subTasksList;

    private Category category;

    private LatLng latLng;

    public Task() {
        expireDate = new Date();
        subTasksList = new ArrayList<>();
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
        dest.writeParcelable(this.category, flags);
        dest.writeString(this.name);
        dest.writeLong(this.expireDate != null ? this.expireDate.getTime() : -1);
        dest.writeTypedList(this.subTasksList);
        dest.writeParcelable(this.latLng, flags);
    }

    protected Task(Parcel in) {
        super(in);
        this.category = in.readParcelable(Category.class.getClassLoader());
        this.name = in.readString();
        long tmpExpireDate = in.readLong();
        this.expireDate = tmpExpireDate == -1 ? null : new Date(tmpExpireDate);
        this.subTasksList = in.createTypedArrayList(SubTask.CREATOR);
        this.latLng = in.readParcelable(LatLng.class.getClassLoader());
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
    public void initByCursor(final Cursor cursor) {
        super.initByCursor(cursor);
        this.name = cursor.getString(cursor.getColumnIndex(DataBaseManager.COLUMN_TASK_NAME));
        this.category = new Category();
        this.category.initByCursor(cursor);
        double lat = cursor.getDouble(cursor.getColumnIndex(DataBaseManager.COLUMN_TASK_LATITUDE));
        double lng = cursor.getDouble(cursor.getColumnIndex(DataBaseManager.COLUMN_TASK_LONGITUDE));
        this.latLng = new LatLng(lat, lng);
    }

    @Override
    public ContentValues toContentValues() {
        ContentValues contentValues = super.toContentValues();
        contentValues.put(DataBaseManager.COLUMN_TASK_NAME, name);
        contentValues.put(DataBaseManager.COLUMN_TASK_CATEGORY_ID, category.getId());
        contentValues.put(DataBaseManager.COLUMN_TASK_LATITUDE, latLng.getLatitude());
        contentValues.put(DataBaseManager.COLUMN_TASK_LONGITUDE, latLng.getLongitude());
        return contentValues;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public LatLng getLatLng() {
        return latLng;
    }

    public Task setLatLng(LatLng latLng) {
        this.latLng = latLng;
        return this;
    }

    @Override
    public String toString() {
        return "Task{" +
//                "id=" + id +
//                ", uuid='" + uuid + '\'' +
//                ", name='" + name + '\'' +
//                ", description='" + description + '\'' +
//                ", status='" + status + '\'' +
                '}';
    }
}
