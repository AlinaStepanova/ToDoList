package com.example.alina.todolist.data.database;

import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;

import com.example.alina.todolist.entities.SubTask;
import com.example.alina.todolist.enums.Constants;
import com.example.alina.todolist.enums.LoadersId;

import java.util.ArrayList;


public class LoadSubTasks implements LoaderManager.LoaderCallbacks<Cursor>{

    private final Context context;

    private final LoaderManager loaderManager;

    private OnEntitiesLoad<SubTask> onSubTaskLoad;

    public LoadSubTasks(final Context context,
                     final LoaderManager loaderManager) {
        this.context = context;
        this.loaderManager = loaderManager;
    }

    @Override
    public CursorLoader onCreateLoader(final int id, final Bundle args) {
        return new CursorLoader(context, ContentProviderValues.TASKS_CONTENT_URI,
                args.getStringArray(Constants.KEY_PROJECTION),
                args.getString(Constants.KEY_SELECTION),
                args.getStringArray(Constants.KEY_SELECTION_ARGS), args.getString(Constants.KEY_SORT_ORDER));
    }

    @Override
    public void onLoadFinished(final Loader<Cursor> loader, final Cursor data) {
        try {
            ArrayList<SubTask> subTasks = new ArrayList<>();
            if ((data != null) && data.moveToFirst()) {
                do {
                    SubTask subTask = new SubTask();
                    subTask.initByCursor(data);
                    subTasks.add(subTask);
                } while (data.moveToNext());
            }
            onSubTaskLoad.onSuccess(subTasks);
        } finally {
            if (data != null) {
                data.close();
            }
        }
    }

    @Override
    public void onLoaderReset(final Loader<Cursor> loader) {

    }

    public void loadSubtasks(OnEntitiesLoad<SubTask> onSubTaskLoad, Bundle args) {
        this.onSubTaskLoad = onSubTaskLoad;
        if(loaderManager.getLoader(LoadersId.SUBTASKS_LOADER_ID.ordinal()) != null) {
            loaderManager.initLoader(LoadersId.SUBTASKS_LOADER_ID.ordinal(), args, this);
        } else {
            loaderManager.restartLoader(LoadersId.SUBTASKS_LOADER_ID.ordinal(), args, this);
        }
    }

}