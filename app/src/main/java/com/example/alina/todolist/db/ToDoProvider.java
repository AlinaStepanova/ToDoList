package com.example.alina.todolist.db;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.text.TextUtils;

public class ToDoProvider extends ContentProvider {

    private DataBaseManager dataBaseManager;

    @Override
    public boolean onCreate() {
        dataBaseManager = new DataBaseManager(getContext());
        return true;
    }

    @Override
    public Cursor query(@NonNull Uri uri, String[] projection, String selection,
                        String[] selectionArgs, String sortOrder) {
        if (TextUtils.isEmpty(sortOrder)) {
            sortOrder = DataBaseManager.TaskTable.COLUMN_TASK_ID;
        }
        switch (ContentProviderValues.uriMatcher.match(uri)) {
            case ContentProviderValues.URI_ALL_TASKS:
                break;
            case ContentProviderValues.URI_SINGLE_TASKS_BY_ID:
                String id = uri.getLastPathSegment();

                selection = addUriIdTableSelection(selection, id);

                break;
            default:
                throw new UnsupportedOperationException("Not yet implemented for " + uri);
        }

        Cursor cursor = dataBaseManager.getWritableDatabase()
                .query(DataBaseManager.TaskTable.TASK_TABLE_NAME, projection, selection, selectionArgs,
                        null, null, sortOrder);
        notifyChange(uri);

        return cursor;
    }

    @Override
    public int delete(@NonNull Uri uri, String selection, String[] selectionArgs) {
        switch (ContentProviderValues.uriMatcher.match(uri)) {
            case ContentProviderValues.URI_ALL_TASKS:
                break;
            case ContentProviderValues.URI_SINGLE_TASKS_BY_ID:
                String id = uri.getLastPathSegment();
                selection = addUriIdTableSelection(selection, id);
                break;
            default:
                throw new UnsupportedOperationException("Not yet implemented for " + uri);

        }
        int deleteItemRow = dataBaseManager.getWritableDatabase()
                .delete(DataBaseManager.TaskTable.TASK_TABLE_NAME, selection,
                        selectionArgs);
        notifyChange(uri);
        return deleteItemRow;
    }

    @Override
    public Uri insert(@NonNull Uri uri, ContentValues values) {
        Uri insertedUri;
        switch (ContentProviderValues.uriMatcher.match(uri)) {
            case ContentProviderValues.URI_ALL_TASKS:
                long insertedRowId = dataBaseManager.getWritableDatabase()
                        .insert(DataBaseManager.TaskTable.TASK_TABLE_NAME, null, values);
                insertedUri = ContentUris
                        .withAppendedId(ContentProviderValues.TASKS_CONTENT_URI, insertedRowId);
                notifyChange(uri);
                break;
            default:
                throw new UnsupportedOperationException("Not yet implemented for " + uri);
        }

        return insertedUri;
    }

    @Override
    public int update(@NonNull Uri uri, ContentValues values, String selection,
                      String[] selectionArgs) {
        switch (ContentProviderValues.uriMatcher.match(uri)) {
            case ContentProviderValues.URI_ALL_TASKS:
                break;
            case ContentProviderValues.URI_SINGLE_TASKS_BY_ID:
                String id = uri.getLastPathSegment();
                selection = addUriIdTableSelection(selection,id);
                break;
            default:
                throw new UnsupportedOperationException("Not yet implemented");
        }
        int updatedRowCount = dataBaseManager.getWritableDatabase()
                .update(DataBaseManager.TaskTable.TASK_TABLE_NAME, values, selection, selectionArgs);
        notifyChange(ContentProviderValues.TASKS_CONTENT_URI);
        return updatedRowCount;
    }

    @Override
    public String getType(@NonNull Uri uri) {
        String type = null;
        switch (ContentProviderValues.uriMatcher.match(uri)) {
            case ContentProviderValues.URI_ALL_TASKS:
                type = ContentProviderValues.TYPE_CONTENT_MANY;
                break;
            case ContentProviderValues.URI_SINGLE_TASKS_BY_ID:
                type = ContentProviderValues.TYPE_CONTENT_SINGLE;
        }
        return type;
    }

    private void notifyChange(Uri uri){
        if (getContext() != null) {
            getContext().getContentResolver().notifyChange(uri, null);
        }
    }

    private String addUriIdTableSelection(String selection, String id){
        if (TextUtils.isEmpty(selection)) {
            selection = String.format("%s = %s", DataBaseManager.TaskTable.COLUMN_TASK_ID, id);
        } else {
            selection = String.format("%s AND %s = %s", selection,
                    DataBaseManager.TaskTable.COLUMN_TASK_ID, id);
        }
        return selection;
    }
}
