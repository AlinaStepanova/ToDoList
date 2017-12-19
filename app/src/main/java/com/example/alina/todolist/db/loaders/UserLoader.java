package com.example.alina.todolist.db.loaders;

import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;

import com.example.alina.todolist.db.ContentProviderValues;
import com.example.alina.todolist.entities.User;
import com.example.alina.todolist.enums.BundleKey;

import java.util.ArrayList;

public class UserLoader implements LoaderManager.LoaderCallbacks<Cursor> {

    private static final int LOADER_ID = 2;

    private final Context context;

    private final LoaderManager loaderManager;

    private OnEntitiesLoad<User> onUserLoad;

    public UserLoader(final Context context,
                      final LoaderManager loaderManager) {
        this.context = context;
        this.loaderManager = loaderManager;
    }

    @Override
    public CursorLoader onCreateLoader(final int id, final Bundle args) {
        return new CursorLoader(context, ContentProviderValues.USER_CONTENT_URI,
                args.getStringArray(BundleKey.PROJECTION.name()),
                args.getString(BundleKey.SELECTION.name()),
                args.getStringArray(BundleKey.SELECTION_ARGS.name()),
                args.getString(BundleKey.SORT_ORDER.name()));
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
        if(loaderManager.getLoader(LOADER_ID) != null) {
            loaderManager.initLoader(LOADER_ID, args, this);
        } else {
            loaderManager.restartLoader(LOADER_ID, args, this);
        }
    }
}
