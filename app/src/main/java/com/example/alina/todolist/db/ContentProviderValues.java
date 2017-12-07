package com.example.alina.todolist.db;

import android.content.UriMatcher;
import android.net.Uri;

public class ContentProviderValues {

    private static final String AUTHORITY = "com.example.alina.todolist";

    public static final Uri TASKS_CONTENT_URI =
            Uri.parse("content://" + AUTHORITY + "/" + DataBaseManager.TASK_TABLE_NAME);

    public static final Uri CATEGORY_CONTENT_URI =
            Uri.parse("content://" + AUTHORITY + "/" + DataBaseManager.CATEGORY_TABLE_NAME);

    public static final Uri SUBTASK_CONTENT_URI =
            Uri.parse("content://" + AUTHORITY + "/" + DataBaseManager.SUBTASK_TABLE_NAME);

    public static final Uri USER_CONTENT_URI =
            Uri.parse("content://" + AUTHORITY + "/" + DataBaseManager.USER_TABLE_NAME);

    public static final int URI_ALL_TASKS = 1;

    public static final int URI_SINGLE_TASKS_BY_ID = 2;

    public static final int URI_ALL_CATEGORIES = 3;

    public static final int URI_SINGLE_CATEGORY_BY_ID = 4;

    public static final int URI_ALL_SUBTASKS = 5;

    public static final int URI_SINGLE_SUBTASK_BY_ID = 6;

    public static final int URI_ALL_USERS = 7;

    public static final int URI_SINGLE_USER_BY_ID = 8;

    public static final UriMatcher uriMatcher;

    public static final String TYPE_CONTENT_MANY = "vnd.android.cursor.dir/vnd."
            + AUTHORITY + "." + DataBaseManager.TASK_TABLE_NAME;

    public static final String TYPE_CONTENT_SINGLE = "vnd.android.cursor.item/vnd."
            + AUTHORITY + "." + DataBaseManager.TASK_TABLE_NAME;

    static {
        uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        uriMatcher.addURI(AUTHORITY, DataBaseManager.TASK_TABLE_NAME, URI_ALL_TASKS);
        uriMatcher.addURI(AUTHORITY, DataBaseManager.TASK_TABLE_NAME + "/#", URI_SINGLE_TASKS_BY_ID);
        uriMatcher.addURI(AUTHORITY, DataBaseManager.CATEGORY_TABLE_NAME, URI_ALL_CATEGORIES);
        uriMatcher.addURI(AUTHORITY, DataBaseManager.CATEGORY_TABLE_NAME + "/#", URI_SINGLE_CATEGORY_BY_ID);
        uriMatcher.addURI(AUTHORITY, DataBaseManager.SUBTASK_TABLE_NAME, URI_ALL_SUBTASKS);
        uriMatcher.addURI(AUTHORITY, DataBaseManager.SUBTASK_TABLE_NAME + "/#", URI_SINGLE_SUBTASK_BY_ID);
        uriMatcher.addURI(AUTHORITY, DataBaseManager.USER_TABLE_NAME, URI_ALL_USERS);
        uriMatcher.addURI(AUTHORITY, DataBaseManager.USER_TABLE_NAME + "#", URI_SINGLE_USER_BY_ID);
    }
}
