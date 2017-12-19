package com.example.alina.todolist.data.db;


import android.content.UriMatcher;
import android.net.Uri;

import com.example.alina.todolist.BuildConfig;

public class ContentProviderValues {
    private static final String AUTHORITY = BuildConfig.APPLICATION_ID;

    public static final UriMatcher uriMatcher;

    public Uri getContentUri(String tableName){
        return Uri.parse("content://" + AUTHORITY + "/" + tableName);
    }

    public String getTypeContentMany(String tableName){
        return "vnd.android.cursor.dir/vnd." + AUTHORITY + "." + tableName;
    }

    public String getTypeContentSingle(String tableName){
        return "vnd.android.cursor.item/vnd." + AUTHORITY + "." + tableName;
    }

    static {
        uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        uriMatcher.addURI(AUTHORITY, DatabaseManager.Task.TABLE_NAME, ContentUriTypes.ALL_TASK.ordinal());
        uriMatcher.addURI(AUTHORITY, DatabaseManager.Task.TABLE_NAME + "/#", ContentUriTypes.ID_TASK.ordinal());

        uriMatcher.addURI(AUTHORITY, DatabaseManager.Category.TABLE_NAME, ContentUriTypes.ALL_CATEGORY.ordinal());
        uriMatcher.addURI(AUTHORITY, DatabaseManager.Category.TABLE_NAME + "/#", ContentUriTypes.ID_CATEGORY.ordinal());

        uriMatcher.addURI(AUTHORITY, DatabaseManager.User.TABLE_NAME, ContentUriTypes.ALL_USER.ordinal());
        uriMatcher.addURI(AUTHORITY, DatabaseManager.User.TABLE_NAME + "/#", ContentUriTypes.ID_USER.ordinal());
    }
}
