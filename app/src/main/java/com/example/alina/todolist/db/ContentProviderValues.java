package com.example.alina.todolist.db;

import android.content.UriMatcher;
import android.net.Uri;

/**
 * Created by Student_3 on 05/12/2017.
 */

public class ContentProviderValues {

    private static final String AUTHORITY = "com.example.alina.todolist";

    public static final Uri TASKS_CONTENT_URI =
            Uri.parse("content://" + AUTHORITY + "/" + DataBaseManager.TaskTable.TASK_TABLE_NAME);

    public static final int URI_ALL_TASKS = 1;

    public static final int URI_SINGLE_TASKS_BY_ID = 2;

    public static final UriMatcher uriMatcher;

    public static final String TYPE_CONTENT_MANY = "vnd.android.cursor.dir/vnd."
            + AUTHORITY + "." + DataBaseManager.TaskTable.TASK_TABLE_NAME;

    public static final String TYPE_CONTENT_SINGLE = "vnd.android.cursor.item/vnd."
            + AUTHORITY + "." + DataBaseManager.TaskTable.TASK_TABLE_NAME;

    static {
        uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        uriMatcher.addURI(AUTHORITY, DataBaseManager.TaskTable.TASK_TABLE_NAME, URI_ALL_TASKS);
        uriMatcher.addURI(AUTHORITY, DataBaseManager.TaskTable.TASK_TABLE_NAME + "/#", URI_SINGLE_TASKS_BY_ID);
    }
}
