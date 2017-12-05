package com.example.alina.todolist.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import static com.example.alina.todolist.db.DataBaseManager.TaskTable.*;
import static com.example.alina.todolist.db.DataBaseManager.CategoryTable.*;

public class DataBaseManager extends SQLiteOpenHelper {

    private static final String DATA_BASE_NAME = "TO_DO_DATABASE";

    private static final int DATA_BASE_VERSION_1 = 1;

    public interface TaskTable {
        String TASK_TABLE_NAME = "task_table";

        String COLUMN_TASK_ID = "task_id";

        String COLUMN_TASK_UUID = "task_uuid";

        String COLUMN_TASK_DESCRIPTION = "task_description";

        String COLUMN_TASK_CATEGORY_ID = "task_category_id";

        String COLUMN_TASK_STATUS = "task_status";

        String COLUMN_TASK_NAME = "task_name";

        String COLUMN_EXPIRE_DATE = "task_expire_date";

        String COLUMN_TASK_USERID = "task_user_id";
    }

    public interface CategoryTable {
        String CATEGORY_TABLE_NAME = "category_table";

        String COLUMN_CATEGORY_ID = "category_id";

        String COLUMN_CATEGORY_NAME = "category_name";

        String COLUMN_CATEGORY_COLOR = "category_color";

        String COLUMN_CATEGORY_USERID = "user_id";
    }
    /*
     * UserTable
     */
    public static final String USER_TABLE_NAME = "user_table";

    public static final String COLUMN_USER_ID = "user_id";

    public static final String COLUMN_USER_NAME = "user_name";

    public static final String COLUMN_USER_EMAIL = "user_email";

    public static final String COLUMN_USER_PIN = "user_pin";
    /*
     * SubTaskTable
     */
    public static final String SUBTASK_TABLE_NAME = "subtask_table";

    public static final String COLUMN_SUBTASK_ID = "subtask_id";

    public static final String COLUMN_SUBTASK_DESCRIPTION = "subtask_description";

    public static final String COLUMN_SUBTASK_TASKID = "subtask_task_id";



    public DataBaseManager(final Context context) {
        super(context, DATA_BASE_NAME, null, DATA_BASE_VERSION_1);
    }

    @Override
    public void onCreate(final SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL("CREATE TABLE " + TASK_TABLE_NAME + "("
                + COLUMN_TASK_ID + " integer primary key autoincrement,"
                + COLUMN_TASK_UUID + " text,"
                + COLUMN_TASK_DESCRIPTION + " text,"
                + COLUMN_TASK_STATUS + " text,"
                + COLUMN_TASK_NAME + " text,"
                + COLUMN_TASK_CATEGORY_ID + " integer,"
                + COLUMN_EXPIRE_DATE + " integer,"
                + COLUMN_CATEGORY_USERID + " integer"
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
                + COLUMN_SUBTASK_ID + " integer primary key autoincrement,");
    }

    @Override
    public void onUpgrade(final SQLiteDatabase sqLiteDatabase, final int oldVersion,
                          final int newVersion) {

    }
}