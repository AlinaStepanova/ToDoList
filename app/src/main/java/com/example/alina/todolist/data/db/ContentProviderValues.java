package com.example.alina.todolist.data.db;


import android.content.UriMatcher;
import android.net.Uri;

public class ContentProviderValues {
    private static final String AUTHORITY = "com.example.alina.todolist.database";

    public static final UriMatcher uriMatcher;

    public static Uri getContentUri(String tableName){
        return Uri.parse("content://" + AUTHORITY + "/" + tableName);
    }

    public static String getTypeContentMany(String tableName){
        return "vnd.android.cursor.dir/vnd." + AUTHORITY + "." + tableName;
    }

    public static String getTypeContentSingle(String tableName){
        return "vnd.android.cursor.item/vnd." + AUTHORITY + "." + tableName;
    }

    static {
        uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        uriMatcher.addURI(AUTHORITY, DatabaseSchema.Task.TABLE_NAME, ContentUriTypes.ALL_TASK);
        uriMatcher.addURI(AUTHORITY, DatabaseSchema.Task.TABLE_NAME + "/#", ContentUriTypes.ID_TASK);

        uriMatcher.addURI(AUTHORITY, DatabaseSchema.Category.TABLE_NAME, ContentUriTypes.ALL_CATEGORY);
        uriMatcher.addURI(AUTHORITY, DatabaseSchema.Category.TABLE_NAME + "/#", ContentUriTypes.ID_CATEGORY);

        uriMatcher.addURI(AUTHORITY, DatabaseSchema.User.TABLE_NAME, ContentUriTypes.ALL_USER);
        uriMatcher.addURI(AUTHORITY, DatabaseSchema.User.TABLE_NAME + "/#", ContentUriTypes.ID_USER);

        uriMatcher.addURI(AUTHORITY, DatabaseSchema.SubTask.TABLE_NAME, ContentUriTypes.ALL_SUB_TASK);
        uriMatcher.addURI(AUTHORITY, DatabaseSchema.SubTask.TABLE_NAME + "/#", ContentUriTypes.ID_SUB_TASK);
    }
}
