package com.example.alina.todolist.enums;

import java.text.SimpleDateFormat;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

/**
 * Created by 1996a on 30.11.2017.
 */

public final class Constants {

    private Constants() {}

    public static final long TimeForCheckPassword = TimeUnit.SECONDS.toMillis(30);

    public static final String KEY_PROJECTION = "KEY_PROJECTION";

    public static final String KEY_SELECTION = "KEY_SELECTION";

    public static final String KEY_SELECTION_ARGS = "KEY_SELECTION_ARGS";

    public static final String KEY_SORT_ORDER = "KEY_SORT_ORDER";

    public static final long MIN_TIME_BW_UPDATES = 1000 * 10;// 10 sec

    public static final long MIN_DISTANCE_CHANGE_FOR_UPDATES = 10; // 10 meters

    public static final String ACTION_LOCAL_BROADCAST_LOCATION = "local broadcast receiver location";

    private static final String DATE_FORMAT_STRING = "dd MMMM yyyy";

    public  static  final SimpleDateFormat DATE_FORMAT;

    static {
        DATE_FORMAT = new SimpleDateFormat(DATE_FORMAT_STRING, Locale.getDefault());
    }
}
