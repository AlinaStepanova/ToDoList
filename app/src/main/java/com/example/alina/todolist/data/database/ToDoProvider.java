package com.example.alina.todolist.data.database;

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
            sortOrder = DataBaseManager.COLUMN_TASK_ID_NAME;
        }

        String tableName;
        Uri contentProviderValue = null;
        switch (ContentProviderValues.uriMatcher.match(uri)) {
            case ContentProviderValues.URI_ALL_TASKS:
                tableName = DataBaseManager.TASK_TABLE_NAME;
                break;
            case ContentProviderValues.URI_SINGLE_TASKS_BY_ID:
                selection = getTaskSelectionById(uri, selection, DataBaseManager.COLUMN_TASK_ID_NAME);
                tableName = DataBaseManager.TASK_TABLE_NAME;
                contentProviderValue = ContentProviderValues.TASKS_CONTENT_URI;
                break;

            case ContentProviderValues.URI_ALL_CATEGORIES:
                tableName = DataBaseManager.CATEGORY_TABLE_NAME;
                break;
            case ContentProviderValues.URI_SINGLE_CATEGORIES_BY_ID:
                selection = getTaskSelectionById(uri,selection,DataBaseManager.COLUMN_CATEGORY_ID_NAME);
                tableName = DataBaseManager.CATEGORY_TABLE_NAME;
                contentProviderValue = ContentProviderValues.CATEGORIES_CONTENT_URI;
                break;

            case ContentProviderValues.URI_ALL_SUBTASKS:
                tableName = DataBaseManager.SUBTASK_TABLE_NAME;
                break;
            case ContentProviderValues.URI_SINGLE_SUBTASKS_BY_ID:
                selection = getTaskSelectionById(uri,selection,DataBaseManager.COLUMN_SUBTASK_ID_NAME);
                tableName = DataBaseManager.SUBTASK_TABLE_NAME;
                contentProviderValue = ContentProviderValues.SUBTASKS_CONTENT_URI;
                break;

            case ContentProviderValues.URI_ALL_USERS:
                tableName = DataBaseManager.USER_TABLE_NAME;
                break;
            case ContentProviderValues.URI_SINGLE_USERS_BY_ID:
                selection = getTaskSelectionById(uri,selection,DataBaseManager.COLUMN_USER_ID_NAME);
                tableName = DataBaseManager.USER_TABLE_NAME;
                contentProviderValue = ContentProviderValues.USERS_CONTENT_URI;
                break;
            default:
                throw new UnsupportedOperationException("Not yet implemented for " + uri);
        }

        Cursor cursor = dataBaseManager.getWritableDatabase()
                .query(tableName, projection, selection, selectionArgs,
                        null, null, sortOrder);

        if (getContext() != null) {
            cursor.setNotificationUri(getContext().getContentResolver(),
                    contentProviderValue);
        }

        return cursor;
    }

    private String getTaskSelectionById(Uri uri, String selection, String dataBaseManagerID) {
        String id = uri.getLastPathSegment();

        if (TextUtils.isEmpty(selection)) {
            selection = String.format("%s = %s", dataBaseManagerID, id);
        } else {
            selection = String.format("%s AND %s = %s", selection,
                    dataBaseManagerID, id);
        }
        return selection;
    }

    @Override
    public int delete(@NonNull Uri uri, String selection, String[] selectionArgs) {
        int deleteItemRow;
        String tableName;
        String columnName = "";
        switch (ContentProviderValues.uriMatcher.match(uri)) {
            case ContentProviderValues.URI_ALL_TASKS:
                tableName = DataBaseManager.TASK_TABLE_NAME;
                break;

            case ContentProviderValues.URI_SINGLE_TASKS_BY_ID:
                tableName = DataBaseManager.TASK_TABLE_NAME;
                columnName = DataBaseManager.COLUMN_TASK_ID_NAME;
                selection = getTaskSelectionById(uri, selection, columnName);
                break;

            case ContentProviderValues.URI_ALL_CATEGORIES:
                tableName = DataBaseManager.CATEGORY_TABLE_NAME;
                break;

            case ContentProviderValues.URI_SINGLE_CATEGORIES_BY_ID:
                tableName = DataBaseManager.CATEGORY_TABLE_NAME;
                columnName = DataBaseManager.COLUMN_CATEGORY_ID_NAME;
                selection = getTaskSelectionById(uri, selection, columnName);
                break;

            case ContentProviderValues.URI_ALL_SUBTASKS:
                tableName = DataBaseManager.SUBTASK_TABLE_NAME;
                break;

            case ContentProviderValues.URI_SINGLE_SUBTASKS_BY_ID:
                tableName = DataBaseManager.SUBTASK_TABLE_NAME;
                columnName = DataBaseManager.COLUMN_SUBTASK_ID_NAME;
                selection = getTaskSelectionById(uri, selection, columnName);
                break;

            case ContentProviderValues.URI_ALL_USERS:
                tableName = DataBaseManager.USER_TABLE_NAME;
                break;

            case ContentProviderValues.URI_SINGLE_USERS_BY_ID:
                tableName = DataBaseManager.USER_TABLE_NAME;
                columnName = DataBaseManager.COLUMN_USER_ID_NAME;
                selection = getTaskSelectionById(uri, selection, columnName);
                break;
            default:
                throw new UnsupportedOperationException("Not yet implemented for " + uri);

        }

            deleteItemRow = dataBaseManager.getWritableDatabase()
                    .delete(tableName, getTaskSelectionById(uri, selection, columnName), selectionArgs);

        notifyChange(uri);
        return deleteItemRow;
    }

    private void notifyChange(Uri uri){
        if (getContext() != null) {
            getContext().getContentResolver().notifyChange(uri, null);
        }
    }

    @Override
    public Uri insert(@NonNull Uri uri, ContentValues values) {
        Uri insertedUri;
        long insertedRowId;
        switch (ContentProviderValues.uriMatcher.match(uri)) {
            case ContentProviderValues.URI_ALL_TASKS:
            case ContentProviderValues.URI_SINGLE_TASKS_BY_ID:
                insertedRowId = dataBaseManager.getWritableDatabase()
                        .insert(DataBaseManager.TASK_TABLE_NAME, null, values);
                insertedUri = ContentUris
                        .withAppendedId(ContentProviderValues.TASKS_CONTENT_URI, insertedRowId);
                break;

            case ContentProviderValues.URI_ALL_CATEGORIES:
            case ContentProviderValues.URI_SINGLE_CATEGORIES_BY_ID:
                 insertedRowId = dataBaseManager.getWritableDatabase()
                         .insert(DataBaseManager.CATEGORY_TABLE_NAME, null, values);
                 insertedUri = ContentUris
                         .withAppendedId(ContentProviderValues.CATEGORIES_CONTENT_URI, insertedRowId);
                 break;

            case ContentProviderValues.URI_ALL_USERS:
            case ContentProviderValues.URI_SINGLE_USERS_BY_ID:
                insertedRowId = dataBaseManager.getWritableDatabase()
                        .insert(DataBaseManager.USER_TABLE_NAME, null, values);
                insertedUri = ContentUris
                        .withAppendedId(ContentProviderValues.USERS_CONTENT_URI, insertedRowId);
                break;

            case ContentProviderValues.URI_ALL_SUBTASKS:
            case ContentProviderValues.URI_SINGLE_SUBTASKS_BY_ID:
                insertedRowId = dataBaseManager.getWritableDatabase()
                        .insert(DataBaseManager.SUBTASK_TABLE_NAME, null, values);
                insertedUri = ContentUris
                        .withAppendedId(ContentProviderValues.SUBTASKS_CONTENT_URI, insertedRowId);
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
        Uri updatedUri;
        int updatedRowId;
        String tableName;
        Uri contentProviderValues;
        switch (ContentProviderValues.uriMatcher.match(uri)) {
            case ContentProviderValues.URI_ALL_TASKS:
                tableName = DataBaseManager.TASK_TABLE_NAME;
                contentProviderValues = ContentProviderValues.TASKS_CONTENT_URI;
                break;
            case ContentProviderValues.URI_SINGLE_TASKS_BY_ID:
                tableName = DataBaseManager.TASK_TABLE_NAME;
                contentProviderValues = ContentProviderValues.TASKS_CONTENT_URI;
                selection = getTaskSelectionById(uri,selection, DataBaseManager.COLUMN_TASK_ID_NAME);
              break;

            case ContentProviderValues.URI_ALL_CATEGORIES:
                tableName = DataBaseManager.CATEGORY_TABLE_NAME;
                contentProviderValues = ContentProviderValues.CATEGORIES_CONTENT_URI;
                break;
            case ContentProviderValues.URI_SINGLE_CATEGORIES_BY_ID:
                tableName = DataBaseManager.CATEGORY_TABLE_NAME;
                contentProviderValues = ContentProviderValues.CATEGORIES_CONTENT_URI;
                selection = getTaskSelectionById(uri,selection, DataBaseManager.COLUMN_CATEGORY_ID_NAME);
                break;

            case ContentProviderValues.URI_ALL_SUBTASKS:
                tableName = DataBaseManager.SUBTASK_TABLE_NAME;
                contentProviderValues = ContentProviderValues.SUBTASKS_CONTENT_URI;
                break;
            case ContentProviderValues.URI_SINGLE_SUBTASKS_BY_ID:
                tableName = DataBaseManager.SUBTASK_TABLE_NAME;
                contentProviderValues = ContentProviderValues.SUBTASKS_CONTENT_URI;
                selection = getTaskSelectionById(uri,selection, DataBaseManager.COLUMN_SUBTASK_ID_NAME);
                break;
            case ContentProviderValues.URI_ALL_USERS:
                tableName = DataBaseManager.USER_TABLE_NAME;
                contentProviderValues = ContentProviderValues.USERS_CONTENT_URI;
                break;
            case ContentProviderValues.URI_SINGLE_USERS_BY_ID:
                tableName = DataBaseManager.USER_TABLE_NAME;
                contentProviderValues = ContentProviderValues.USERS_CONTENT_URI;
                selection = getTaskSelectionById(uri,selection, DataBaseManager.COLUMN_USER_ID_NAME);
                break;
            default:
                throw new UnsupportedOperationException("Not yet implemented");
        }

        updatedRowId = dataBaseManager.getWritableDatabase()
                .update(tableName, values, selection, selectionArgs);
        updatedUri = ContentUris
                .withAppendedId(contentProviderValues, updatedRowId);
        notifyChange(updatedUri);
        return updatedRowId;
    }

    @Override
    public String getType(@NonNull Uri uri) {
        String type = null;
        switch (ContentProviderValues.uriMatcher.match(uri)) {
            case ContentProviderValues.URI_ALL_TASKS:
                type = ContentProviderValues.TYPE_CONTENT_TASK_MANY;
                break;
            case ContentProviderValues.URI_SINGLE_TASKS_BY_ID:
                type = ContentProviderValues.TYPE_CONTENT_TASK_SINGLE;
                break;

            case ContentProviderValues.URI_ALL_CATEGORIES:
                type = ContentProviderValues.TYPE_CONTENT_CATEGORY_MANY;
                break;
            case ContentProviderValues.URI_SINGLE_CATEGORIES_BY_ID:
                type = ContentProviderValues.TYPE_CONTENT_CATEGORY_SINGLE;
                break;

            case ContentProviderValues.URI_ALL_USERS:
                type = ContentProviderValues.TYPE_CONTENT_USER_MANY;
                break;
            case ContentProviderValues.URI_SINGLE_USERS_BY_ID:
                type = ContentProviderValues.TYPE_CONTENT_USER_SINGLE;
                break;

            case ContentProviderValues.URI_ALL_SUBTASKS:
                type = ContentProviderValues.TYPE_CONTENT_SUBTASK_MANY;
                break;
            case ContentProviderValues.URI_SINGLE_SUBTASKS_BY_ID:
                type = ContentProviderValues.TYPE_CONTENT_SUBTASK_SINGLE;
                break;
        }
        return type;
    }
}