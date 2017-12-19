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

    public static final Uri TASK_CATEGORY_CONTENT_URI =
            Uri.parse("content://" + AUTHORITY + "/" + DataBaseManager.TASK_TABLE_NAME + "&"
            + DataBaseManager.CATEGORY_TABLE_NAME);

    public static final int URI_ALL_TASKS = 1;

    public static final int URI_SINGLE_TASKS_BY_ID = 2;

    public static final int URI_ALL_CATEGORIES = 3;

    public static final int URI_SINGLE_CATEGORY_BY_ID = 4;

    public static final int URI_ALL_SUBTASKS = 5;

    public static final int URI_SINGLE_SUBTASK_BY_ID = 6;

    public static final int URI_ALL_USERS = 7;

    public static final int URI_SINGLE_USER_BY_ID = 8;

    public static final int URI_ALL_JOIN_TASK_CATEGORY_BY_USERID = 9;

    public static final int URI_SINGLE_JOIN_TASK_CATEGORY_BY_TASKID = 10;

    public static final UriMatcher uriMatcher;

    private static final String VND_AUTHORITY_SINGLE = "vnd.android.cursor.item/vnd."
            + AUTHORITY + ".";
    private static final String VND_AUTHORITY_MANY = "vnd.android.cursor.dir/vnd."
            + AUTHORITY + ".";

    public static final String TYPE_CONTENT_TASK_MANY = VND_AUTHORITY_MANY +
            DataBaseManager.TASK_TABLE_NAME;

    public static final String TYPE_CONTENT_TASK_SINGLE = VND_AUTHORITY_SINGLE
            + DataBaseManager.TASK_TABLE_NAME;

    public static final String TYPE_CONTENT_CATEGORY_MANY = VND_AUTHORITY_MANY
            + DataBaseManager.CATEGORY_TABLE_NAME;

    public static final String TYPE_CONTENT_CATEGORY_SINGLE = VND_AUTHORITY_SINGLE
            + DataBaseManager.CATEGORY_TABLE_NAME;

    public static final String TYPE_CONTENT_SUBTASK_MANY = VND_AUTHORITY_MANY
            + DataBaseManager.SUBTASK_TABLE_NAME;

    public static final String TYPE_CONTENT_SUBTASK_SINGLE = VND_AUTHORITY_SINGLE
            + DataBaseManager.SUBTASK_TABLE_NAME;

    public static final String TYPE_CONTENT_USER_MANY = VND_AUTHORITY_MANY
            + DataBaseManager.USER_TABLE_NAME;

    public static final String TYPE_CONTENT_USER_SINGLE = VND_AUTHORITY_SINGLE
            + DataBaseManager.USER_TABLE_NAME;

    public static final String TYPE_CONTENT_TASK_CATEGORY_SINGLE = VND_AUTHORITY_SINGLE
            + DataBaseManager.TASK_TABLE_NAME + "&" + DataBaseManager.CATEGORY_TABLE_NAME;

    public static final String TYPE_CONTENT_TASK_CATEGORY_ALL_BY_USERID = VND_AUTHORITY_MANY
            + DataBaseManager.TASK_TABLE_NAME + "&" + DataBaseManager.CATEGORY_TABLE_NAME;

    static {
        uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        uriMatcher.addURI(AUTHORITY, DataBaseManager.TASK_TABLE_NAME, URI_ALL_TASKS);
        uriMatcher.addURI(AUTHORITY, DataBaseManager.TASK_TABLE_NAME + "/#", URI_SINGLE_TASKS_BY_ID);
        uriMatcher.addURI(AUTHORITY, DataBaseManager.CATEGORY_TABLE_NAME, URI_ALL_CATEGORIES);
        uriMatcher.addURI(AUTHORITY, DataBaseManager.CATEGORY_TABLE_NAME + "/#", URI_SINGLE_CATEGORY_BY_ID);
        uriMatcher.addURI(AUTHORITY, DataBaseManager.SUBTASK_TABLE_NAME, URI_ALL_SUBTASKS);
        uriMatcher.addURI(AUTHORITY, DataBaseManager.SUBTASK_TABLE_NAME + "/#", URI_SINGLE_SUBTASK_BY_ID);
        uriMatcher.addURI(AUTHORITY, DataBaseManager.USER_TABLE_NAME, URI_ALL_USERS);
        uriMatcher.addURI(AUTHORITY, DataBaseManager.USER_TABLE_NAME + "/#", URI_SINGLE_USER_BY_ID);
        uriMatcher.addURI(AUTHORITY, DataBaseManager.TASK_TABLE_NAME + "&" +
                DataBaseManager.CATEGORY_TABLE_NAME, URI_ALL_JOIN_TASK_CATEGORY_BY_USERID);
        uriMatcher.addURI(AUTHORITY, DataBaseManager.TASK_TABLE_NAME + "&" +
                DataBaseManager.CATEGORY_TABLE_NAME, URI_SINGLE_JOIN_TASK_CATEGORY_BY_TASKID);
    }
}
