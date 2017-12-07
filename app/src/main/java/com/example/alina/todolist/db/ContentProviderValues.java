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

    public static final int URI_SINGLE_JOIN_USER_TASK = 9;

    public static final int URI_SINGLE_JOIN_USER_CATEGORY = 10;

    public static final int URI_SINGLE_JOIN_TASK_SUBTASK = 11;

    public static final int URI_SINGLE_JOIN_TASK_CATEGORY = 12;

    public static final UriMatcher uriMatcher;

    private static final String VND_AUTHORITY = "vnd.android.cursor.dir/vnd."
            + AUTHORITY + ".";

    public static final String TYPE_CONTENT_TASK_MANY = VND_AUTHORITY + DataBaseManager.TASK_TABLE_NAME;

    public static final String TYPE_CONTENT_TASK_SINGLE = VND_AUTHORITY + DataBaseManager.TASK_TABLE_NAME;

    public static final String TYPE_CONTENT_CATEGORY_MANY = VND_AUTHORITY + DataBaseManager.CATEGORY_TABLE_NAME;

    public static final String TYPE_CONTENT_CATEGORY_SINGLE = VND_AUTHORITY + DataBaseManager.CATEGORY_TABLE_NAME;

    public static final String TYPE_CONTENT_SUBTASK_MANY = VND_AUTHORITY + DataBaseManager.SUBTASK_TABLE_NAME;

    public static final String TYPE_CONTENT_SUBTASK_SINGLE = VND_AUTHORITY + DataBaseManager.SUBTASK_TABLE_NAME;

    public static final String TYPE_CONTENT_USER_MANY = VND_AUTHORITY + DataBaseManager.USER_TABLE_NAME;

    public static final String TYPE_CONTENT_USER_SINGLE = VND_AUTHORITY + DataBaseManager.USER_TABLE_NAME;

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
        uriMatcher.addURI(AUTHORITY, DataBaseManager.USER_TABLE_NAME + "/"
                + DataBaseManager.CATEGORY_TABLE_NAME, URI_SINGLE_JOIN_USER_CATEGORY);
        uriMatcher.addURI(AUTHORITY, DataBaseManager.USER_TABLE_NAME + "/"
                + DataBaseManager.TASK_TABLE_NAME, URI_SINGLE_JOIN_USER_TASK);
        uriMatcher.addURI(AUTHORITY, DataBaseManager.TASK_TABLE_NAME + "/"
                + DataBaseManager.SUBTASK_TABLE_NAME, URI_SINGLE_JOIN_TASK_SUBTASK);
        uriMatcher.addURI(AUTHORITY, DataBaseManager.TASK_TABLE_NAME + "/"
                + DataBaseManager.CATEGORY_TABLE_NAME, URI_SINGLE_JOIN_USER_TASK);
    }
}
