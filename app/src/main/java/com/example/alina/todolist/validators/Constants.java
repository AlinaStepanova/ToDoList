package com.example.alina.todolist.validators;

import java.text.SimpleDateFormat;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

/**
 * Created by Alina on 07.11.2017.
 */

public final class Constants {

    private Constants() {}

    private static final String DATE_FORMAT_STRING = "dd MMMM yyyy";
    private static final String IMAGE_DATE_FORMAT_STRING = "yyyyMMdd_HHmmss";
    public  static  final SimpleDateFormat DATE_FORMAT, IMAGE_FORMAT;
    public static final long TimeForCheckPassword = TimeUnit.SECONDS.toMillis(30);

    static {
        DATE_FORMAT = new SimpleDateFormat(DATE_FORMAT_STRING, Locale.getDefault());
        IMAGE_FORMAT = new SimpleDateFormat(IMAGE_DATE_FORMAT_STRING, Locale.getDefault());
    }
}
