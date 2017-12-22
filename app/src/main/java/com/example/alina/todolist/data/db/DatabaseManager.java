package com.example.alina.todolist.data.db;


import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseManager extends SQLiteOpenHelper{
    private static final String DB_NAME = "TODO_DB.db";
    private static final int DB_VERSION = 1;

    public DatabaseManager(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(createUserTable());
        db.execSQL(createCategoryTable());
        db.execSQL(createTaskTable());
        db.execSQL(createSubTaskTable());
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    }

    private String createUserTable(){
        return "CREATE TABLE " + DatabaseSchema.User.TABLE_NAME + "("
                + DatabaseSchema.User.ID + " integer primary key autoincrement,"
                + DatabaseSchema.User.NAME + " text,"
                + DatabaseSchema.User.EMAIL + " text,"
                + DatabaseSchema.User.PIN + " text"
                + ")";
    }

    private String createCategoryTable(){
        return "CREATE TABLE " + DatabaseSchema.Category.TABLE_NAME + "("
                + DatabaseSchema.Category.ID + " integer primary key autoincrement,"
                + DatabaseSchema.Category.NAME + " text,"
                + DatabaseSchema.Category.COLOR + " integer,"
                + DatabaseSchema.Category.USER_ID + " integer,"
                + " foreign key (" + DatabaseSchema.Category.USER_ID + ") references " + DatabaseSchema.User.TABLE_NAME + "(" + DatabaseSchema.User.ID + ")"
                + ");";
    }

    private String createTaskTable(){
        return "CREATE TABLE " + DatabaseSchema.Task.TABLE_NAME + "("
                + DatabaseSchema.Task.ID + " integer primary key autoincrement,"
                + DatabaseSchema.Task.NAME + " text,"
                + DatabaseSchema.Task.DESC + " text,"
                + DatabaseSchema.Task.EXP_DATE + " integer,"
                + DatabaseSchema.Task.LOCATION_LAT + " text,"
                + DatabaseSchema.Task.LOCATION_LON + " text,"
                + DatabaseSchema.Task.STATUS + " text,"
                + DatabaseSchema.Task.CATEGORY_ID + " integer,"
                + DatabaseSchema.Task.USER_ID + " integer,"
                + DatabaseSchema.Task.IMAGE_URL + " text,"
                + DatabaseSchema.Task.IMAGE_STATUS + " text,"
                + " foreign key (" + DatabaseSchema.Task.CATEGORY_ID + ") references " + DatabaseSchema.Category.TABLE_NAME + "(" + DatabaseSchema.Category.ID + ")"
                + " foreign key (" + DatabaseSchema.Task.USER_ID + ") references " + DatabaseSchema.User.TABLE_NAME + "(" + DatabaseSchema.User.ID + ")"
                + ");";
    }

    private String createSubTaskTable(){
        return "CREATE TABLE " + DatabaseSchema.SubTask.TABLE_NAME + "("
                + DatabaseSchema.SubTask.ID + " integer primary key autoincrement,"
                + DatabaseSchema.SubTask.DESC + " text,"
                + DatabaseSchema.SubTask.STATUS + " text,"
                + DatabaseSchema.SubTask.TASK_ID + " integer,"
                + " foreign key (" + DatabaseSchema.SubTask.TASK_ID + ") references " + DatabaseSchema.Task.TABLE_NAME + "(" + DatabaseSchema.Task.ID + ")"
                + ");";
    }

    public static final String JOIN_TASK_CATEGORY_ALL = "SELECT "
            + DatabaseSchema.Category.TABLE_NAME + "." + DatabaseSchema.Category.ID + ", "
            + DatabaseSchema.Category.TABLE_NAME + "." + DatabaseSchema.Category.NAME + ", "
            + DatabaseSchema.Category.TABLE_NAME + "." + DatabaseSchema.Category.COLOR + ", "
            + DatabaseSchema.Task.TABLE_NAME + "." + DatabaseSchema.Task.ID + ", "
            + DatabaseSchema.Task.TABLE_NAME + "." + DatabaseSchema.Task.NAME + ", "
            + DatabaseSchema.Task.TABLE_NAME + "." + DatabaseSchema.Task.STATUS + ", "
            + DatabaseSchema.Task.TABLE_NAME + "." + DatabaseSchema.Task.DESC + ", "
            + DatabaseSchema.Task.TABLE_NAME + "." + DatabaseSchema.Task.EXP_DATE + ", "
            + DatabaseSchema.Task.TABLE_NAME + "." + DatabaseSchema.Task.LOCATION_LAT + ", "
            + DatabaseSchema.Task.TABLE_NAME + "." + DatabaseSchema.Task.LOCATION_LON + ", " + " FROM "
            + DatabaseSchema.Task.TABLE_NAME + " INNER JOIN " + DatabaseSchema.Category.TABLE_NAME
            + " ON " + DatabaseSchema.Task.TABLE_NAME + "." + DatabaseSchema.Task.CATEGORY_ID
            + " = " + DatabaseSchema.Category.TABLE_NAME + "." + DatabaseSchema.Category.ID;

    public static final String JOIN_TASK_CATEGORY_SINGLE = JOIN_TASK_CATEGORY_ALL
            + " WHERE " + DatabaseSchema.Task.TABLE_NAME + "." + DatabaseSchema.Task.ID
            + " =?";

    public static final String JOIN_TASK_CATEGORY_USER = JOIN_TASK_CATEGORY_ALL
            + " WHERE " + DatabaseSchema.Task.TABLE_NAME + "." + DatabaseSchema.Task.ID
            + " = ?";
}
