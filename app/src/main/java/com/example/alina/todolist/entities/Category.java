package com.example.alina.todolist.entities;

import android.content.ContentValues;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Parcel;
import android.os.Parcelable;
import android.provider.ContactsContract;

import com.example.alina.todolist.db.DataBaseContract;
import com.example.alina.todolist.db.DataBaseManager;

import java.util.Random;

/**
 * Created by gromi on 11/22/2017.
 */

public class Category implements Parcelable, DataBaseContract {

    private String name;
    private int color;
    private long id;
    private int userId;

    public Category(String name){
        this.name = name;
        Random random = new Random();
        this.color = Color.argb(255, random.nextInt(256), random.nextInt(256),
                random.nextInt(256));
        this.id = System.currentTimeMillis();
    }

    protected Category(Parcel in){
        this.name = in.readString();
        this.color = in.readInt();
        this.id = in.readLong();
    }

    @Override
    public void initByCursor(Cursor cursor) {
        this.id = cursor.getInt(cursor.getColumnIndex(DataBaseManager.COLUMN_CATEGORY_ID));
        this.color = cursor.getInt(cursor.getColumnIndex(DataBaseManager.COLUMN_CATEGORY_COLOR));
        this.name = cursor.getString(cursor.getColumnIndex(DataBaseManager.COLUMN_CATEGORY_NAME));
    }

    @Override
    public ContentValues toContentValues() {
        ContentValues values = new ContentValues();
        values.put(DataBaseManager.COLUMN_CATEGORY_NAME, name);
        values.put(DataBaseManager.COLUMN_CATEGORY_COLOR, color);
        values.put(DataBaseManager.COLUMN_CATEGORY_USERID, userId);
        return values;
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
}
