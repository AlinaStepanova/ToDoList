package com.example.alina.todolist.db.loaders;


import android.content.ContentValues;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.content.AsyncTaskLoader;

import com.example.alina.todolist.enums.BundleKey;

/**
 * Created by gromi on 12/11/2017.
 */

public class CudLoader extends AsyncTaskLoader<Void> {

    public static final int LOADER_ID = 99;

    public enum CudType {
        INSERT,
        DELETE,
        UPDATE
    }

    private CudType type;
    private String selection;
    private String[] selectionArgs;
    private Uri uri;
    private ContentValues values;

    public CudLoader(Context context, Bundle args){
        super(context);
        type = CudType.values()[args.getInt(BundleKey.DB_INTERACTION_TYPE.name())];
        selection = args.getString(BundleKey.SELECTION.name());
        selectionArgs = args.getStringArray(BundleKey.SELECTION_ARGS.name());
        uri = args.getParcelable(BundleKey.URI.name());
        values = args.getParcelable(BundleKey.CONTENT_VALUES.name());
        forceLoad();
    };

    @Override
    protected void onStartLoading() {
        super.onStartLoading();
    }

    @Override
    public Void loadInBackground() {
        switch (type){
            case INSERT:
                getContext().getContentResolver().insert(uri, values);
                break;
            case UPDATE:
                getContext().getContentResolver().update(uri, values, selection, selectionArgs);
                break;
            case DELETE:
                getContext().getContentResolver().delete(uri, selection, selectionArgs);
                break;
        }
        return null;
    }
}
