package com.hoanmy.kleanco.utils;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Utils {
    private static final long MILLIS_IN_A_DAY = 1000 * 60 * 60 * 24;

    public static String getTime(String day) {

        DateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
        Date date;
        String time;
        switch (day) {
            case "yesterday":
                date = new Date(new Date().getTime() - MILLIS_IN_A_DAY);
                time = dateFormat.format(date);
                break;
            case "now":
                time = dateFormat.format(new Date());
                break;
            case "tomorrow":
                date = new Date(new Date().getTime() + MILLIS_IN_A_DAY);
                time = dateFormat.format(date);
                break;
            default:
                throw new IllegalStateException("Unexpected value: " + day);
        }
        return time;
    }
}
