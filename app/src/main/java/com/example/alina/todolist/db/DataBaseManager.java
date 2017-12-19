package com.example.alina.todolist.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DataBaseManager extends SQLiteOpenHelper {

    private static final String DATA_BASE_NAME = "TO_DO_DATABASE";

    private static final int DATA_BASE_VERSION_1 = 1;


    public static final String TASK_TABLE_NAME = "task_table";

    public static final String COLUMN_TASK_ID = "task_id";

    public static final String COLUMN_TASK_DESCRIPTION = "task_description";

    public static final String COLUMN_TASK_LATITUDE = "task_latitude";

    public static final String COLUMN_TASK_LONGITUDE = "task_longitude";

    public static final String COLUMN_TASK_CATEGORY_ID = "task_category_id";

    public static final String COLUMN_TASK_STATUS = "task_status";

    public static final String COLUMN_TASK_NAME = "task_name";

    public static final String COLUMN_EXPIRE_DATE = "task_expire_date";

    public static final String COLUMN_TASK_USERID = "task_user_id";



    public static final String CATEGORY_TABLE_NAME = "category_table";

    public static final String COLUMN_CATEGORY_ID = "category_id";

    public static final String COLUMN_CATEGORY_NAME = "category_name";

    public static final String COLUMN_CATEGORY_COLOR = "category_color";

    public static final String COLUMN_CATEGORY_USERID = "user_id";



    public static final String USER_TABLE_NAME = "user_table";

    public static final String COLUMN_USER_ID = "user_id";

    public static final String COLUMN_USER_NAME = "user_name";

    public static final String COLUMN_USER_EMAIL = "user_email";

    public static final String COLUMN_USER_PIN = "user_pin";



    public static final String SUBTASK_TABLE_NAME = "subtask_table";

    public static final String COLUMN_SUBTASK_TASKID = "subtask_task_id";


    public static final String QUERY_JOIN_TASK_CATEGORY_ALL = "SELECT "
            + DataBaseManager.CATEGORY_TABLE_NAME + "." + DataBaseManager.COLUMN_CATEGORY_NAME + ", "
            + DataBaseManager.CATEGORY_TABLE_NAME + "." + DataBaseManager.COLUMN_CATEGORY_COLOR + ", "
            + DataBaseManager.CATEGORY_TABLE_NAME + "." + DataBaseManager.COLUMN_CATEGORY_ID + ", "
            + DataBaseManager.TASK_TABLE_NAME + "." + DataBaseManager.COLUMN_TASK_NAME + ", "
            + DataBaseManager.TASK_TABLE_NAME + "." + DataBaseManager.COLUMN_TASK_DESCRIPTION + ", "
            + DataBaseManager.TASK_TABLE_NAME + "." + DataBaseManager.COLUMN_TASK_STATUS + ", "
            + DataBaseManager.TASK_TABLE_NAME + "." + DataBaseManager.COLUMN_TASK_LATITUDE + ", "
            + DataBaseManager.TASK_TABLE_NAME + "." + DataBaseManager.COLUMN_TASK_LONGITUDE + ", "
            + DataBaseManager.TASK_TABLE_NAME + "." + DataBaseManager.COLUMN_TASK_ID + " FROM "
            + DataBaseManager.TASK_TABLE_NAME + " INNER JOIN " + DataBaseManager.CATEGORY_TABLE_NAME
            + " ON " + DataBaseManager.TASK_TABLE_NAME + "." + DataBaseManager.COLUMN_TASK_CATEGORY_ID
            + " = " + DataBaseManager.CATEGORY_TABLE_NAME + "." + DataBaseManager.COLUMN_CATEGORY_ID;

    public static final String QUERY_JOIN_TASK_CATEGORY_SINGLE = QUERY_JOIN_TASK_CATEGORY_ALL
            + " WHERE " + DataBaseManager.TASK_TABLE_NAME + "." + DataBaseManager.COLUMN_TASK_ID
            + " = ?";

    public static final String QUERY_JOIN_TASK_CATEGORY_BY_USER = QUERY_JOIN_TASK_CATEGORY_ALL
            + " WHERE " + DataBaseManager.TASK_TABLE_NAME + "." + DataBaseManager.COLUMN_TASK_USERID
            + " = ?";

    public DataBaseManager(final Context context) {
        super(context, DATA_BASE_NAME, null, DATA_BASE_VERSION_1);
    }

    @Override
    public void onCreate(final SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL("CREATE TABLE " + TASK_TABLE_NAME + "("
                + COLUMN_TASK_ID + " integer primary key autoincrement,"
                + COLUMN_TASK_DESCRIPTION + " text,"
                + COLUMN_TASK_STATUS + " text,"
                + COLUMN_TASK_NAME + " text,"
                + COLUMN_TASK_LONGITUDE + " real,"
                + COLUMN_TASK_LATITUDE + " real,"
                + COLUMN_TASK_CATEGORY_ID + " integer,"
                + COLUMN_EXPIRE_DATE + " integer,"
                + COLUMN_TASK_USERID + " integer"
                + ");");
        sqLiteDatabase.execSQL("CREATE TABLE " + CATEGORY_TABLE_NAME + "("
                + COLUMN_CATEGORY_ID + " integer primary key autoincrement,"
                + COLUMN_CATEGORY_NAME + " text,"
                + COLUMN_CATEGORY_COLOR + " integer,"
                + COLUMN_CATEGORY_USERID + " integer"
                + ");");
        sqLiteDatabase.execSQL("CREATE TABLE " + USER_TABLE_NAME + "("
                + COLUMN_USER_ID + " integer primary key autoincrement,"
                + COLUMN_USER_NAME + " text,"
                + COLUMN_USER_EMAIL + " text,"
                + COLUMN_USER_PIN + " text"
                + ");");
        sqLiteDatabase.execSQL("CREATE TABLE " + SUBTASK_TABLE_NAME + "("
                + COLUMN_TASK_ID + " integer primary key autoincrement,"
                + COLUMN_TASK_DESCRIPTION + " text,"
                + COLUMN_SUBTASK_TASKID + " integer,"
                + COLUMN_TASK_STATUS + " text" + ");");
    }

    @Override
    public void onUpgrade(final SQLiteDatabase sqLiteDatabase, final int oldVersion,
                          final int newVersion) {

    }
}