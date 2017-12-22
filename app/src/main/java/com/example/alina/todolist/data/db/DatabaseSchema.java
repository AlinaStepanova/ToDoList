package com.example.alina.todolist.data.db;


public class DatabaseSchema {
    public final class Task{
        public static final String TABLE_NAME = "task";
        public static final String ID = "id";
        public static final String NAME = "name";
        public static final String STATUS = "status";
        public static final String DESC = "description";
        public static final String EXP_DATE = "exp_date";
        public static final String LOCATION_LAT = "lat";
        public static final String LOCATION_LON = "lon";
        public static final String CATEGORY_ID = "category_id";
        public static final String USER_ID = "user_id";
        public static final String IMAGE_URL = "image_url";
        public static final String IMAGE_STATUS = "image_status";


    }

    public final class Category{
        public static final String TABLE_NAME = "category";
        public static final String ID = "id";
        public static final String NAME = "name";
        public static final String COLOR = "color";
        public static final String USER_ID = "user_id";
    }

    public final class User{
        public static final String TABLE_NAME = "user";
        public static final String ID = "id";
        public static final String NAME = "name";
        public static final String EMAIL = "email";
        public static final String PIN = "pin";
    }

    public final class SubTask{
        public static final String TABLE_NAME = "sub_task";
        public static final String ID = "id";
        public static final String DESC = "desc";
        public static final String TASK_ID = "task_id";
        public static final String STATUS = "status";
    }
}
