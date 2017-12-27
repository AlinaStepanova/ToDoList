package com.example.alina.todolist.data.database;

import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;

import com.example.alina.todolist.entities.User;
import com.example.alina.todolist.enums.Constants;
import com.example.alina.todolist.enums.LoadersId;

import java.util.ArrayList;


public class LoadUsers implements LoaderManager.LoaderCallbacks<Cursor> {

    private final Context context;

    private final LoaderManager loaderManager;

    private OnEntitiesLoad<User> onUserLoad;

    public LoadUsers(final Context context,
                    final LoaderManager loaderManager) {
        this.context = context;
        this.loaderManager = loaderManager;
    }

    @Override
    public CursorLoader onCreateLoader(final int id, final Bundle args) {
        return new CursorLoader(context, ContentProviderValues.TASKS_CONTENT_URI,
                args.getStringArray(Constants.KEY_PROJECTION), args.getString(Constants.KEY_SELECTION),
                args.getStringArray(Constants.KEY_SELECTION_ARGS), args.getString(Constants.KEY_SORT_ORDER));
    }

    @Override
    public void onLoadFinished(final Loader<Cursor> loader, final Cursor data) {
        ArrayList<User> users = new ArrayList<>();
        if (data != null && data.moveToFirst()) {
            do {
                users.clear();
                User user = new User();
                user.initByCursor(data);
                users.add(user);
            } while (data.moveToNext());
        }
        onUserLoad.onSuccess(users);
    }

    @Override
    public void onLoaderReset(final Loader<Cursor> loader) {

    }

    public void loadUsers(OnEntitiesLoad<User> onUserLoad, Bundle args) {
        this.onUserLoad = onUserLoad;
        if(loaderManager.getLoader(LoadersId.USER_LOADER_ID.ordinal()) != null) {
            loaderManager.initLoader(LoadersId.USER_LOADER_ID.ordinal(), args, this);
        } else {
            loaderManager.restartLoader(LoadersId.USER_LOADER_ID.ordinal(), args, this);
        }
    }

}