package com.example.alina.todolist.data.db.loaders;

import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;

import com.example.alina.todolist.data.db.ContentProviderValues;
import com.example.alina.todolist.data.db.DataLoadCallback;
import com.example.alina.todolist.data.db.DatabaseSchema;
import com.example.alina.todolist.entities.User;

import java.util.ArrayList;
import java.util.List;


public class UserLoader extends BaseLoader<List<User>> {
    private static final int LOADER_ID = 0;

    public UserLoader(Context context, LoaderManager loaderManager) {
        super(context, loaderManager);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return new CursorLoader(context, ContentProviderValues.getContentUri(DatabaseSchema.User.TABLE_NAME),
                args.getStringArray(KEY_PROJECTION),
                args.getString(KEY_SELECTION),
                args.getStringArray(KEY_SELECTION_ARGS),
                args.getString(KEY_SORT_ORDER));
    }

    @Override
    public void onLoadFinished(Loader loader, Cursor data) {
        List<User> userList = new ArrayList<>();
        if (data != null && data.moveToFirst()){
            do {
                User user = new User();
                user.initByCursor(data);
                userList.add(user);
            } while (data.moveToNext());
        }
        callback.onSuccess(userList);
    }

    @Override
    public void onLoaderReset(Loader loader) {
    }

    @Override
    public void loadData(DataLoadCallback<List<User>> callback, Bundle args) {
        this.callback = callback;
        loaderManager.initLoader(LOADER_ID, args, this);
    }
}
