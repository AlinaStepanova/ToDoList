package com.example.alina.todolist.data.database;

import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;

import com.example.alina.todolist.entities.Task;
import com.example.alina.todolist.enums.Constants;

import java.util.ArrayList;

public class LoadTasks implements LoaderManager.LoaderCallbacks<Cursor> {

    private static final int LOADER_ID = 1;

    private final Context context;

    private final LoaderManager loaderManager;

    private OnTaskLoad onTaskLoad;

    public LoadTasks(final Context context,
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
        try {
            ArrayList<Task> tasks = new ArrayList<>();
            if ((data != null) && data.moveToFirst()) {
                do {
                    Task task = new Task();
                    task.initByCursor(data);
                    tasks.add(task);
                } while (data.moveToNext());
            }
            onTaskLoad.onSuccess(tasks);
        } finally {
            if (data != null) {
                data.close();
            }
        }
    }

    @Override
    public void onLoaderReset(final Loader<Cursor> loader) {

    }

    interface OnTaskLoad {
        void onSuccess(ArrayList<Task> tasks);
    }

    public void loadTasks(OnTaskLoad onTaskLoad) {
        this.onTaskLoad = onTaskLoad;
        loaderManager.initLoader(LOADER_ID, new Bundle(), this);
    }

}