package com.hoanmy.kleanco.utils;

import android.app.Activity;
import android.content.Intent;

import com.google.firebase.messaging.FirebaseMessaging;
import com.hoanmy.kleanco.CustomerActivity;
import com.hoanmy.kleanco.EmployeeAtivity;
import com.hoanmy.kleanco.LoginActivity;
import com.hoanmy.kleanco.ManagerActivity;
import com.hoanmy.kleanco.commons.Constants;
import com.hoanmy.kleanco.models.Login;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import io.paperdb.Paper;

public class Utils {
    public static String TAG = "Hoanmy----";
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

    public static void loginActivity(Activity activity, Login login) {
        Paper.book().destroy();
        FirebaseMessaging.getInstance().unsubscribeFromTopic(Constants.KEY_SUBSCRIBE_ADMIN);
        FirebaseMessaging.getInstance().unsubscribeFromTopic(Constants.KEY_SUBSCRIBE_ALL);
        if (login.getProject_id() != null)
            FirebaseMessaging.getInstance().unsubscribeFromTopic(login.getProject_id());
        if (login.getProjectDetail().getUser_id() != null)
            FirebaseMessaging.getInstance().unsubscribeFromTopic(login.getProjectDetail().getUser_id());
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

    public static String convertString(String str) {
        str = str.replaceAll("??|??|???|???|??|??|???|???|???|???|???|??|???|???|???|???|???", "a");
        str = str.replaceAll("??|??|???|???|???|??|???|???|???|???|???", "e");
        str = str.replaceAll("??|??|???|???|??", "i");
        str = str.replaceAll("??|??|???|???|??|??|???|???|???|???|???|??|???|???|???|???|???", "o");
        str = str.replaceAll("??|??|???|???|??|??|???|???|???|???|???", "u");
        str = str.replaceAll("???|??|???|???|???", "y");
        str = str.replaceAll("??", "d");

        str = str.replaceAll("??|??|???|???|??|??|???|???|???|???|???|??|???|???|???|???|???", "A");
        str = str.replaceAll("??|??|???|???|???|??|???|???|???|???|???", "E");
        str = str.replaceAll("??|??|???|???|??", "I");
        str = str.replaceAll("??|??|???|???|??|??|???|???|???|???|???|??|???|???|???|???|???", "O");
        str = str.replaceAll("??|??|???|???|??|??|???|???|???|???|???", "U");
        str = str.replaceAll("???|??|???|???|???", "Y");
        str = str.replaceAll("??", "D");
        return str;
    }
}
