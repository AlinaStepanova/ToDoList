package com.example.alina.todolist.data.database;

import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;

import com.example.alina.todolist.entities.Task;
import com.example.alina.todolist.enums.Constants;
import com.example.alina.todolist.enums.LoadersId;

import java.util.ArrayList;

public class LoadTasks implements LoaderManager.LoaderCallbacks<Cursor> {

    private final Context context;

    private final LoaderManager loaderManager;

    private OnEntitiesLoad<Task> onTaskLoad;

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

    public void loadTasks(OnEntitiesLoad<Task> onTaskLoad, Bundle args) {
        this.onTaskLoad = onTaskLoad;
        if(loaderManager.getLoader(LoadersId.TASKS_LOADER_ID.ordinal()) != null) {
            loaderManager.initLoader(LoadersId.TASKS_LOADER_ID.ordinal(), args, this);
        } else {
            loaderManager.restartLoader(LoadersId.TASKS_LOADER_ID.ordinal(), args, this);
        }
    }

}