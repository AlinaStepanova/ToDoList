package com.example.alina.todolist.data.db;


import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseManager extends SQLiteOpenHelper{
    private static final String DB_NAME = "TODO_DB";
    private static final int DB_VERSION = 1;


    public DatabaseManager(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(createTaskTable());
        db.execSQL(createCategoryTable());
        db.execSQL(createUserTable());
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    private String createTaskTable(){
        return "CREATE TABLE " + Task.TABLE_NAME + "("
                + Task.ID + " integer primary key autoincrement,"
                + Task.NAME + " text,"
                + Task.DESC + " text,"
                + Task.EXP_DATE + " integer,"
                + Task.LOCATION_LAT + " text,"
                + Task.LOCATION_LON + " text,"
                + Task.SUB_TASK + " text,"
                + Task.CATEGORY_ID + " integer,"
                + Task.USER_ID + " integer"
                + ");";
    }

    private String createCategoryTable(){
        return "CREATE TABLE " + Category.TABLE_NAME + "("
                + Category.ID + " integer primary key autoincrement,"
                + Category.NAME + " text,"
                + Category.COLOR + " integer"
                + ");";
    }

    private String createUserTable(){
        return "CREATE TABLE " + User.TABLE_NAME + "("
                + User.ID + " integer primary key autoincrement,"
                + User.NAME + " text,"
                + User.EMAIL + " text"
                + ")";
    }

    class Task{
        public static final String TABLE_NAME = "task";
        public static final String ID = "id";
        public static final String NAME = "name";
        public static final String DESC = "description";
        public static final String EXP_DATE = "exp_date";
        public static final String LOCATION_LAT = "lat";
        public static final String LOCATION_LON = "lon";
        public static final String SUB_TASK = "sub_task";
        public static final String CATEGORY_ID = "category_id";
        public static final String USER_ID = "user_id";
    }

    class Category{
        public static final String TABLE_NAME = "category";
        public static final String ID = "id";
        public static final String NAME = "name";
        public static final String COLOR = "color";
    }

    class User{
        public static final String TABLE_NAME = "user";
        public static final String ID = "id";
        public static final String NAME = "name";
        public static final String EMAIL = "email";
    }
}
