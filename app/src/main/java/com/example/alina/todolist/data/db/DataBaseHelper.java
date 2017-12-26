package com.example.alina.todolist.data.db;

import android.content.ContentUris;
import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;

import com.example.alina.todolist.data.db.loaders.UserLoader;
import com.example.alina.todolist.entities.User;

import java.util.List;

public class DataBaseHelper {
    private Context context;


    public DataBaseHelper(Context context){
        this.context = context;
    }

    public void getUser(DataLoadCallback<List<User>> callback, LoaderManager manager){
        new UserLoader(context, manager).loadData(callback, new Bundle());
    }

    public void createNewUser(User user){
        context.getContentResolver().insert(ContentProviderValues.getContentUri(DatabaseSchema.User.TABLE_NAME),
                user.toContentValues());
    }

    public User getUserById(int userId){
        User user = new User();
        Cursor query = context.getContentResolver()
                .query(ContentUris.withAppendedId(ContentProviderValues.getContentUri(DatabaseSchema.User.TABLE_NAME) , userId),
                null, null, null, null);
        try {
            if (query != null && query.moveToFirst()){
                do {
                    user.initByCursor(query);
                }while (query.moveToNext());
            }
        } finally {
            if (query != null)
                query.close();
        }
        return user;
    }
}
