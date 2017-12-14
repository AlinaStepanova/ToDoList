package com.example.alina.todolist.enums;

import java.util.concurrent.TimeUnit;

/**
 * Created by 1996a on 30.11.2017.
 */

public final class Constants {
    public static final long TimeForCheckPassword = TimeUnit.SECONDS.toMillis(5);

    public static final String KEY_PROJECTION = "KEY_PROJECTION";

    public static final String KEY_SELECTION = "KEY_SELECTION";

    public static final String KEY_SELECTION_ARGS = "KEY_SELECTION_ARGS";

    public static final String KEY_SORT_ORDER = "KEY_SORT_ORDER";

    public static final long MIN_TIME_BW_UPDATES = 1000 * 60;// 1 minute

    public static final long MIN_DISTANCE_CHANGE_FOR_UPDATES = 10; // 10 meters

    public static final String LATITUDE = "latitude";

    public static final String LONGITUDE = "longitude";

    public static final String LOCAL_BROADCAST_LOCATION = "local broadcast receiver location";

}
