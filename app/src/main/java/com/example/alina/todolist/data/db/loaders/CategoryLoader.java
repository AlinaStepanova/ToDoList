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
import com.example.alina.todolist.entities.Category;

import java.util.ArrayList;
import java.util.List;

public class CategoryLoader extends BaseLoader<List<Category>> {
    private static final int LOADER_ID = 2;

    public CategoryLoader(Context context, LoaderManager loaderManager) {
        super(context, loaderManager);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return new CursorLoader(context,
                ContentProviderValues.getContentUri(DatabaseSchema.Category.TABLE_NAME),
                args.getStringArray(KEY_PROJECTION),
                args.getString(KEY_SELECTION),
                args.getStringArray(KEY_SELECTION_ARGS),
                args.getString(KEY_SORT_ORDER));
    }

    @Override
    public void onLoadFinished(Loader loader, Cursor data) {
        List<Category> categories = new ArrayList<>();
        if (data != null && data.moveToFirst()){
            do {
                Category category = new Category();
                category.initByCursor(data);
                categories.add(category);
            } while (data.moveToNext());
        }
        callback.onSuccess(categories);
    }

    @Override
    public void onLoaderReset(Loader loader) {
    }

    @Override
    public void loadData(DataLoadCallback<List<Category>> callback, Bundle args) {
        this.callback = callback;
        loaderManager.initLoader(LOADER_ID, args, this);
    }
}
