package com.example.alina.todolist.db.loaders;

import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;

import com.example.alina.todolist.db.ContentProviderValues;
import com.example.alina.todolist.entities.Task;
import com.example.alina.todolist.enums.BundleKey;

import java.util.ArrayList;

public class TaskLoader implements LoaderManager.LoaderCallbacks<Cursor> {

    private static final int LOADER_ID = 1;

    private final Context context;

    private final LoaderManager loaderManager;

    private OnEntitiesLoad<Task> onTaskLoad;

    public TaskLoader(final Context context,
                     final LoaderManager loaderManager) {
        this.context = context;
        this.loaderManager = loaderManager;
    }

    @Override
    public CursorLoader onCreateLoader(final int id, final Bundle args) {
        return new CursorLoader(context, ContentProviderValues.TASK_CATEGORY_CONTENT_URI,
                args.getStringArray(BundleKey.PROJECTION.name()),
                args.getString(BundleKey.SELECTION.name()),
                args.getStringArray(BundleKey.SELECTION_ARGS.name()),
                args.getString(BundleKey.SORT_ORDER.name()));
    }

    @Override
    public void onLoadFinished(final Loader<Cursor> loader, final Cursor data) {
        ArrayList<Task> tasks = new ArrayList<>();
        if (data != null && data.moveToFirst()) {
            do {
                Task task = new Task();
                task.initByCursor(data);
                tasks.add(task);
            } while (data.moveToNext());
        }
        onTaskLoad.onSuccess(tasks);
    }

    @Override
    public void onLoaderReset(final Loader<Cursor> loader) {

    }

    public void loadTasks(OnEntitiesLoad<Task> onTaskLoad, Bundle args) {
        this.onTaskLoad = onTaskLoad;
        if(loaderManager.getLoader(LOADER_ID) != null) {
            loaderManager.initLoader(LOADER_ID, args, this);
        } else {
            loaderManager.restartLoader(LOADER_ID, args, this);
        }
    }
}
