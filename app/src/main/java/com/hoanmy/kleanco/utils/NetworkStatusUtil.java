package com.hoanmy.kleanco.utils;

import android.content.Context;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiManager;

/** * Created by ADMIN on 08/03/2015.
 */
public class NetworkStatusUtil {
    private static final String TAG = NetworkStatusUtil.class.getSimpleName();

    public static boolean isMobileAvailable(Context con) {
        try {
            ConnectivityManager connectivityManager = (ConnectivityManager) con.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo mobileInfo = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
            if (mobileInfo.isConnected()) {
                return true;
            }
        } catch (Exception e) {
            //e.printStackTrace();
        }

        return false;
    }

    public static boolean isWifiAvaiable(Context context) {
        if (context == null) return false;

        WifiManager wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);

        return wifiManager.isWifiEnabled();
    }

    public static boolean isGPSEnabale(Context context) {
        LocationManager locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);

        boolean isEnabale = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);

        return isEnabale;
    }

    public static boolean isNetworkAvaiable(Context context) {
        if (context == null) return false;

        try {
            ConnectivityManager cm =
                    (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo netInfo = cm.getActiveNetworkInfo();
            return netInfo != null && netInfo.isConnectedOrConnecting();
        } catch (SecurityException e) {
            return false;
        }
    }
}
