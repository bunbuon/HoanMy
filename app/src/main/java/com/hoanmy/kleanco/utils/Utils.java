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
        str = str.replaceAll("à|á|ạ|ả|ã|â|ầ|ấ|ậ|ẩ|ẫ|ă|ằ|ắ|ặ|ẳ|ẵ", "a");
        str = str.replaceAll("è|é|ẹ|ẻ|ẽ|ê|ề|ế|ệ|ể|ễ", "e");
        str = str.replaceAll("ì|í|ị|ỉ|ĩ", "i");
        str = str.replaceAll("ò|ó|ọ|ỏ|õ|ô|ồ|ố|ộ|ổ|ỗ|ơ|ờ|ớ|ợ|ở|ỡ", "o");
        str = str.replaceAll("ù|ú|ụ|ủ|ũ|ư|ừ|ứ|ự|ử|ữ", "u");
        str = str.replaceAll("ỳ|ý|ỵ|ỷ|ỹ", "y");
        str = str.replaceAll("đ", "d");

        str = str.replaceAll("À|Á|Ạ|Ả|Ã|Â|Ầ|Ấ|Ậ|Ẩ|Ẫ|Ă|Ằ|Ắ|Ặ|Ẳ|Ẵ", "A");
        str = str.replaceAll("È|É|Ẹ|Ẻ|Ẽ|Ê|Ề|Ế|Ệ|Ể|Ễ", "E");
        str = str.replaceAll("Ì|Í|Ị|Ỉ|Ĩ", "I");
        str = str.replaceAll("Ò|Ó|Ọ|Ỏ|Õ|Ô|Ồ|Ố|Ộ|Ổ|Ỗ|Ơ|Ờ|Ớ|Ợ|Ở|Ỡ", "O");
        str = str.replaceAll("Ù|Ú|Ụ|Ủ|Ũ|Ư|Ừ|Ứ|Ự|Ử|Ữ", "U");
        str = str.replaceAll("Ỳ|Ý|Ỵ|Ỷ|Ỹ", "Y");
        str = str.replaceAll("Đ", "D");
        return str;
    }
}
