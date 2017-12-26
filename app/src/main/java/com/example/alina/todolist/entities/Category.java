package com.example.alina.todolist.entities;

import android.content.ContentValues;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Parcel;
import android.os.Parcelable;

import com.example.alina.todolist.data.db.DatabaseContract;
import com.example.alina.todolist.data.db.DatabaseSchema;

import java.util.Random;


public class Category implements Parcelable, DatabaseContract {

    private long id;
    private String name;
    private int color;
    private int userId;

    public Category(){

    }

    public Category(String name){
        this.name = name;
        Random random = new Random();
        this.color = Color.argb(255, random.nextInt(256), random.nextInt(256),
                random.nextInt(256));
    }

    protected Category(Parcel in){
        this.id = in.readLong();
        this.name = in.readString();
        this.color = in.readInt();
        this.userId = in.readInt();
    }

    public static final Creator<Category> CREATOR = new Creator<Category>() {
        @Override
        public Category createFromParcel(Parcel in) {
            return new Category(in);
        }

        @Override
        public Category[] newArray(int size) {
            return new Category[0];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeLong(this.id);
        parcel.writeString(this.name);
        parcel.writeInt(this.color);
        parcel.writeInt(this.userId);
    }

    public String getName() {
        return name;
    }

    public int getColor() {
        return color;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    @Override
    public boolean equals(Object obj) {
        boolean result = false;
        if(obj instanceof Category){
            if(((Category) obj).getId() == this.id && ((Category) obj).getColor() == this.color
                    && ((Category) obj).getName().equals(this.name)){
                result = true;
            }
        }
        return result;
    }

    @Override
    public void initByCursor(Cursor cursor) {
        id = cursor.getInt(cursor.getColumnIndex(DatabaseSchema.Category.ID));
        userId = cursor.getInt(cursor.getColumnIndex(DatabaseSchema.Category.USER_ID));
        name = cursor.getString(cursor.getColumnIndex(DatabaseSchema.Category.NAME));
        color = cursor.getInt(cursor.getColumnIndex(DatabaseSchema.Category.COLOR));
    }

    @Override
    public ContentValues toContentValues() {
        ContentValues cv = new ContentValues();
        cv.put(DatabaseSchema.Category.NAME, name);
        cv.put(DatabaseSchema.Category.COLOR, color);
        cv.put(DatabaseSchema.Category.USER_ID, userId);
        return cv;
    }
}
