package com.example.alina.todolist.entities;


import android.content.ContentValues;
import android.database.Cursor;
import android.os.Parcel;
import android.os.Parcelable;

import com.example.alina.todolist.data.database.DataBaseContract;
import com.example.alina.todolist.data.database.DataBaseManager;

import java.util.ArrayList;

public class User implements Parcelable, DataBaseContract {
    private int id;
    private String name;
    private String email;
    private String pin;
    private ArrayList<Task> tasks;
    private ArrayList<Category> categories;

    public User() {
        this.name = "";
        this.email = "";
        this.pin = "";
        this.tasks = new ArrayList<>();
        this.categories = new ArrayList<>();
    }

    public User(User user) {
        this.name = user.name;
        this.email = user.email;
        this.pin = user.pin;
        this.tasks = user.tasks;
        this.categories = user.categories;
    }

    public User(String name, String email, String pin, ArrayList<Task> tasks, ArrayList<Category> categories) {
        this.name = name;
        this.email = email;
        this.pin = pin;
        this.tasks = tasks;
        this.categories = categories;
    }

    public User(Parcel parcel) {
        parcel.writeInt(this.id);
        String[] data = new String[3];
        parcel.readStringArray(data);
        this.name = data[0];
        this.email = data[1];
        this.pin = data[2];
        parcel.readTypedList(this.tasks, Task.CREATOR);
        parcel.readTypedList(this.categories, Category.CREATOR);
    }


    public void setName(String name) {
        this.name = name;
    }

    public void setPin(String pin) {
        this.pin = pin;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setTasks(ArrayList<Task> tasks) {
        this.tasks = tasks;
    }

    public void setCategories(ArrayList<Category> categories) {
        this.categories = categories;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getPin() {
        return pin;
    }

    public ArrayList<Task> getTasks() {
        return tasks;
    }

    public ArrayList<Category> getCategories() {
        return categories;
    }

    public static final Creator<User> CREATOR = new Creator<User>() {
        @Override
        public User createFromParcel(Parcel parcel) {
            return new User(parcel);
        }

        @Override
        public User[] newArray(int i) {
            return new User[0];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(this.id);
        parcel.writeStringArray(new String[]{this.name,this.email,this.pin});
        parcel.writeTypedList(this.tasks);
        parcel.writeTypedList(this.categories);
    }

    @Override
    public void initByCursor(Cursor cursor) {
        this.id = cursor.getInt(cursor.getColumnIndex(DataBaseManager.COLUMN_USER_ID_NAME));
        this.name = cursor.getString(cursor.getColumnIndex(DataBaseManager.COLUMN_USER_NAME));
        this.email = cursor.getString(cursor.getColumnIndex(DataBaseManager.COLUMN_USER_EMAIL));
        this.pin = cursor.getString(cursor.getColumnIndex(DataBaseManager.COLUMN_USER_PIN));
        /**
         it`s not finished------------------------------------------------------------------
         tasks
         categories
         */
    }

    @Override
    public ContentValues toContentValues() {
        ContentValues contentValues = new ContentValues();
        contentValues.put(DataBaseManager.COLUMN_USER_ID_NAME,this.id);
        contentValues.put(DataBaseManager.COLUMN_USER_NAME, this.name);
        contentValues.put(DataBaseManager.COLUMN_USER_EMAIL, this.email);
        contentValues.put(DataBaseManager.COLUMN_USER_PIN, this.pin);
        /**
         it`s not finished------------------------------------------------------------------
         tasks
         categories
         */
        return contentValues;
    }

    @Override
    public String toString() {
        return "User{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", email=" + email +

                /*
                 it`s not finished------------------------------------------------------------------
                 tasks
                 categories
                 */

                '}';
    }
}
