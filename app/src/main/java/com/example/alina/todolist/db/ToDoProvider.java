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
        Cursor cursor;
        if (TextUtils.isEmpty(sortOrder)) {
            sortOrder = DataBaseManager.COLUMN_TASK_ID;
        }
        switch (ContentProviderValues.uriMatcher.match(uri)) {
            case ContentProviderValues.URI_ALL_TASKS:
                cursor = createCursor(DataBaseManager.TASK_TABLE_NAME, projection, selection, selectionArgs,
                    sortOrder);
                break;
            case ContentProviderValues.URI_SINGLE_TASKS_BY_ID:
                selection = addUriIdSelection(DataBaseManager.COLUMN_TASK_ID, selection, uri.getLastPathSegment());
                cursor = createCursor(DataBaseManager.TASK_TABLE_NAME, projection, selection, selectionArgs,
                        sortOrder);
                break;
            case ContentProviderValues.URI_ALL_CATEGORIES:
                cursor = createCursor(DataBaseManager.CATEGORY_TABLE_NAME, projection, selection, selectionArgs,
                        sortOrder);
                break;
            case ContentProviderValues.URI_SINGLE_CATEGORY_BY_ID:
                selection = addUriIdSelection(DataBaseManager.COLUMN_CATEGORY_ID, selection, uri.getLastPathSegment());
                cursor = createCursor(DataBaseManager.CATEGORY_TABLE_NAME, projection, selection, selectionArgs,
                        sortOrder);
                break;
            case ContentProviderValues.URI_ALL_SUBTASKS:
                cursor = createCursor(DataBaseManager.SUBTASK_TABLE_NAME, projection, selection, selectionArgs,
                        sortOrder);
                break;
            case ContentProviderValues.URI_SINGLE_SUBTASK_BY_ID:
                selection = addUriIdSelection(DataBaseManager.COLUMN_SUBTASK_ID, selection, uri.getLastPathSegment());
                cursor = createCursor(DataBaseManager.SUBTASK_TABLE_NAME, projection, selection, selectionArgs,
                        sortOrder);
                break;
            case ContentProviderValues.URI_ALL_USERS:
                cursor = createCursor(DataBaseManager.USER_TABLE_NAME, projection, selection, selectionArgs,
                        sortOrder);
                break;
            case ContentProviderValues.URI_SINGLE_USER_BY_ID:
                selection = addUriIdSelection(DataBaseManager.COLUMN_USER_ID, selection, uri.getLastPathSegment());
                cursor = createCursor(DataBaseManager.USER_TABLE_NAME, projection, selection, selectionArgs,
                        sortOrder);
                break;

            default:
                throw new UnsupportedOperationException("Not yet implemented for " + uri);
        }
        notifyChange(uri);

        return cursor;
    }

    @Override
    public int delete(@NonNull Uri uri, String selection, String[] selectionArgs) {
        int deleteItemRowCount;
        switch (ContentProviderValues.uriMatcher.match(uri)) {
            case ContentProviderValues.URI_ALL_TASKS:
                deleteItemRowCount = deleteRows(DataBaseManager.TASK_TABLE_NAME, selection, selectionArgs);
                break;
            case ContentProviderValues.URI_SINGLE_TASKS_BY_ID:
                selection = addUriIdSelection(DataBaseManager.COLUMN_TASK_ID, selection, uri.getLastPathSegment());
                deleteItemRowCount = deleteRows(DataBaseManager.TASK_TABLE_NAME, selection, selectionArgs);
                break;
            case ContentProviderValues.URI_ALL_CATEGORIES:
                deleteItemRowCount = deleteRows(DataBaseManager.CATEGORY_TABLE_NAME, selection, selectionArgs);
                break;
            case ContentProviderValues.URI_SINGLE_CATEGORY_BY_ID:
                selection = addUriIdSelection(DataBaseManager.COLUMN_CATEGORY_ID, selection, uri.getLastPathSegment());
                deleteItemRowCount = deleteRows(DataBaseManager.CATEGORY_TABLE_NAME, selection, selectionArgs);
                break;
            case ContentProviderValues.URI_ALL_SUBTASKS:
                deleteItemRowCount = deleteRows(DataBaseManager.SUBTASK_TABLE_NAME, selection, selectionArgs);
                break;
            case ContentProviderValues.URI_SINGLE_SUBTASK_BY_ID:
                selection = addUriIdSelection(DataBaseManager.COLUMN_SUBTASK_ID, selection, uri.getLastPathSegment());
                deleteItemRowCount = deleteRows(DataBaseManager.SUBTASK_TABLE_NAME, selection, selectionArgs);
                break;
            case ContentProviderValues.URI_ALL_USERS:
                deleteItemRowCount = deleteRows(DataBaseManager.USER_TABLE_NAME, selection, selectionArgs);
                break;
            case ContentProviderValues.URI_SINGLE_USER_BY_ID:
                selection = addUriIdSelection(DataBaseManager.COLUMN_USER_ID, selection, uri.getLastPathSegment());
                deleteItemRowCount = deleteRows(DataBaseManager.USER_TABLE_NAME, selection, selectionArgs);
                break;
            default:
                throw new UnsupportedOperationException("Not yet implemented for " + uri);

        }
        notifyChange(uri);
        return deleteItemRowCount;
    }

    @Override
    public Uri insert(@NonNull Uri uri, ContentValues values) {
        Uri insertedUri;
        switch (ContentProviderValues.uriMatcher.match(uri)) {
            case ContentProviderValues.URI_ALL_TASKS:
                insertedUri = insertRow(DataBaseManager.TASK_TABLE_NAME, values,
                        ContentProviderValues.TASKS_CONTENT_URI);
                break;
            case ContentProviderValues.URI_ALL_CATEGORIES:
                insertedUri = insertRow(DataBaseManager.CATEGORY_TABLE_NAME, values,
                        ContentProviderValues.CATEGORY_CONTENT_URI);
                break;
            case ContentProviderValues.URI_ALL_SUBTASKS:
                insertedUri = insertRow(DataBaseManager.SUBTASK_TABLE_NAME, values,
                        ContentProviderValues.SUBTASK_CONTENT_URI);
                break;
            case ContentProviderValues.URI_ALL_USERS:
                insertedUri = insertRow(DataBaseManager.USER_TABLE_NAME, values,
                        ContentProviderValues.USER_CONTENT_URI);
                break;
            default:
                throw new UnsupportedOperationException("Not yet implemented for " + uri);
        }
        notifyChange(insertedUri);
        return insertedUri;
    }

    @Override
    public int update(@NonNull Uri uri, ContentValues values, String selection,
                      String[] selectionArgs) {
        int updatedRowCount;
        switch (ContentProviderValues.uriMatcher.match(uri)) {
            case ContentProviderValues.URI_ALL_TASKS:
                updatedRowCount = updateTable(DataBaseManager.TASK_TABLE_NAME, values, selection,
                        selectionArgs);
                break;
            case ContentProviderValues.URI_SINGLE_TASKS_BY_ID:
                selection = addUriIdSelection(DataBaseManager.COLUMN_TASK_ID, selection,uri.getLastPathSegment());
                updatedRowCount = updateTable(DataBaseManager.TASK_TABLE_NAME, values, selection,
                        selectionArgs);
                break;
            case ContentProviderValues.URI_ALL_CATEGORIES:
                updatedRowCount = updateTable(DataBaseManager.CATEGORY_TABLE_NAME, values, selection,
                        selectionArgs);
                break;
            case ContentProviderValues.URI_SINGLE_CATEGORY_BY_ID:
                selection = addUriIdSelection(DataBaseManager.COLUMN_CATEGORY_ID, selection,uri.getLastPathSegment());
                updatedRowCount = updateTable(DataBaseManager.CATEGORY_TABLE_NAME, values, selection,
                        selectionArgs);
                break;
            case ContentProviderValues.URI_ALL_SUBTASKS:
                updatedRowCount = updateTable(DataBaseManager.SUBTASK_TABLE_NAME, values, selection,
                        selectionArgs);
                break;
            case ContentProviderValues.URI_SINGLE_SUBTASK_BY_ID:
                selection = addUriIdSelection(DataBaseManager.COLUMN_SUBTASK_ID, selection, uri.getLastPathSegment());
                updatedRowCount = updateTable(DataBaseManager.SUBTASK_TABLE_NAME, values, selection,
                        selectionArgs);
                break;
            case ContentProviderValues.URI_ALL_USERS:
                updatedRowCount = updateTable(DataBaseManager.USER_TABLE_NAME, values, selection,
                        selectionArgs);
                break;
            case ContentProviderValues.URI_SINGLE_USER_BY_ID:
                String id = uri.getLastPathSegment();
                selection = addUriIdSelection(DataBaseManager.COLUMN_USER_ID, selection,id);
                updatedRowCount = updateTable(DataBaseManager.USER_TABLE_NAME, values, selection,
                        selectionArgs);
                break;
            default:
                throw new UnsupportedOperationException("Not yet implemented");
        }
        notifyChange(uri);
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
                break;
            case ContentProviderValues.URI_ALL_CATEGORIES:
                type = ContentProviderValues.TYPE_CONTENT_MANY;
                break;
            case ContentProviderValues.URI_SINGLE_CATEGORY_BY_ID:
                type = ContentProviderValues.TYPE_CONTENT_SINGLE;
                break;
            case ContentProviderValues.URI_ALL_SUBTASKS:
                type = ContentProviderValues.TYPE_CONTENT_MANY;
                break;
            case ContentProviderValues.URI_SINGLE_SUBTASK_BY_ID:
                type = ContentProviderValues.TYPE_CONTENT_SINGLE;
                break;
            case ContentProviderValues.URI_ALL_USERS:
                type = ContentProviderValues.TYPE_CONTENT_MANY;
                break;
            case ContentProviderValues.URI_SINGLE_USER_BY_ID:
                type = ContentProviderValues.TYPE_CONTENT_SINGLE;
                break;
        }
        return type;
    }

    private void notifyChange(Uri uri){
        if (getContext() != null) {
            getContext().getContentResolver().notifyChange(uri, null);
        }
    }

    private String addUriIdSelection(String column, String selection, String id){
        if (TextUtils.isEmpty(selection)) {
            selection = String.format("%s = %s", column, id);
        } else {
            selection = String.format("%s AND %s = %s", selection,
                    column, id);
        }
        return selection;
    }

    private Cursor createCursor(String table, String[] projection, String selection,
                                String[] selectionArgs, String sortOrder){
        return dataBaseManager.getWritableDatabase()
                .query(table, projection, selection, selectionArgs,
                        null, null, sortOrder);
    }

    private int deleteRows(String table, String selection, String[] selectionArgs){
        return dataBaseManager.getWritableDatabase().delete(table, selection, selectionArgs);
    }

    private Uri insertRow(String table, ContentValues values, Uri contentUri){
        long insertedRowId = dataBaseManager.getWritableDatabase()
                .insert(table, null, values);
        return ContentUris.withAppendedId(contentUri, insertedRowId);
    }

    private int updateTable(String table, ContentValues values, String selection, String[] selectionArgs){
        return dataBaseManager.getWritableDatabase()
                .update(table, values, selection, selectionArgs);
    }
}
