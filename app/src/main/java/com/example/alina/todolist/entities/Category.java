package com.example.alina.todolist.entities;

import android.content.ContentValues;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Parcel;
import android.os.Parcelable;

import com.example.alina.todolist.data.database.DataBaseContract;
import com.example.alina.todolist.data.database.DataBaseManager;

import java.util.Random;

public class Category implements Parcelable, DataBaseContract {

    private String name;
    private int color;
    private int id;
    private int userId;

    public Category(){

    }

    public Category(String name){
        this.name = name;
        Random random = new Random();
        this.color = Color.argb(255, random.nextInt(256), random.nextInt(256),
                random.nextInt(256));
//        this.id = System.nanoTime();
        this.id = random.nextInt(10000);
    }

    protected Category(Parcel in){
        this.name = in.readString();
        this.color = in.readInt();
        this.id = in.readInt();
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
        parcel.writeString(this.name);
        parcel.writeInt(this.color);
        parcel.writeLong(this.id);
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

    public void setId(int id) {
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
    public String toString() {
        return  "Category{" +
                "name='" + name + '\'' +
                ", color=" + color +
                ", id=" + id + " " +
                '}';
    }

    @Override
    public void initByCursor(Cursor cursor) {
        this.id = cursor.getInt(cursor.getColumnIndex(DataBaseManager.COLUMN_CATEGORY_ID_NAME));
        this.name = cursor.getString(cursor.getColumnIndex(DataBaseManager.COLUMN_CATEGORY_NAME));
        this.color = cursor.getInt(cursor.getColumnIndex(DataBaseManager.COLUMN_CATEGORY_COLOR));
        this.userId = cursor.getInt(cursor.getColumnIndex(DataBaseManager.COLUMN_CATEGORY_USER_ID));
    }

    @Override
    public ContentValues toContentValues() {
        ContentValues contentValues = new ContentValues();
//        contentValues.put(DataBaseManager.COLUMN_CATEGORY_ID_NAME, this.id);

        contentValues.put(DataBaseManager.COLUMN_CATEGORY_NAME, this.name);
        contentValues.put(DataBaseManager.COLUMN_CATEGORY_COLOR, this.color);
        return null;
    }
}
