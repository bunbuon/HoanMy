package com.hoanmy.kleanco.utils;

import android.app.Activity;
import android.content.Intent;

import com.hoanmy.kleanco.CustomerActivity;
import com.hoanmy.kleanco.EmployeeAtivity;
import com.hoanmy.kleanco.LoginActivity;
import com.hoanmy.kleanco.ManagerActivity;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import io.paperdb.Paper;

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
    public static void loginActivity(Activity activity) {
        Paper.book().delete("STATUS_LOGIN");
        Intent intent = new Intent(activity, LoginActivity.class);
        activity.startActivity(intent);
        activity.finish();
    }

    public static void nextHome(Activity activity) {
        Intent intent = new Intent(activity, ManagerActivity.class);
        activity.startActivity(intent);
        activity.finish();
    }

    public static void nextCustomer(Activity activity) {
        Intent intent = new Intent(activity, CustomerActivity.class);
        activity.startActivity(intent);
        activity.finish();
    }

    public static void nextEmployee(Activity activity) {
        Intent intent = new Intent(activity, EmployeeAtivity.class);
        activity.startActivity(intent);
        activity.finish();
    }
}
