package com.example.alina.todolist.data.db.loaders;

import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;

import com.example.alina.todolist.data.db.DataLoadCallback;


public abstract class BaseLoader<T> implements LoaderManager.LoaderCallbacks<Cursor> {

    protected final LoaderManager loaderManager;

    protected final Context context;

    protected DataLoadCallback<T> callback;

    protected static final String KEY_PROJECTION = "KEY_PROJECTION";

    protected static final String KEY_SELECTION = "KEY_SELECTION";

    protected static final String KEY_SELECTION_ARGS = "KEY_SELECTION_ARGS";

    protected static final String KEY_SORT_ORDER = "KEY_SORT_ORDER";

    public BaseLoader(final Context context, final LoaderManager loaderManager){
        this.context = context;
        this.loaderManager = loaderManager;
    }

    public abstract void loadData(DataLoadCallback<T> callback, Bundle args);
}
