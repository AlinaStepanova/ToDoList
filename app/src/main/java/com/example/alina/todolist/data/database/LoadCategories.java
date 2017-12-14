package com.example.alina.todolist.data.database;

import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;

import com.example.alina.todolist.entities.Category;
import com.example.alina.todolist.enums.Constants;

import java.util.ArrayList;

public class LoadCategories implements LoaderManager.LoaderCallbacks<Cursor> {

    private static final int LOADER_ID = 2;

    private final Context context;

    private final LoaderManager loaderManager;

    private OnCategoryLoad onCategoryLoad;

    public LoadCategories(final Context context,
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
            ArrayList<Category> categories = new ArrayList<>();
            if ((data != null) && data.moveToFirst()) {
                do {
                    Category category = new Category();
                    category.initByCursor(data);
                    categories.add(category);
                } while (data.moveToNext());
            }
            onCategoryLoad.onSuccess(categories);
        } finally {
            if (data != null) {
                data.close();
            }
        }
    }

    @Override
    public void onLoaderReset(final Loader<Cursor> loader) {

    }

    public void loadTasks(LoadCategories.OnCategoryLoad onCategoryLoad) {
        this.onCategoryLoad = onCategoryLoad;
        loaderManager.initLoader(LOADER_ID, new Bundle(), this);
    }

    interface OnCategoryLoad {
        void onSuccess(ArrayList<Category> categories);
    }

}