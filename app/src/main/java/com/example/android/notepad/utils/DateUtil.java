package com.example.android.notepad.utils;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;


public class DateUtil {
    public static String StringToDate(long time) {
        DateFormat format = new SimpleDateFormat("yyyy-MM-dd", Locale.CHINA);
        return format.format(new Date(time));
    }

    //TODO 详情页具体时间
    public static String StringToDateExactly(long time) {
        DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.CHINA);
        return format.format(new Date(time));
    }

}
