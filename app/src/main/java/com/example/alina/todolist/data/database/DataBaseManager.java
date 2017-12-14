package com.example.alina.todolist.data.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DataBaseManager extends SQLiteOpenHelper {

    private static final String DATA_BASE_NAME = "DATABASE";
    private static final int DATA_BASE_VERSION_1 = 1;

    public static final String TASK_TABLE_NAME = "task_table";
    public static final String SUBTASK_TABLE_NAME = "subtask_table";
    public static final String CATEGORY_TABLE_NAME = "category_table";
    public static final String USER_TABLE_NAME = "user_table";

    public static final String COLUMN_TASK_ID_NAME = "task_id";
    public static final String COLUMN_TASK_USER_ID = "task_user_id";
    public static final String COLUMN_TASK_DESCRIPTION = "task_description";
    public static final String COLUMN_TASK_STATUS = "task_status";
    public static final String COLUMN_TASK_NAME = "task_name";
    private static final String COLUMN_TASK_CATEGORY_ID = "task_category_id";
    public static final String COLUMN_TASK_EXPIRE_DATE = "task_expire_date";
    public static final String COLUMN_TASK_LATITUDE = "task_latitude";
    public static final String COLUMN_TASK_LONGITUDE = "task_longitude";

    public static final String COLUMN_CATEGORY_ID_NAME = "category_id";
    private static final String COLUMN_CATEGORY_USER_ID = "category_user_id";
    public static final String COLUMN_CATEGORY_NAME = "category_name";
    public static final String COLUMN_CATEGORY_COLOR = "category_color";

    public static final String COLUMN_SUBTASK_ID_NAME = "subtask_id";
    public static final String COLUMN_SUBTASK_DESCRIPTION = "subtask_description";
    public static final String COLUMN_SUBTASK_STATUS = "subtask_status";
    private static final String COLUMN_SUBTASK_TASK_ID = "subtask_task_id";

    public static final String COLUMN_USER_ID_NAME = "user_id";
    public static final String COLUMN_USER_NAME = "user_name";
    public static final String COLUMN_USER_EMAIL = "user_email";
    public static final String COLUMN_USER_PIN = "user_pin";


    public DataBaseManager(final Context context) {
        super(context, DATA_BASE_NAME, null, DATA_BASE_VERSION_1);
    }

    @Override
    public void onCreate(final SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL("CREATE TABLE " + TASK_TABLE_NAME + "("
                + COLUMN_TASK_ID_NAME + " integer primary key autoincrement,"
                + COLUMN_TASK_USER_ID + " integer,"
                + COLUMN_TASK_DESCRIPTION + " text,"
                + COLUMN_TASK_STATUS + " text,"
                + COLUMN_TASK_NAME + " text,"
                + COLUMN_TASK_CATEGORY_ID + " integer,"
                + COLUMN_TASK_EXPIRE_DATE + " integer"
                + ");");

        sqLiteDatabase.execSQL("CREATE TABLE " + CATEGORY_TABLE_NAME + "("
                + COLUMN_CATEGORY_ID_NAME + " integer primary key autoincrement,"
                + COLUMN_CATEGORY_USER_ID + " integer,"
                + COLUMN_CATEGORY_NAME + " text,"
                + COLUMN_CATEGORY_COLOR + " integer"
                + ");");

        sqLiteDatabase.execSQL("CREATE TABLE " + SUBTASK_TABLE_NAME + "("
                + COLUMN_SUBTASK_ID_NAME + " integer primary key autoincrement,"
                + COLUMN_SUBTASK_DESCRIPTION + " text,"
                + COLUMN_SUBTASK_STATUS + " text,"
                + COLUMN_SUBTASK_TASK_ID + " integer"
                + ");");

        sqLiteDatabase.execSQL("CREATE TABLE " + USER_TABLE_NAME + "("
                + COLUMN_USER_ID_NAME + " integer primary key autoincrement,"
                + COLUMN_USER_NAME + " text,"
                + COLUMN_USER_EMAIL + " text,"
                + COLUMN_USER_PIN + " integer"
                + ");");

    }

    @Override
    public void onUpgrade(final SQLiteDatabase sqLiteDatabase, final int oldVersion,
                          final int newVersion) {

    }
}