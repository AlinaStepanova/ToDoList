package com.example.alina.todolist.db.loaders;

import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;

import com.example.alina.todolist.db.ContentProviderValues;
import com.example.alina.todolist.entities.Task;

import java.util.ArrayList;

public class TaskLoader implements LoaderManager.LoaderCallbacks<Cursor> {

    private static final int LOADER_ID = 1;

    private final Context context;

    private final LoaderManager loaderManager;

    private static final String KEY_PROJECTION = "KEY_PROJECTION";

    private static final String KEY_SELECTION = "KEY_SELECTION";

    private static final String KEY_SELECTION_ARGS = "KEY_SELECTION_ARGS";

    private static final String KEY_SORT_ORDER = "KEY_SORT_ORDER";

    private OnTaskLoad onTaskLoad;

    public TaskLoader(final Context context,
                     final LoaderManager loaderManager) {
        this.context = context;
        this.loaderManager = loaderManager;
    }

    @Override
    public CursorLoader onCreateLoader(final int id, final Bundle args) {
        return new CursorLoader(context, ContentProviderValues.TASKS_CONTENT_URI,
                args.getStringArray(KEY_PROJECTION), args.getString(KEY_SELECTION),
                args.getStringArray(KEY_SELECTION_ARGS), args.getString(KEY_SORT_ORDER));
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

    public interface OnTaskLoad {
        void onSuccess(ArrayList<Task> tasks);
    }

    public void loadTasks(OnTaskLoad onTaskLoad) {
        this.onTaskLoad = onTaskLoad;
        loaderManager.initLoader(LOADER_ID, new Bundle(), this);
    }
}
