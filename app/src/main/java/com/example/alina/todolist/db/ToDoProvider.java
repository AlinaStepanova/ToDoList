package com.example.alina.todolist.db;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.util.Log;

import com.example.alina.todolist.data.DataBaseDataSource;

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
        String table = null;
        String rawQuery = null;
        String defaultSortOrder;
        switch (ContentProviderValues.uriMatcher.match(uri)) {
            case ContentProviderValues.URI_ALL_TASKS:
                table = DataBaseManager.TASK_TABLE_NAME;
                defaultSortOrder = DataBaseManager.COLUMN_TASK_ID;
                break;
            case ContentProviderValues.URI_SINGLE_TASKS_BY_ID:
                selection = addUriIdSelection(DataBaseManager.COLUMN_TASK_ID, selection,
                        uri.getLastPathSegment());
                table = DataBaseManager.TASK_TABLE_NAME;
                defaultSortOrder = DataBaseManager.COLUMN_TASK_ID;
                break;
            case ContentProviderValues.URI_ALL_CATEGORIES:
                table = DataBaseManager.CATEGORY_TABLE_NAME;
                defaultSortOrder = DataBaseManager.COLUMN_CATEGORY_ID;
                break;
            case ContentProviderValues.URI_SINGLE_CATEGORY_BY_ID:
                selection = addUriIdSelection(DataBaseManager.COLUMN_CATEGORY_ID, selection,
                        uri.getLastPathSegment());
                table = DataBaseManager.CATEGORY_TABLE_NAME;
                defaultSortOrder = DataBaseManager.COLUMN_CATEGORY_ID;
                break;
            case ContentProviderValues.URI_ALL_SUBTASKS:
                table = DataBaseManager.SUBTASK_TABLE_NAME;
                defaultSortOrder = DataBaseManager.COLUMN_TASK_ID;
                break;
            case ContentProviderValues.URI_SINGLE_SUBTASK_BY_ID:
                selection = addUriIdSelection(DataBaseManager.COLUMN_TASK_ID, selection,
                        uri.getLastPathSegment());
                table = DataBaseManager.SUBTASK_TABLE_NAME;
                defaultSortOrder = DataBaseManager.COLUMN_TASK_ID;
                break;
            case ContentProviderValues.URI_ALL_USERS:
                table = DataBaseManager.USER_TABLE_NAME;
                defaultSortOrder = DataBaseManager.COLUMN_USER_ID;
                break;
            case ContentProviderValues.URI_SINGLE_USER_BY_ID:
                selection = addUriIdSelection(DataBaseManager.COLUMN_USER_ID, selection,
                        uri.getLastPathSegment());
                table = DataBaseManager.USER_TABLE_NAME;
                defaultSortOrder = DataBaseManager.COLUMN_USER_ID;
                break;
            case ContentProviderValues.URI_SINGLE_JOIN_TASK_CATEGORY_BY_TASKID:
                rawQuery = DataBaseManager.QUERY_JOIN_TASK_CATEGORY_ALL;
                defaultSortOrder = DataBaseManager.COLUMN_TASK_ID;
                break;
            case ContentProviderValues.URI_ALL_JOIN_TASK_CATEGORY_BY_USERID:
                rawQuery = DataBaseManager.QUERY_JOIN_TASK_CATEGORY_BY_USER;
                defaultSortOrder = DataBaseManager.COLUMN_TASK_ID;
                break;
            default:
                throw new UnsupportedOperationException("Not yet implemented for " + uri);
        }
        sortOrder = setOrder(sortOrder, defaultSortOrder);
        cursor = createCursor(table, projection, selection, selectionArgs,
                sortOrder, rawQuery);
        notifyChange(uri);
        return cursor;
    }

    @Override
    public int delete(@NonNull Uri uri, String selection, String[] selectionArgs) {
        String table;
        switch (ContentProviderValues.uriMatcher.match(uri)) {
            case ContentProviderValues.URI_ALL_TASKS:
                table = DataBaseManager.TASK_TABLE_NAME;
                break;
            case ContentProviderValues.URI_SINGLE_TASKS_BY_ID:
                selection = addUriIdSelection(DataBaseManager.COLUMN_TASK_ID, selection,
                        uri.getLastPathSegment());
                table = DataBaseManager.TASK_TABLE_NAME;
                break;
            case ContentProviderValues.URI_ALL_CATEGORIES:
                table = DataBaseManager.CATEGORY_TABLE_NAME;
                break;
            case ContentProviderValues.URI_SINGLE_CATEGORY_BY_ID:
                selection = addUriIdSelection(DataBaseManager.COLUMN_CATEGORY_ID, selection,
                        uri.getLastPathSegment());
                table = DataBaseManager.CATEGORY_TABLE_NAME;
                break;
            case ContentProviderValues.URI_ALL_SUBTASKS:
                table = DataBaseManager.SUBTASK_TABLE_NAME;
                break;
            case ContentProviderValues.URI_SINGLE_SUBTASK_BY_ID:
                selection = addUriIdSelection(DataBaseManager.COLUMN_TASK_ID, selection,
                        uri.getLastPathSegment());
                table = DataBaseManager.SUBTASK_TABLE_NAME;
                break;
            case ContentProviderValues.URI_ALL_USERS:
                table = DataBaseManager.USER_TABLE_NAME;
                break;
            case ContentProviderValues.URI_SINGLE_USER_BY_ID:
                selection = addUriIdSelection(DataBaseManager.COLUMN_USER_ID, selection,
                        uri.getLastPathSegment());
                table = DataBaseManager.USER_TABLE_NAME;
                break;
            default:
                throw new UnsupportedOperationException("Not yet implemented for " + uri);

        }
        notifyChange(uri);
        return deleteRows(table, selection, selectionArgs);
    }

    @Override
    public Uri insert(@NonNull Uri uri, ContentValues values) {
        Uri insertedUri;
        String table;
        switch (ContentProviderValues.uriMatcher.match(uri)) {
            case ContentProviderValues.URI_ALL_TASKS:
                table = DataBaseManager.TASK_TABLE_NAME;
                break;
            case ContentProviderValues.URI_ALL_CATEGORIES:
                table = DataBaseManager.CATEGORY_TABLE_NAME;
                break;
            case ContentProviderValues.URI_ALL_SUBTASKS:
                table = DataBaseManager.SUBTASK_TABLE_NAME;
                break;
            case ContentProviderValues.URI_ALL_USERS:
                table = DataBaseManager.USER_TABLE_NAME;
                break;
            default:
                throw new UnsupportedOperationException("Not yet implemented for " + uri);
        }
        insertedUri = insertRow(table, values, uri);
        notifyChange(insertedUri);
        return insertedUri;
    }

    @Override
    public int update(@NonNull Uri uri, ContentValues values, String selection,
                      String[] selectionArgs) {
        String table;
        switch (ContentProviderValues.uriMatcher.match(uri)) {
            case ContentProviderValues.URI_ALL_TASKS:
                table = DataBaseManager.TASK_TABLE_NAME;
                break;
            case ContentProviderValues.URI_SINGLE_TASKS_BY_ID:
                selection = addUriIdSelection(DataBaseManager.COLUMN_TASK_ID, selection,
                        uri.getLastPathSegment());
                table = DataBaseManager.TASK_TABLE_NAME;
                break;
            case ContentProviderValues.URI_ALL_CATEGORIES:
                table = DataBaseManager.CATEGORY_TABLE_NAME;
                break;
            case ContentProviderValues.URI_SINGLE_CATEGORY_BY_ID:
                selection = addUriIdSelection(DataBaseManager.COLUMN_CATEGORY_ID, selection,
                        uri.getLastPathSegment());
                table = DataBaseManager.CATEGORY_TABLE_NAME;
                break;
            case ContentProviderValues.URI_ALL_SUBTASKS:
                table = DataBaseManager.SUBTASK_TABLE_NAME;
                break;
            case ContentProviderValues.URI_SINGLE_SUBTASK_BY_ID:
                selection = addUriIdSelection(DataBaseManager.COLUMN_TASK_ID, selection,
                        uri.getLastPathSegment());
                table = DataBaseManager.SUBTASK_TABLE_NAME;
                break;
            case ContentProviderValues.URI_ALL_USERS:
                table = DataBaseManager.USER_TABLE_NAME;
                break;
            case ContentProviderValues.URI_SINGLE_USER_BY_ID:
                selection = addUriIdSelection(DataBaseManager.COLUMN_USER_ID, selection,
                        uri.getLastPathSegment());
                table = DataBaseManager.USER_TABLE_NAME;
                break;
            case ContentProviderValues.URI_SINGLE_JOIN_TASK_CATEGORY_BY_TASKID:
                table = DataBaseManager.TASK_TABLE_NAME;
                break;
            default:
                throw new UnsupportedOperationException("Not yet implemented");
        }
        notifyChange(uri);
        return updateTable(table, values, selection,
                selectionArgs);
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
            case ContentProviderValues.URI_SINGLE_CATEGORY_BY_ID:
                type = ContentProviderValues.TYPE_CONTENT_CATEGORY_SINGLE;
                break;
            case ContentProviderValues.URI_ALL_SUBTASKS:
                type = ContentProviderValues.TYPE_CONTENT_SUBTASK_MANY;
                break;
            case ContentProviderValues.URI_SINGLE_SUBTASK_BY_ID:
                type = ContentProviderValues.TYPE_CONTENT_SUBTASK_SINGLE;
                break;
            case ContentProviderValues.URI_ALL_USERS:
                type = ContentProviderValues.TYPE_CONTENT_USER_MANY;
                break;
            case ContentProviderValues.URI_SINGLE_USER_BY_ID:
                type = ContentProviderValues.TYPE_CONTENT_USER_SINGLE;
                break;
            case ContentProviderValues.URI_SINGLE_JOIN_TASK_CATEGORY_BY_TASKID:
                type = ContentProviderValues.TYPE_CONTENT_TASK_CATEGORY_SINGLE;
                break;
            case ContentProviderValues.URI_ALL_JOIN_TASK_CATEGORY_BY_USERID:
                type = ContentProviderValues.TYPE_CONTENT_TASK_CATEGORY_ALL_BY_USERID;
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
                                String[] selectionArgs, String sortOrder, String rawQuery){
        Cursor cursor;
        if(table != null) {
            cursor = dataBaseManager.getWritableDatabase()
                    .query(table, projection, selection, selectionArgs,
                            null, null, sortOrder);
        } else {
            cursor = dataBaseManager.getWritableDatabase().rawQuery(rawQuery, selectionArgs);
        }
        return cursor;
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

    private String setOrder(String sortOrder, String defaultSortOrder){
        if (TextUtils.isEmpty(sortOrder)) {
            sortOrder = defaultSortOrder;
        }
        return sortOrder;
    }
}
