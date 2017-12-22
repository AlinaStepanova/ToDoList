package com.example.alina.todolist.data.db;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;


public class DatabaseProvider extends ContentProvider {
    private DatabaseManager databaseManager;

    @Override
    public boolean onCreate() {
        databaseManager = new DatabaseManager(getContext());
        return true;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        String defOrder;
        SQLiteQueryBuilder builder = new SQLiteQueryBuilder();

        switch (ContentProviderValues.uriMatcher.match(uri)){
            case ContentUriTypes.ALL_TASK:
                builder.setTables(DatabaseSchema.Task.TABLE_NAME);
                defOrder = DatabaseSchema.Task.ID;
                break;
            case ContentUriTypes.ID_TASK:
                builder.setTables(DatabaseSchema.Task.TABLE_NAME);
                selection = getSelectionByID(DatabaseSchema.Task.ID, selection, uri.getLastPathSegment());
                defOrder = DatabaseSchema.Task.ID;
                break;
            case ContentUriTypes.ALL_SUB_TASK:
                builder.setTables(DatabaseSchema.SubTask.TABLE_NAME);
                defOrder = DatabaseSchema.SubTask.ID;
                break;
            case ContentUriTypes.ID_SUB_TASK:
                builder.setTables(DatabaseSchema.SubTask.TABLE_NAME);
                selection = getSelectionByID(DatabaseSchema.SubTask.ID, selection, uri.getLastPathSegment());
                defOrder = DatabaseSchema.SubTask.ID;
                break;
            case ContentUriTypes.ALL_CATEGORY:
                builder.setTables(DatabaseSchema.Category.TABLE_NAME);
                defOrder = DatabaseSchema.Category.ID;
                break;
            case ContentUriTypes.ID_CATEGORY:
                builder.setTables(DatabaseSchema.Category.TABLE_NAME);
                selection = getSelectionByID(DatabaseSchema.Category.ID, selection, uri.getLastPathSegment());
                defOrder = DatabaseSchema.Category.ID;
                break;
            case ContentUriTypes.ALL_USER:
                builder.setTables(DatabaseSchema.User.TABLE_NAME);
                defOrder = DatabaseSchema.User.ID;
                break;
            case ContentUriTypes.ID_USER:
                builder.setTables(DatabaseSchema.User.TABLE_NAME);
                selection = getSelectionByID(DatabaseSchema.User.ID, selection, uri.getLastPathSegment());
                defOrder = DatabaseSchema.User.ID;
                break;
            default: throw new UnsupportedOperationException("Unknown URI: " + uri);
        }
        sortOrder = getSortOrder(sortOrder, defOrder);
        Cursor cursor = builder.query(databaseManager.getWritableDatabase(), projection, selection, selectionArgs, null, null, sortOrder);
        if (getContext() != null){
            cursor.setNotificationUri(getContext().getContentResolver(), uri);
        }
        return cursor;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        switch (ContentProviderValues.uriMatcher.match(uri)) {
            case ContentUriTypes.ALL_TASK:
                return ContentProviderValues.getTypeContentMany(DatabaseSchema.Task.TABLE_NAME);
            case ContentUriTypes.ID_TASK:
                return ContentProviderValues.getTypeContentSingle(DatabaseSchema.Task.TABLE_NAME);
            case ContentUriTypes.ALL_SUB_TASK:
                return ContentProviderValues.getTypeContentMany(DatabaseSchema.SubTask.TABLE_NAME);
            case ContentUriTypes.ID_SUB_TASK:
                return ContentProviderValues.getTypeContentSingle(DatabaseSchema.SubTask.TABLE_NAME);
            case ContentUriTypes.ALL_CATEGORY:
                return ContentProviderValues.getTypeContentMany(DatabaseSchema.Category.TABLE_NAME);
            case ContentUriTypes.ID_CATEGORY:
                return ContentProviderValues.getTypeContentSingle(DatabaseSchema.Category.TABLE_NAME);
            case ContentUriTypes.ALL_USER:
                return ContentProviderValues.getTypeContentMany(DatabaseSchema.User.TABLE_NAME);
            case ContentUriTypes.ID_USER:
                return ContentProviderValues.getTypeContentSingle(DatabaseSchema.User.TABLE_NAME);
            default:
                throw new UnsupportedOperationException("Unknown URI: " + uri);
        }
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues contentValues) {
        SQLiteDatabase db = databaseManager.getWritableDatabase();
        ContentValues values;
        if (contentValues != null){
            values = new ContentValues(contentValues);
        }else {
            values = new ContentValues();
        }
        long rowId;
        Uri rowUri = Uri.EMPTY;

        switch (ContentProviderValues.uriMatcher.match(uri)) {
            case ContentUriTypes.ALL_TASK:
                rowId = db.insert(DatabaseSchema.Task.TABLE_NAME,
                        null,
                        values);
                break;
            case ContentUriTypes.ALL_SUB_TASK:
                rowId = db.insert(DatabaseSchema.SubTask.TABLE_NAME,
                        null,
                        values);
                break;
            case ContentUriTypes.ALL_CATEGORY:
                rowId = db.insert(DatabaseSchema.Category.TABLE_NAME,
                        null,
                        values);
                break;
            case ContentUriTypes.ALL_USER:
                rowId = db.insert(DatabaseSchema.User.TABLE_NAME,
                        null,
                        values);
                break;
            default:
                throw new UnsupportedOperationException("Unknown URI: " + uri);
        }
        if (rowId > 0) {
            rowUri = ContentUris.withAppendedId(uri, rowId);
            if (getContext() != null)
                getContext().getContentResolver().notifyChange(rowUri, null);
        }
        notifyChanges(uri);
        return rowUri;
    }

    @Override
    public int bulkInsert(@NonNull Uri uri, @NonNull ContentValues[] values) {
        SQLiteDatabase db = databaseManager.getWritableDatabase();
        String table;
        int numInserted;
        switch (ContentProviderValues.uriMatcher.match(uri)) {
            case ContentUriTypes.ALL_TASK:
                table = DatabaseSchema.Task.TABLE_NAME;
                break;
            case ContentUriTypes.ALL_SUB_TASK:
                table = DatabaseSchema.SubTask.TABLE_NAME;
                break;
            case ContentUriTypes.ALL_CATEGORY:
                table = DatabaseSchema.Category.TABLE_NAME;
                break;
            case ContentUriTypes.ALL_USER:
                table = DatabaseSchema.User.TABLE_NAME;
                break;
            default:
                throw new UnsupportedOperationException("Unknown URI: " + uri);
        }
        db.beginTransaction();
        try{
            for (ContentValues x : values){
                db.insertWithOnConflict(table, null, x, SQLiteDatabase.CONFLICT_IGNORE);
                /*long newId = db.insertWithOnConflict(table, null, x, SQLiteDatabase.CONFLICT_IGNORE);
                if (newId < 0 )
                    throw new SQLException("Failed to insert row into " + uri);*/
            }
            db.setTransactionSuccessful();
            if (getContext() != null)
                getContext().getContentResolver().notifyChange(uri, null);
            numInserted = values.length;
        }finally {
            db.endTransaction();
        }
        return numInserted;
    }

    @Override
    public int delete(@NonNull Uri uri, String selection, String[] selectionArgs) {
        String tableName;
        switch (ContentProviderValues.uriMatcher.match(uri)) {
            case ContentUriTypes.ALL_TASK:
                tableName = DatabaseSchema.Task.TABLE_NAME;
                break;
            case ContentUriTypes.ID_TASK:
                tableName = DatabaseSchema.Task.TABLE_NAME;
                selection = getSelectionByID(DatabaseSchema.Task.ID, selection, uri.getLastPathSegment());
                break;
            case ContentUriTypes.ALL_SUB_TASK:
                tableName = DatabaseSchema.SubTask.TABLE_NAME;
                break;
            case ContentUriTypes.ID_SUB_TASK:
                tableName = DatabaseSchema.SubTask.TABLE_NAME;
                selection = getSelectionByID(DatabaseSchema.SubTask.ID, selection, uri.getLastPathSegment());
                break;
            case ContentUriTypes.ALL_CATEGORY:
                tableName = DatabaseSchema.Category.TABLE_NAME;
                break;
            case ContentUriTypes.ID_CATEGORY:
                tableName = DatabaseSchema.Category.TABLE_NAME;
                selection = getSelectionByID(DatabaseSchema.Category.ID, selection, uri.getLastPathSegment());
                break;
            case ContentUriTypes.ALL_USER:
                tableName = DatabaseSchema.User.TABLE_NAME;
                break;
            case ContentUriTypes.ID_USER:
                tableName = DatabaseSchema.User.TABLE_NAME;
                selection = getSelectionByID(DatabaseSchema.User.ID, selection, uri.getLastPathSegment());
                break;
            default:
                throw new UnsupportedOperationException("Unknown URI: " + uri);
        }
        notifyChanges(uri);
        return deleteRow(tableName, selection, selectionArgs);
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues contentValues, @Nullable String selection, @Nullable String[] selectionArgs) {
        String tableName;
        switch (ContentProviderValues.uriMatcher.match(uri)) {
            case ContentUriTypes.ALL_TASK:
                tableName = DatabaseSchema.Task.TABLE_NAME;
                break;
            case ContentUriTypes.ID_TASK:
                tableName = DatabaseSchema.Task.TABLE_NAME;
                selection = getSelectionByID(DatabaseSchema.Task.ID, selection, uri.getLastPathSegment());
                break;
            case ContentUriTypes.ALL_SUB_TASK:
                tableName = DatabaseSchema.SubTask.TABLE_NAME;
                break;
            case ContentUriTypes.ID_SUB_TASK:
                tableName = DatabaseSchema.SubTask.TABLE_NAME;
                selection = getSelectionByID(DatabaseSchema.SubTask.ID, selection, uri.getLastPathSegment());
                break;
            case ContentUriTypes.ALL_CATEGORY:
                tableName = DatabaseSchema.Category.TABLE_NAME;
                break;
            case ContentUriTypes.ID_CATEGORY:
                tableName = DatabaseSchema.Category.TABLE_NAME;
                selection = getSelectionByID(DatabaseSchema.Category.ID, selection, uri.getLastPathSegment());
                break;
            case ContentUriTypes.ALL_USER:
                tableName = DatabaseSchema.User.TABLE_NAME;
                break;
            case ContentUriTypes.ID_USER:
                tableName = DatabaseSchema.User.TABLE_NAME;
                selection = getSelectionByID(DatabaseSchema.User.ID, selection, uri.getLastPathSegment());
                break;
            default:
                throw new UnsupportedOperationException("Unknown URI: " + uri);
        }
        notifyChanges(uri);
        return updateTable(tableName, contentValues, selection, selectionArgs);
    }

    private String getSelectionByID(String column, String selection, String idFromUri){
        if (TextUtils.isEmpty(selection)) {
            selection = String.format("%s = %s", column, idFromUri);
        } else {
            selection = String.format("%s AND %s = %s", selection,
                    column, idFromUri);
        }
        return selection;
    }

    private String getSortOrder(String order, String defaultOrder){
        return TextUtils.isEmpty(order) ? defaultOrder : order;
    }

    private void notifyChanges(Uri uri){
        if (getContext() != null) {
            getContext().getContentResolver().notifyChange(uri, null);
        }
    }

    private int deleteRow(String table, String selection, String[] selectionArgs){
        return databaseManager.getWritableDatabase().delete(table, selection, selectionArgs);
    }

    private int updateTable(String tableName, ContentValues values, String selection, String[] selectionArgs){
        return databaseManager.getWritableDatabase().update(tableName, values, selection, selectionArgs);
    }
}
